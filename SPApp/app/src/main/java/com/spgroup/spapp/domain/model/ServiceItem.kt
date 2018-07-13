package com.spgroup.spapp.domain.model

abstract class ServiceItem(
        open var name: String,
        open var price: Int,
        open var unit: String
) {
    abstract fun getItemCount(): Int
}

class CounterServiceItem(
        override var name: String,
        override var price: Int,
        override var unit: String,
        var minCount: Int = 0,
        var maxCount: Int = 0,
        var count: Int = 0
) : ServiceItem(name, price, unit) {

    override fun getItemCount() = count
}

class CheckBoxServiceItem(
        override var name: String,
        override var price: Int,
        override var unit: String,
        var description: String,
        var selected: Boolean

): ServiceItem(name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0
}