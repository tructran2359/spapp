package com.spgroup.spapp.presentation.activity

//import com.spgroup.spapp.R.id.webview
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import kotlinx.android.synthetic.main.activity_pdf.*

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

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        mTitle = intent.getStringExtra(EXTRA_TITLE)
        mUrl = intent.getStringExtra(EXTRA_URL)

        action_bar.run {
            setTitle(this@PdfActivity.mTitle)

            setOnBackPress {
                this@PdfActivity.onBackPressed()
            }
        }
        webview.getSettings().setJavaScriptEnabled(true)
        if (mUrl.endsWith(".pdf")) {
            webview.loadUrl("https://docs.google.com/gview?url=$mUrl")
        } else {
            webview.loadUrl(mUrl)
        }

//        webview.loadUrl("http://www.pdf995.com/samples/pdf.pdf")

    }
}