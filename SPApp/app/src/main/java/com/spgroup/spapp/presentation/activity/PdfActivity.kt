package com.spgroup.spapp.presentation.activity

//import com.spgroup.spapp.R.id.webview
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.PdfViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.obtainViewModel
import kotlinx.android.synthetic.main.activity_pdf.*
import org.jetbrains.anko.longToast
import java.io.File

class PdfActivity: BaseActivity() {

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_URL = "EXTRA_URL"

        fun getLaunchIntent(context: Context, title: String, url: String?): Intent {
            val intent = Intent(context, PdfActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            val loadUrl = if (url == null) "" else url
            intent.putExtra(EXTRA_URL, loadUrl)
            return intent
        }
    }

    private lateinit var mTitle: String
    private lateinit var mUrl: String
    private lateinit var mViewModel: PdfViewModel

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        mTitle = intent.getStringExtra(EXTRA_TITLE)
        mUrl = intent.getStringExtra(EXTRA_URL)

        mViewModel = obtainViewModel(PdfViewModel::class.java, ViewModelFactory.getInstance())
        subscribeUI()

        action_bar.run {
            setTitle(this@PdfActivity.mTitle)

            setOnBackPress {
                this@PdfActivity.onBackPressed()
            }
        }


        mViewModel.setUpData("http://gahp.net/wp-content/uploads/2017/09/sample.pdf")

//        webview.loadUrl("http://www.pdf995.com/samples/pdf.pdf")

    }

    override fun onDestroy() {
        val filePath = mViewModel.mFilePath.value
        filePath?.run {
            val file = File(this)
            file.delete()
        }
        super.onDestroy()
    }

    private fun subscribeUI() {
        mViewModel.run {
            mFilePath.observe(this@PdfActivity, Observer {
                it?.run {
                    loadPdf(this)
                }
            })

            mIsLoading.observe(this@PdfActivity, Observer {
                it?.run {
                    fl_loading_view.isGone = !(this)
                }
            })

            mErrorMessage.observe(this@PdfActivity, Observer {
                it?.run {
                    doLogE("LoadPdf", "Error: $this")
                    this@PdfActivity.longToast(R.string.error_loading_pdf)
                }
            })
        }
    }

    private fun loadPdf(filePath: String) {
        val file = File(filePath)
        pdf_view.fromFile(file)
                .swipeVertical(true)
                .onPageChange { pageIndex, pageCount ->
                    tv_page_indicator.text = "$pageIndex of $pageCount"
                    if (tv_page_indicator.visibility == View.GONE) {
                        tv_page_indicator.visibility = View.VISIBLE
                    }
                }
                .load()
    }
}