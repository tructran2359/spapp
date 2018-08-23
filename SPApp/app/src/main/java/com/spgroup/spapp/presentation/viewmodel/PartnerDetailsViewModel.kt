package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetCustomisationLowestPrice
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.domain.usecase.SelectedServiceUsecase
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.toInt
import java.io.Serializable

class PartnerDetailsViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase,
        private val getCustomisationLowestPrice: GetCustomisationLowestPrice
) : BaseViewModel() {

    lateinit var partnerUEN: String
//    val mapSelectedServices = HashMap<String, MutableList<ISelectedService>>()
    private val mSelectedServiceUsecase = SelectedServiceUsecase()

    val partnerDetails = MutableLiveData<PartnerDetails>()
    val selectedCount = MutableLiveData<Int>()
    val estimatedPrice = MutableLiveData<Float>()

    init {
        selectedCount.value = 0
    }

    fun loadServices() {
        val disposable = getServicesListByPartnerUsecase
                .getPartnerDetails(partnerUEN)
                .subscribe(
                        {
                            preProcessCustomisationLowestPrice(it)
                            partnerDetails.value = it
                        },
                        { error.value = it }
                )
        disposeBag.add(disposable)
    }

    private fun preProcessCustomisationLowestPrice(partnerDetails: PartnerDetails) {
        partnerDetails.categories?.forEach { cat ->
            cat.subCategories.forEach { subCat ->
                subCat.services.forEach { service ->
                    if (service is ComplexCustomisationService) {
                        val lowestPrice = getCustomisationLowestPrice.run(service.customisations)
                        if (lowestPrice == GetCustomisationLowestPrice.NO_PRICE) {
                            service.priceText = null
                        } else {
                            service.priceText = lowestPrice.formatPrice()
                        }
                    }
                }
            }
        }
    }

    /**
     * Update when Multiplier or Checkbox card is selected/unselected
     * Throw exception when called with Complex card
     */
    fun updateNormalSelectedServiceItem(absServiceItem: AbsServiceItem, count: Int, categoryId: String) {
        mSelectedServiceUsecase.updateNormalSelectedServiceItem(absServiceItem, count, categoryId)
        updateCountAndPrice()
    }


    private fun updateCountAndPrice() {
        selectedCount.value = mSelectedServiceUsecase.calculateTotalCount()

        estimatedPrice.value = mSelectedServiceUsecase.calculateEstPrice()
    }

    fun getCategory(categoryId: String): Category? {
        return partnerDetails
                .value
                ?.categories
                ?.first { it.id == categoryId }
    }

    fun getPartnerInfoModel(): PartnerInformationActivity.PartnerInfo? {
        partnerDetails.value?.run {
            return PartnerInformationActivity.PartnerInfo(
                    name = name,
                    desc = description,
                    offerTitle = offeringTitle,
                    offers = offering,
                    phone = phone,
                    uen = uen,
                    nea = nea,
                    website = website,
                    tnc = tnc
            )
        }

        return null
    }

    fun getSelectedOptionMap(categoryId: String, serviceId: Int): HashMap<Int, Int>? {
        return getSelectedService(categoryId, serviceId)?.let {
            (it as ComplexSelectedService).selectedCustomisation
        }
    }

    fun getSelectedInstruction(categoryId: String, serviceId: Int): String? {
        return mSelectedServiceUsecase.getSelectedInstruction(categoryId, serviceId)
    }

    fun getSelectedService(categoryId: String, serviceId: Int): ISelectedService? {
        return mSelectedServiceUsecase.getSelectedService(categoryId, serviceId)
    }

    fun updateComplexSelectedServiceItem(customiseDisplayData: CustomiseDisplayData) {
        mSelectedServiceUsecase.updateComplexSelectedServiceItem(customiseDisplayData)
        updateCountAndPrice()
    }



    /**
     * Create map of <CategoryId, CategoryName>
     */
    fun getMapSelectedCategories(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        val categories = partnerDetails.value?.categories
        categories?.run {
            val mapSelectedServices = mSelectedServiceUsecase.mapSelectedServices
            for ((cateId, _) in mapSelectedServices) {
                hashMap[cateId] = categories.first { it.id == cateId }.label
            }
        }
        return hashMap
    }

    fun getMapSelectedService() = mSelectedServiceUsecase.mapSelectedServices

    fun getDiscount() = partnerDetails.value?.discount ?: ""

    fun getPartnerName() = partnerDetails.value?.name ?: ""
}

interface ISelectedService {
    fun getSelectedCount(): Int
    fun getEstPrice(): Float
    fun getId() : Int
}

data class SelectedService(
        var service: AbsServiceItem,
        var count: Int = 0,
        var subTotal: Float = 0F
): ISelectedService, Serializable {
    override fun getSelectedCount() = count

    override fun getEstPrice() = subTotal

    override fun getId() = service.getServiceId()
}

data class ComplexSelectedService(
        var service: ComplexCustomisationService,
        var selectedCustomisation: HashMap<Int, Int>?, //<Option Index, Selected Position>
        var subTotal: Float,
        var specialInstruction: String?
): ISelectedService, Serializable {
    override fun getSelectedCount() = (selectedCustomisation != null && !selectedCustomisation!!.isEmpty()).toInt()

    override fun getEstPrice() = subTotal

    override fun getId() = service.getServiceId()
}