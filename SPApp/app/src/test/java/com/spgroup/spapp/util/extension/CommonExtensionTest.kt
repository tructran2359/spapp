package com.spgroup.spapp.util.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class CommonExtensionTest {

    @Test
    fun `formatPrice - should add comma separator`() {

        assertEquals("S$1.50", 1.5f.formatPrice())
        assertEquals("S$100.50", 100.5f.formatPrice())
        assertEquals("S$1,000.50", 1000.5f.formatPrice())
        assertEquals("S$100,000.50", 100000.5f.formatPrice())
        assertEquals("S$1,000,000.50", 1000000.5f.formatPrice())

        assertEquals("S$1.00", 1f.formatPrice())
        assertEquals("S$100.00", 100f.formatPrice())
        assertEquals("S$1,000.00", 1000f.formatPrice())
        assertEquals("S$100,000.00", 100000f.formatPrice())
        assertEquals("S$1,000,000.00", 1000000f.formatPrice())
    }

    @Test
    fun `formatPrice - should not remove 0 before dot`() {
        assertEquals("S$0.50", 0.5f.formatPrice())
    }

    @Test
    fun `formatPriceWithUnit should show unit after formatted price when unit is not empty`() {
        val dummy = 15f
        val unit = "piece"
        assertEquals("S$15.00 / piece", dummy.formatPriceWithUnit(unit))
    }

    @Test
    fun `formatPriceWithUnit should not show unit when unit is empty`() {
        val dummy = 15f
        val unit = ""
        assertEquals("S$15.00", dummy.formatPriceWithUnit(unit))
    }

    @Test
    fun `formatEstPrice should show EST before formatted price`() {
        val dummy = 15f
        assertEquals("EST: S$15.00", dummy.formatEstPrice())
    }

}