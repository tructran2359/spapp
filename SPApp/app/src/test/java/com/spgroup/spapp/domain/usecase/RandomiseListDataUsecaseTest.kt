package com.spgroup.spapp.domain.usecase

import org.junit.Assert
import org.junit.Test

class RandomiseListDataUsecaseTest {

    @Test
    fun `should randomise order of member`() {
        val ori = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val usecase = RandomiseListDataUsecase<Int>()

        for (i in 0..10) {
            val check = usecase.getRandomisedList(ori)
            Assert.assertNotEquals(ori.toString(), check.toString())
        }
    }
}