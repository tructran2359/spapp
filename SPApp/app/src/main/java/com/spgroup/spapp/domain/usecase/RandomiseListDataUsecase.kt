package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.TopLevelPromotion
import java.util.*

open class RandomiseListDataUsecase<T>: SynchronousUsecase() {

    fun getRandomisedList(list: List<T>?): List<T> {
        if (list == null || list.isEmpty()) {
            return listOf()
        }

        val oriList = list.toMutableList()
        val outputList = mutableListOf<T>()
        do {
            val randomIndex = Random().nextInt(oriList.size)
            outputList.add(oriList.removeAt(randomIndex))
        } while (!oriList.isEmpty())
        return outputList
    }
}

class RandomiseListFeaturedPromotionUsecase: RandomiseListDataUsecase<TopLevelPromotion>()