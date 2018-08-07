package com.spgroup.spapp.domain.model

import com.spgroup.spapp.presentation.viewmodel.CustomiseViewModel
import java.io.Serializable

abstract class ServiceItem(
        open val id: Int,
        open var name: String,
        open var price: Float,
        open var unit: String
): Serializable {
    abstract fun getItemCount(): Int
    abstract fun copy(): ServiceItem
}

class ServiceItemCounter(
        override val id: Int,
        override var name: String,
        override var price: Float,
        override var unit: String,
        var minCount: Int = 0,
        var maxCount: Int = 0,
        var count: Int = 0
) : ServiceItem(id, name, price, unit) {

    override fun getItemCount() = count

    override fun copy() = ServiceItemCounter(id, name, price, unit, minCount, maxCount, count)
}

class ServiceItemCheckBox(
        override val id: Int,
        override var name: String,
        override var price: Float,
        override var unit: String,
        var description: String,
        var selected: Boolean

): ServiceItem(id, name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0

    override fun copy() = ServiceItemCheckBox(id, name, price, unit, description, selected)
}

class ServiceItemCombo(
        override val id: Int,
        override var name: String,
        override var price: Float,
        override var unit: String,
        var description: String,
        var selected: Boolean
) : ServiceItem(id, name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0

    override fun copy() = ServiceItemCombo(id, name, price, unit, description, selected)
}

class ServiceItemComboDummy(
        override val id: Int,
        override var name: String,
        override var price: Float,
        override var unit: String,
        var description: String,
        var selected: Boolean,
        var dummyContent: CustomiseViewModel.Content
) : ServiceItem(id, name, price, unit) {

    override fun getItemCount() = if (selected) 1 else 0

    override fun copy() = ServiceItemComboDummy(id, name, price, unit, description, selected, dummyContent)

    fun toServiceItemCombo() = ServiceItemCombo(id, name, price, unit, description, selected)
}