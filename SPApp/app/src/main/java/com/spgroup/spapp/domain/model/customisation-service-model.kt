package com.spgroup.spapp.domain.model


sealed class AbsCustomisation(
        val id: Int,
        val label: String
)


class BooleanCustomisation(
        id: Int,
        label: String,
        val price: Float
) : AbsCustomisation(id, label)


class MatrixCustomisation(
        id: Int,
        label: String,
        val min: Int,
        val max: Int,
        val matrixOptions: List<MatrixOptionItem>
) : AbsCustomisation(id, label)


class DropdownCustomisation(
        id: Int,
        label: String,
        val dropdownOptions: List<DropdownOptionItem>
) : AbsCustomisation(id, label)


class NumberCustomisation(
        id: Int,
        label: String,
        val price: Float,
        val min: Int,
        val max: Int
) : AbsCustomisation(id, label)


class MatrixOptionItem(val value: Int, val price: Float)
class DropdownOptionItem(val label: String, val price: Float)