package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCustomisationLowestPriceTest {

    @Test
    fun `should calculate correct lowest price`() {
        val dumbId = -1
        val dumbLabel = ""
        val dumbValue = -1
        val dataSet1 = listOf(
                MatrixCustomisation(dumbId, dumbLabel, dumbValue, dumbValue, listOf(
                        MatrixOptionItem(dumbValue, 3F),
                        MatrixOptionItem(dumbValue, 2F),
                        MatrixOptionItem(dumbValue, 4F)
                )),
                DropdownCustomisation(dumbId, dumbLabel, listOf(
                        DropdownOptionItem(dumbLabel, 3F),
                        DropdownOptionItem(dumbLabel, 5F),
                        DropdownOptionItem(dumbLabel, 4F)
                ))
        )
        val dataSet2 = listOf(

                DropdownCustomisation(dumbId, dumbLabel, listOf(
                        DropdownOptionItem(dumbLabel, 3F),
                        DropdownOptionItem(dumbLabel, 5F),
                        DropdownOptionItem(dumbLabel, 4F)
                )),

                MatrixCustomisation(dumbId, dumbLabel, dumbValue, dumbValue, listOf(
                        MatrixOptionItem(dumbValue, 3F),
                        MatrixOptionItem(dumbValue, 2F),
                        MatrixOptionItem(dumbValue, 4F)
                ))
        )

        assertEquals(2F, GetCustomisationLowestPrice().run(dataSet1))
        assertEquals(3F, GetCustomisationLowestPrice().run(dataSet2))
    }

    @Test
    fun `should return no price`() {
        val dumbId = -1
        val dumbLabel = ""
        val dumbValue = -1
        val dataSet = listOf(
                BooleanCustomisation(dumbId, dumbLabel, 0F),
                MatrixCustomisation(dumbId, dumbLabel, dumbValue, dumbValue, listOf(
                        MatrixOptionItem(dumbValue, 0F),
                        MatrixOptionItem(dumbValue, 0F),
                        MatrixOptionItem(dumbValue, 0F)
                ))
        )
        assertEquals(GetCustomisationLowestPrice().run(dataSet), GetCustomisationLowestPrice.NO_PRICE)
    }
}