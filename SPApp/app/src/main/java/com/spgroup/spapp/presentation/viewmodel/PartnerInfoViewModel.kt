package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.usecase.GetPartnerDetailsUsecase
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity.PartnerInfo
import javax.inject.Inject

class PartnerInfoViewModel @Inject constructor(
        private val getServicesListByPartnerUsecase: GetPartnerDetailsUsecase
): BaseViewModel() {

    val mPartnerInfo = MutableLiveData<PartnerInfo>()

    fun showData(partnerInfo: PartnerInfo) {
        mPartnerInfo.value = partnerInfo
    }

    fun loadData(uen: String) {
        val disposable = getServicesListByPartnerUsecase
                .getPartnerDetails(uen)
                .subscribe(
                        {
                            it?.run {
                                mPartnerInfo.value = getPartnerInfoModel()

                                // To simulate api call error
//                                error.value = Throwable("Test error Partner Info")
                            }
                        },
                        { error.value = it}
                )
        disposeBag.addAll(disposable)
    }
}