package com.spgroup.spapp.domain.model

import com.spgroup.spapp.presentation.viewmodel.CustomiseData
import com.spgroup.spapp.presentation.viewmodel.CustomiseOption
import com.spgroup.spapp.util.extension.formatPrice
import java.io.Serializable


sealed class AbsCustomisation(
        val id: Int,
        val label: String
): Serializable {

    abstract fun toCustomiseData(): CustomiseData

}


class BooleanCustomisation(
        id: Int,
        label: String,
        val price: Float
) : AbsCustomisation(id, label) {
    override fun toCustomiseData(): CustomiseData {
        val options = listOf(
                CustomiseOption("Yes - ${price.formatPrice()}", price),
                CustomiseOption("No - ${0f.formatPrice()}", 0f))
        return CustomiseData(label, options)
    }
}


class MatrixCustomisation(
        id: Int,
        label: String,
        val min: Int,
        val max: Int,
        val matrixOptions: List<MatrixOptionItem>
) : AbsCustomisation(id, label) {

    override fun toCustomiseData(): CustomiseData {
        val options = matrixOptions.map { it.toCustomiseOption() }
        return CustomiseData(label, options)
    }

}


class DropdownCustomisation(
        id: Int,
        label: String,
        val dropdownOptions: List<DropdownOptionItem>
) : AbsCustomisation(id, label) {

    override fun toCustomiseData(): CustomiseData {
        val options = dropdownOptions.map { it.toCustomiseOption() }
        return CustomiseData(label, options)
    }

}


class NumberCustomisation(
        id: Int,
        label: String,
        val price: Float,
        val min: Int,
        val max: Int
) : AbsCustomisation(id, label) {

    override fun toCustomiseData(): CustomiseData {
        val options = mutableListOf<CustomiseOption>()
        for (i in min..max) {
            options.add(CustomiseOption("$i - ${(i * price).formatPrice()}", i * price))
        }
        return CustomiseData(label, options)
    }

}


class MatrixOptionItem(val value: Int, val price: Float): Serializable {
    fun toCustomiseOption() = CustomiseOption("$value - ${price.formatPrice()}", price)
}
class DropdownOptionItem(val label: String, val price: Float): Serializable {
    fun toCustomiseOption() = CustomiseOption("$label - ${price.formatPrice()}", price)
}