package com.spgroup.spapp.domain.usecase

import com.spgroup.spapp.domain.model.Partner
import com.spgroup.spapp.domain.model.PartnersListingData
import com.spgroup.spapp.domain.model.PartnersListingItem
import com.spgroup.spapp.domain.model.Promotion


/**
 * Sort list of partner to put the highlighted partner on top
 *
 * Then merge list of partner with list of promotion
 */
class PreProcessPartnerUsecase: SynchronousUsecase() {

    fun run(partnerListingData: PartnersListingData): List<PartnersListingItem> {
        val listSortedPartner = sort(partnerListingData.partners)
        return merge(listSortedPartner, partnerListingData.promotions, 4)
    }

    private fun sort(partners: List<Partner>): List<Partner> {
        return partners.sortedWith(PartnerComparator())
    }

    private fun merge(partners: List<Partner>, promotions: List<Promotion>, promotionIndex: Int): List<PartnersListingItem> {
        if (promotions.isEmpty()) {
            return partners
        } else {
            if (partners.size < promotionIndex) {
                val mergedList: MutableList<PartnersListingItem> = partners.toMutableList()
                mergedList.add(promotions[0])
                return mergedList
            } else {
                val mergedList: MutableList<PartnersListingItem> = partners.toMutableList()
                if (partners.size == promotionIndex) {
                    mergedList.add(promotions[0])
                } else {
                    mergedList.add(promotionIndex, promotions[0])
                }
                return mergedList
            }
        }
    }
}

class PartnerComparator: Comparator<Partner> {

    override fun compare(partner1: Partner?, partner2: Partner?): Int {
        if (partner1 != null && partner2 != null) {
            if (partner1.highlight.isEmpty() && partner2.highlight.isEmpty()) {
                // Both are empty highlight -> same
                return 0
            } else if (!partner1.highlight.isEmpty() && !partner2.highlight.isEmpty()) {
                // Both are highlighted -> same
                return 0
            } else if (partner1.highlight.isEmpty()) {
                // 1 no highlight, 2 has highlight -> put 2 on top
                return 1
            } else {
                // 2 no highlight, 1 has highlight -> put 1 on top
                return -1
            }
        } else if (partner1 == null && partner2 == null) {
            return 0
        } else if (partner1 == null) {
            return 1
        } else {
            return -1
        }
    }

}