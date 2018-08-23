package com.spgroup.spapp.domain.model

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
        val discount: String,                           //11
        val offeringTitle: String?,                      //12
        val offering: List<String>?,                     //13
        val banners: List<String>?,                     //14 Nullable
        val categories: List<Category>?,                //15 Nullable
        val menus: List<FoodMenu>?,                     //16 Nullable
        val serviceInfo: PartnerDetailServiceInfo?      //17 Nullable

) {
    fun findService(categoryId: String, serviceId: Int): AbsServiceItem? {
        if (categories != null) {
            categories.forEach { category ->
                if (category.id == categoryId) {
                    category.subCategories.forEach { subCategory: SubCategory ->
                        subCategory.services.forEach { absServiceItem: AbsServiceItem ->
                            if (absServiceItem.getServiceId() == serviceId) {
                                return absServiceItem
                            }
                        }
                    }
                }
            }
        }

        return null
    }
}