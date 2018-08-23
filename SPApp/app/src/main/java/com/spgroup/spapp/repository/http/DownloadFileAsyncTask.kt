package com.spgroup.spapp.repository.http

import android.os.AsyncTask
import com.spgroup.spapp.presentation.SPApplication
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Class use to download a file from a url
 *
 * Input: url to file
 *
 * Output: filepath in local storage
 */
class DownloadFileAsyncTask: AsyncTask<String, Unit, Boolean>() {

    private var mListener: OnDownloadListener? = null
    private var mFilePath = ""
    private var mErrorMsg = ""

    override fun onPreExecute() {
        super.onPreExecute()
        mListener?.onStart()
    }

    override fun doInBackground(vararg params: String?): Boolean {
        if (params.isEmpty()) {
            mErrorMsg = "Params should not be empty"
            return false
        } else {
            val urlString = params[0]
            if (urlString == null || urlString.isEmpty()) {
                mErrorMsg = "Url should not be null or empty"
                return false
            } else {
                try {
                    val url = URL(urlString)
                    val urlConnection = url.openConnection()
                    urlConnection.connect()
                    val inputStream = BufferedInputStream(url.openStream())
                    val file = File(SPApplication.mAppInstance.filesDir, "${System.currentTimeMillis()}_pdf_file.pdf")
                    val fileOutputStream = FileOutputStream(file)
                    val byteArray = ByteArray(1024)

                    var count = inputStream.read(byteArray)
                    while (count != -1) {
                        fileOutputStream.write(byteArray, 0, count)
                        count = inputStream.read(byteArray)
                    }
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    inputStream.close()
                    mFilePath = file.absolutePath
                    return true
                } catch (ex: Exception) {
                    mErrorMsg = ex.message ?: ""
                    return false
                }
            }
        }
    }

    override fun onPostExecute(success: Boolean?) {
        super.onPostExecute(success)
        success?.let {
            if (it) {
                mListener?.onSuccess(mFilePath)
            } else {
                mListener?.onError(mErrorMsg)
            }
        }
    }

    fun setOnDownloadLisener(listener: OnDownloadListener) {
        mListener = listener
    }

    interface OnDownloadListener {
        fun onStart()
        fun onSuccess(filePath: String)
        fun onError(errorMsg: String)
    }
}