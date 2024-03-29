package com.spgroup.spapp.domain.model

import com.spgroup.spapp.presentation.activity.PartnerInformationActivity
import com.spgroup.spapp.util.extension.toFloatWithException
import java.io.Serializable

data class PartnerDetails(
        val uen: String,                                //1
        val name: String,                               //2
        val partnerType: String,                        //3
        val categoryId: String,                         //4
        val nea: String?,                                //5
        val description: String?,                        //6
        val phone: String?,                              //7
        val website: String?,                            //8
        val tnc: String?,                               //9  Nullable
        val promo: String?,                             //10 Nullable
        val discount: String?,                           //11
        val offeringTitle: String?,                      //12
        val offering: List<String>?,                     //13
        val banners: List<String>?,                     //14 Nullable
        val categories: List<Category>?,                //15 Nullable
        val menu: FoodMenu?,                           //16 Nullable
        val serviceInfo: PartnerDetailServiceInfo?,      //17 Nullable
        val email: String?,
        val minimumOrderAmount: String?,
        val amountDiscount: String?,
        val amountDiscountLabel: String?

): Serializable {

    fun getPercentageDiscountValue() = discount?.toFloatWithException() ?: 0f

    fun getCategoryById(cateId: String) = categories?.firstOrNull { cateId == it.id }

    fun getSubCateByServiceId(serviceId: Int): SubCategory? {
        categories?.forEach { category ->
            category.subCategories.forEach { subCate ->
                val service = subCate.services.firstOrNull { serviceId == it.getServiceId() }
                if (service != null) {
                    return subCate
                }
            }
        }

        return null
    }

    fun getSubCateByCateIdAndServiceId(cateId: String, serviceId: Int): SubCategory? {
        categories
                ?.firstOrNull { category -> cateId == category.id }
                ?.subCategories?.forEach { subCate ->
                    val service = subCate.services.firstOrNull { service -> serviceId == service.getServiceId() }
                    if (service != null) {
                        return subCate
                    }
                }
        return null
    }

    fun getPartnerInfoModel() = PartnerInformationActivity.PartnerInfo(
            name = name,
            desc = description,
            offeringTitle = offeringTitle,
            offering = offering,
            phone = phone,
            uen = uen,
            nea = nea,
            website = website,
            tnc = tnc,
            email = email
    )

    fun getMinimumOrderValue() = minimumOrderAmount?.toFloatWithException() ?: 0f

    fun getAmountDiscountValue() = amountDiscount?.toFloatWithException() ?: 0f
}