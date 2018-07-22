package com.spgroup.spapp.repository

import com.spgroup.spapp.domain.ServicesRepository
import com.spgroup.spapp.domain.model.*
import io.reactivex.Single

class ServicesDataMock : ServicesRepository {

    override fun getTopLevelServiceCategories(): Single<List<TopLevelServiceCategory>> {
        return Single.fromCallable {
            listOf(TopLevelServiceCategory(1, "Food"),
                    TopLevelServiceCategory(2, "House-keeping"),
                    TopLevelServiceCategory(3, "Aircon Servicing"),
                    TopLevelServiceCategory(4, "Laundry"),
                    TopLevelServiceCategory(5, "Education"),
                    TopLevelServiceCategory(6, "Grocery"))
        }
    }

    override fun getSuppliersByCategory(categoryId: Int): Single<List<Supplier>> {
        return Single.fromCallable {
            listOf(Supplier(1, "QuiKlean Laundry"),
                    Supplier(2, "ABC Laundry"),
                    Supplier(3, "XYZ Laundry"))
        }
    }

    override fun getSupplierServicesDetails(supplierId: Int): Single<List<SupplierServiceCategory>> {
        return Single.fromCallable {

            val listCategoryName = listOf("Dry Clean", "Wash & Press", "Press Only", "Wash & Fold", "Curtains & Carpets")
            val listServiceCategory = mutableListOf<SupplierServiceCategory>()

            // Create 5 dummy category
            for (cateId in 1..5) {

                // Create 2 service for each category
                val listServiceGroup = mutableListOf<ServiceGroup>()
                for (serviceId in 1..2) {
                    val service1 = ServiceItemCounter(
                            "Paint $serviceId",
                            10f,
                            "item")

                    val service2 = ServiceItemCheckBox(
                            "Day Curtains $serviceId",
                            10f,
                            "piece",
                            "Also known as sheers, made of light coloured materials to allow light in from outside.",
                            false)

                    val service3 = ServiceItemCheckBox(
                            "Night Curtains $serviceId",
                            9f,
                            "kg",
                            "Heavier in weight and are of dark coloured materials. These are drawn at night for privacy.",
                            false
                    )

                    val service4 = ServiceItemCombo (
                            "3 Dishes Plus 1 Soup Meal Set",
                            165f,
                            "month",
                            "Weekdays only. Island-wide delivery. Packed in microwavable containers only.",
                            false
                    )
                    val serviceGroup = ServiceGroup("GARMENTS $serviceId",
                            "Includes free dismantling & installation. Measurement & evaluation will be done on-site, price estimation will not be included in this request.",
                            mutableListOf(service1, service2, service3, service4),
                            false)
                    listServiceGroup.add(serviceGroup)
                }

                val supplierServiceCategory = SupplierServiceCategory(
                        id = cateId,
                        title = listCategoryName[cateId - 1],
                        services = listServiceGroup)
                listServiceCategory.add(supplierServiceCategory)

            }

            // Create This week's menu
            val thisWeekMenuCate = SupplierServiceCategory(
                    6,
                    SupplierServiceCategory.TYPE_WEEKLY_MENU,
                    "This week's menu",
                    mutableListOf())
            listServiceCategory.add(thisWeekMenuCate)

            // Return:
            listServiceCategory
        }
    }

}