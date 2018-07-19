package com.spgroup.spapp.domain.model

import java.io.Serializable

abstract class ServiceItem(
        open var name: String,
        open var price: Float,
        open var unit: String
): Serializable {
    abstract fun getItemCount(): Int
    abstract fun copy(): ServiceItem
}

class ServiceItemCounter(
        override var name: String,
        override var price: Float,
        override var unit: String,
        var minCount: Int = 0,
        var maxCount: Int = 0,
        var count: Int = 0
) : ServiceItem(name, price, unit) {

    override fun getItemCount() = count

    override fun copy() = ServiceItemCounter(name, price, unit, minCount, maxCount, count)
}

class ServiceItemCheckBox(
        override var name: String,
        override var price: Float,
        override var unit: String,
        var description: String,
        var selected: Boolean

): ServiceItem(name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0

    override fun copy() = ServiceItemCheckBox(name, price, unit, description, selected)
}

class ServiceItemCombo(
        override var name: String,
        override var price: Float,
        override var unit: String,
        var description: String,
        var selected: Boolean
) : ServiceItem(name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0

    override fun copy() = ServiceItemCombo(name, price, unit, description, selected)
}