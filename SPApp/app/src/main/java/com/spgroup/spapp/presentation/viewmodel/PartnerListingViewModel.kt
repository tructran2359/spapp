package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.spgroup.spapp.domain.model.Supplier
import com.spgroup.spapp.domain.usecase.GetPartnerListingUsecase
import com.spgroup.spapp.util.doLogD
import io.reactivex.disposables.CompositeDisposable

class PartnerListingViewModel(
        private val getPartnerListingUsecase: GetPartnerListingUsecase
) : ViewModel() {

    private val disposeBag = CompositeDisposable()
    val partnerListing = MutableLiveData<List<Supplier>>()
    val error = MutableLiveData<Throwable>()

    fun loadPartnerListing(id: Int) {
        val disposable = getPartnerListingUsecase
                .getPartnerListing(id)
                .subscribe(

                        {
                            doLogD("PListing", "Data: ${it.size}")
                            if (it.size > 3) {
                                val newList = it.toMutableList()
                                val promotion = Supplier(name = "\$50 OFF when \nyou order from SGHeartyMeals.sg", isPromotion = true)
                                newList.add(2, promotion)

                                partnerListing.value = newList
                            } else {
                                partnerListing.value = it
                            }
                        },

                        {
                            error.value = it
                        }
                )

        disposeBag.addAll(disposable)
    }

    fun getPartner(position: Int) = partnerListing.value?.get(position)
}