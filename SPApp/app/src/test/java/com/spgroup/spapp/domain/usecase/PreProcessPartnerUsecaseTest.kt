package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingData
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.junit.Assert.assertThat
import org.junit.Test

class PreProcessPartnerUsecaseTest {

    @Test
    fun `should put 2 on top if 2 is highlighted`() {
        val dumb = createDumbPartnerModel()
        val dumb1 = dumb.copy(uen = "1", highlight = "")
        val dumb2 = dumb.copy(uen = "2", highlight = "Sponsored")
        val dumb3 = dumb.copy(uen = "3", highlight = "")
        val dumbData = PartnersListingData(listOf(dumb1, dumb2, dumb3), listOf())

        val sorted = PreProcessPartnerUsecase().run(dumbData)
        val listId = getListId(sorted)

        assertThat(listId.toString(), anyOf(`is`("[2, 1, 3]"), `is`("[2, 3, 1]")))
    }

    @Test
    fun `should put 2, 3 on top if 2, 3 are highlighted`() {
        val dumb = createDumbPartnerModel()
        val dumb1 = dumb.copy(uen = "1", highlight = "")
        val dumb2 = dumb.copy(uen = "2", highlight = "Sponsored")
        val dumb3 = dumb.copy(uen = "3", highlight = "Highlight")
        val dumbData = PartnersListingData(listOf(dumb1, dumb2, dumb3), listOf())

        val sorted = PreProcessPartnerUsecase().run(dumbData)
        val listId = getListId(sorted)

        assertThat(listId.toString(), anyOf(`is`("[2, 3, 1]"), `is`("[3, 2, 1]")))
    }

    @Test
    fun `should put 1, 3 on top if 1, 3 are highlighted`() {
        val dumb = createDumbPartnerModel()
        val dumb1 = dumb.copy(uen = "1", highlight = "Sponsored")
        val dumb2 = dumb.copy(uen = "2", highlight = "")
        val dumb3 = dumb.copy(uen = "3", highlight = "Highlight")
        val dumbData = PartnersListingData(listOf(dumb1, dumb2, dumb3), listOf())

        val sorted = PreProcessPartnerUsecase().run(dumbData)
        val listId = getListId(sorted)

        assertThat(listId.toString(), anyOf(`is`("[1, 3, 2]"), `is`("[3, 1, 2]")))
    }

    @Test
    fun `should put 1, 3 on top if 1, 3 are highlighted and promotion at bottom`() {
        val dumb = createDumbPartnerModel()
        val dumb1 = dumb.copy(uen = "1", highlight = "Sponsored")
        val dumb2 = dumb.copy(uen = "2", highlight = "")
        val dumb3 = dumb.copy(uen = "3", highlight = "Highlight")

        val dumbPromotion1 = createDumbPromotionModel().copy(partnerId = "10")

        val dumbData = PartnersListingData(listOf(dumb1, dumb2, dumb3), listOf(dumbPromotion1))

        val sorted = PreProcessPartnerUsecase().run(dumbData)
        val listId = getListId(sorted)


        assertThat(listId.toString(), anyOf(`is`("[1, 3, 2, 10]"), `is`("[3, 1, 2, 10]")))
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dummy method
    ///////////////////////////////////////////////////////////////////////////

    private fun createDumbPartnerModel() = Partner(
            "0",
            "P1",
            "/dumburl",
            "dumb",
            "highlight",
            "dumbtype")

    private fun createDumbPromotionModel() = Promotion (
            "dumb",
            "dumb",
            "dumb",
            "dumb",
            "dumb",
            "-1",
            "dumb"
    )

    private fun getListId(list: List<PartnersListingItem>) = list.map {
        when(it) {
            is Partner -> it.uen
            is Promotion -> it.partnerId
            else -> "invalid"
        }
    }
}