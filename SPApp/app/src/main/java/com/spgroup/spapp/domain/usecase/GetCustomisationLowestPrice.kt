package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.*

class GetCustomisationLowestPrice : SynchronousUsecase() {

    companion object {
        const val NO_PRICE = -1F
    }

    fun run(dataSet: List<AbsCustomisation>): Float {
        return dataSet
                .map { absCustomisationItem ->
                    when (absCustomisationItem) {
                        is BooleanCustomisation -> listOf(absCustomisationItem.price)
                        is NumberCustomisation -> listOf(absCustomisationItem.price)
                        is MatrixCustomisation -> {
                            absCustomisationItem.matrixOptions
                                    .map { it.price }
                        }
                        is DropdownCustomisation -> {
                            absCustomisationItem.dropdownOptions
                                    .map { it.price }
                        }
                    }
                }
                .flatten()
                .filter { it > 0 }
                .toFloatArray()
                .min() ?: NO_PRICE
    }
}