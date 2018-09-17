package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.*

class GetCustomisationLowestPrice : SynchronousUsecase() {

    companion object {
        const val NO_PRICE = -1F
    }

    fun run(dataSet: List<AbsCustomisation>): Float {
        return if (dataSet.isEmpty()) {
            NO_PRICE
        } else {
            val firstCustom = dataSet[0]
            val listPrice = when (firstCustom) {
                is BooleanCustomisation -> listOf(firstCustom.price)
                is NumberCustomisation -> listOf(firstCustom.price)
                is MatrixCustomisation -> {
                    firstCustom.matrixOptions
                            .map { it.price }
                }
                is DropdownCustomisation -> {
                    firstCustom.dropdownOptions
                            .map { it.price }
                }
            }

            // RETURN
            // Need to call `asSequence` coz Kotlin recommend that when do method chain with collection
            listPrice
                    .asSequence()
                    .filter { it > 0 }
                    .min() ?: NO_PRICE
        }
    }
}