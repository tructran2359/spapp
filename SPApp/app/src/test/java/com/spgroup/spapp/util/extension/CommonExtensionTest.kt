package com.spgroup.spapp.util.extension

import org.junit.Assert
import org.junit.Test

class CommonExtensionTest {

    @Test
    fun `should add comma separator`() {
        Assert.assertEquals("S$0.50", 0.5f.formatPrice())

        Assert.assertEquals("S$1.50", 1.5f.formatPrice())
        Assert.assertEquals("S$100.50", 100.5f.formatPrice())
        Assert.assertEquals("S$1,000.50", 1000.5f.formatPrice())
        Assert.assertEquals("S$100,000.50", 100000.5f.formatPrice())
        Assert.assertEquals("S$1,000,000.50", 1000000.5f.formatPrice())

        Assert.assertEquals("S$1.00", 1f.formatPrice())
        Assert.assertEquals("S$100.00", 100f.formatPrice())
        Assert.assertEquals("S$1,000.00", 1000f.formatPrice())
        Assert.assertEquals("S$100,000.00", 100000f.formatPrice())
        Assert.assertEquals("S$1,000,000.00", 1000000f.formatPrice())
    }

}