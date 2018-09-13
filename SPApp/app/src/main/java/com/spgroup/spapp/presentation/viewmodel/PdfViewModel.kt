package com.spgroup.spapp.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.spgroup.spapp.repository.http.DownloadFileAsyncTask
import javax.inject.Inject

class PdfViewModel @Inject constructor(): BaseViewModel(), DownloadFileAsyncTask.OnDownloadListener {

    val mFilePath = MutableLiveData<String>()
    val mIsLoading = MutableLiveData<Boolean>()
    val mErrorMessage = MutableLiveData<String>()
    private lateinit var mUrl: String
    private var mDownloadTask: DownloadFileAsyncTask? = null

    fun setUpData(url: String) {
        mUrl = url
        loadPdfFile()
    }

    private fun loadPdfFile() {
        if (mUrl.isEmpty()) {
            mErrorMessage.value = "Empty URL"
        }
        mDownloadTask = DownloadFileAsyncTask()
        mDownloadTask?.setOnDownloadLisener(this)
        mDownloadTask?.execute(mUrl)
    }

    ///////////////////////////////////////////////////////////////////////////
    // DownloadFileAsyncTask.OnDownloadListener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        mIsLoading.value = true
    }

    override fun onSuccess(filePath: String) {
        mIsLoading.value = false
        mFilePath.value = filePath
    }

    override fun onError(errorMsg: String) {
        mIsLoading.value = false
        mErrorMessage.value = errorMsg
    }
}