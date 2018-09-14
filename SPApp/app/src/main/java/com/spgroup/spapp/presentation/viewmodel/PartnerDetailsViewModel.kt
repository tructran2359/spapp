package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.Category
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.PartnerDetails
import com.spgroup.spapp.domain.usecase.GetCustomisationLowestPrice
import com.spgroup.spapp.domain.usecase.GetPartnerDetailsUsecase
import com.spgroup.spapp.domain.usecase.SelectedServiceUsecase
import com.spgroup.spapp.presentation.activity.CustomiseDisplayData
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.toInt
import java.io.Serializable
import javax.inject.Inject

class PartnerDetailsViewModel @Inject constructor(
        private val getServicesListByPartnerUsecase: GetPartnerDetailsUsecase,
        private val getCustomisationLowestPrice: GetCustomisationLowestPrice
) : BaseViewModel() {

    lateinit var partnerUEN: String
//    val mapSelectedServices = HashMap<String, MutableList<ISelectedService>>()
    private val mSelectedServiceUsecase = SelectedServiceUsecase()

    val partnerDetails = MutableLiveData<PartnerDetails>()
    val selectedCount = MutableLiveData<Int>()
    val estimatedPrice = MutableLiveData<Float>()
    val newSelectedComplexServiceWithCateId = MutableLiveData<Pair<String, Int>>()
    val refreshData = MutableLiveData<Boolean>()
    val updateData = MutableLiveData<Boolean>()

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

                            // To simulate api call error
//                            error.value = Throwable("Test API Error Partner Detail")
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

    fun removeSelectedService(categoryId: String, serviceId: Int) {
        mSelectedServiceUsecase.removeSelectedItem(categoryId, serviceId)
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
            return getPartnerInfoModel()
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

    fun addComplexSelectedServiceItem(customiseDisplayData: CustomiseDisplayData) {
        mSelectedServiceUsecase.updateComplexSelectedServiceItem(customiseDisplayData)
        newSelectedComplexServiceWithCateId.value = Pair(customiseDisplayData.categoryId, customiseDisplayData.serviceItem.id)
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

    fun getMapSelectedServices() = mSelectedServiceUsecase.mapSelectedServices

    fun getDiscount() = partnerDetails.value?.discount ?: ""

    fun getPartnerName() = partnerDetails.value?.name ?: ""

    fun getSubCateName(categoryId: String, serviceId: Int) = partnerDetails.value?.getSubCateByCateIdAndServiceId(categoryId, serviceId)?.label ?: ""

    fun clearAllSelectedService() {
        mSelectedServiceUsecase.clearAllSelectedServices()
        updateCountAndPrice()
        refreshData.value = true
    }

    fun updateSelectedService(updatedMap: HashMap<String, MutableList<ISelectedService>>) {
        mSelectedServiceUsecase.mapSelectedServices = updatedMap
        updateCountAndPrice()
        updateData.value = true
    }
}

interface ISelectedService {
    fun getSelectedCount(): Int
    fun getEstPrice(): Float
    fun getId() : Int
    fun getServiceType(): String
    fun getServiceName(): String
    fun getSelectedCustomisationLabel(): String?
    fun getSpectialInstructions(): String?
}

data class SelectedService(
        var service: AbsServiceItem,
        var count: Int = 0,
        var subTotal: Float = 0F
): ISelectedService, Serializable {
    override fun getSelectedCount() = count

    override fun getEstPrice() = subTotal

    override fun getId() = service.getServiceId()

    override fun getServiceType() = service.getServiceType()

    override fun getServiceName() = service.getServiceName()

    override fun getSelectedCustomisationLabel() = null

    override fun getSpectialInstructions() = null
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

    override fun getServiceType() = service.getServiceType()

    override fun getServiceName() = service.getServiceName()

    override fun getSelectedCustomisationLabel(): String? {
        selectedCustomisation?.run {
            val firstKey = keys.first()
            val firstValue = this[firstKey]
            if (firstValue != null) {
                return service.getSelectedCustomisationLabel(firstKey, firstValue)
            }
        }

        return null
    }

    override fun getSpectialInstructions() = specialInstruction
}