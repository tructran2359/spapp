package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.*
import io.reactivex.Single

class ServicesDataMock : ServicesRepository {

    override fun getTopLevelServiceCategories(): Single<List<TopLevelCategory>> {
        return Single.fromCallable {
            listOf(TopLevelCategory("food", "Food"),
                    TopLevelCategory("housekeeping", "House-keeping"),
                    TopLevelCategory("aircon", "Aircon Servicing"),
                    TopLevelCategory("laundry", "Laundry"),
                    TopLevelCategory("education", "Education"),
                    TopLevelCategory("groceries", "Grocery"))
        }
    }

    override fun getSuppliersByCategory(categoryId: String): Single<List<Partner>> {
        return Single.fromCallable {
            val list = mutableListOf<Partner>()
            for (i in 1..10) {
                list.add(
                        Partner(
                        id = i,
                        name = "Partner $i",
                        price = 165f + i,
                        unit = "month",
                        imgUrl = "testUrl $i",
                        isSponsored = (i % 2 == 0))
                )
            }

            list
        }
    }

    override fun getSupplierServicesDetails(supplierId: Int): Single<List<ServiceCategory>> {
        return Single.fromCallable {

            val listCategoryName = listOf("Dry Clean", "Wash & Press", "Press Only", "Wash & Fold", "Curtains & Carpets")
            val listServiceCategory = mutableListOf<ServiceCategory>()

            // Create 5 dummy category
            for (cateId in 1..5) {

                // Create 2 service for each category
                val listServiceGroup = mutableListOf<ServiceGroup>()
                for (serviceId in 1..2) {
                    val service1 = ServiceItemCounter(
                            0,
                            "Paint $serviceId",
                            10f,
                            "item")

                    val service2 = ServiceItemCheckBox(
                            0,
                            "Day Curtains $serviceId",
                            10f,
                            "piece",
                            "Also known as sheers, made of light coloured materials to allow light in from outside.",
                            false)

                    val service3 = ServiceItemCheckBox(
                            0,
                            "Night Curtains $serviceId",
                            9f,
                            "kg",
                            "Heavier in weight and are of dark coloured materials. These are drawn at night for privacy.",
                            false
                    )

                    val service4 = ServiceItemCombo (
                            0,
                            "3 Dishes Plus 1 Soup Meal Set",
                            165f,
                            "month",
                            "Weekdays only. Island-wide delivery. Packed in microwavable containers only.",
                            false
                    )
                    val serviceGroup = ServiceGroup(
                            0,
                            "GARMENTS $serviceId",
                            "Includes free dismantling & installation. Measurement & evaluation will be done on-site, price estimation will not be included in this request.",
                            mutableListOf(service1, service2, service3, service4),
                            false)
                    listServiceGroup.add(serviceGroup)
                }

                val supplierServiceCategory = ServiceCategory(
                        id = cateId,
                        title = listCategoryName[cateId - 1],
                        services = listServiceGroup)
                listServiceCategory.add(supplierServiceCategory)

            }

            // Create This week's menu
            val thisWeekMenuCate = ServiceCategory(
                    6,
                    ServiceCategory.TYPE_WEEKLY_MENU,
                    "This week's menu",
                    mutableListOf())
            listServiceCategory.add(thisWeekMenuCate)

            // Return:
            listServiceCategory
        }
    }

}