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
            val service1 = ServiceItemCounter(
                    "Paint",
                    10f,
                    "item")
            val service2 = ServiceItemCheckBox(
                    "Day Curtains",
                    10f,
                    "piece",
                    "Also known as sheers, made of light coloured materials to allow light in from outside.",
                    false)
            val service3 = ServiceItemCheckBox(
                    "Night Curtains",
                    9f,
                    "kg",
                    "Heavier in weight and are of dark coloured materials. These are drawn at night for privacy.",
                    false
            )

            val serviceGroup1 = ServiceGroup("GARMENTS",
                    "Includes free dismantling & installation. Measurement & evaluation will be done on-site, price estimation will not be included in this request.",
                    mutableListOf(service1, service2, service3),
                    false)
            val serviceGroup2 = serviceGroup1.copy(name = "GARMENTS 2")

            listOf(
                    SupplierServiceCategory(1, "Dry Clean", listOf(serviceGroup1, serviceGroup2)),
                    SupplierServiceCategory(2, "Wash & Press", listOf(serviceGroup1, serviceGroup2)),
                    SupplierServiceCategory(3, "Press Only", listOf(serviceGroup1, serviceGroup2)),
                    SupplierServiceCategory(4, "Wash & Fold", listOf(serviceGroup1, serviceGroup2)),
                    SupplierServiceCategory(5, "Curtains & Carpets", listOf(serviceGroup1, serviceGroup2)))
        }
    }

}