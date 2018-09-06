package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.domain.usecase.GetServicesListByPartnerUsecase
import com.spgroup.spapp.presentation.activity.PartnerInformationActivity.PartnerInfo

class PartnerInfoViewModel(
        private val getServicesListByPartnerUsecase: GetServicesListByPartnerUsecase
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