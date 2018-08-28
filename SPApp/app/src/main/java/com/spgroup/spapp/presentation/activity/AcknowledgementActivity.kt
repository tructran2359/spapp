package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.RequestAck
import kotlinx.android.synthetic.main.activity_acknowledgement.*

class AcknowledgementActivity : BaseActivity() {

    companion object {
        const val EXTRA_REQUEST_ACK = "EXTRA_REQUEST_ACK"
        fun getLaunchIntent(context: Context, requestAck: RequestAck): Intent {
            val intent = Intent(context, AcknowledgementActivity::class.java)
            intent.putExtra(EXTRA_REQUEST_ACK, requestAck)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acknowledgement)

        val requestAck = intent.getSerializableExtra(EXTRA_REQUEST_ACK) as RequestAck

        tv_request_number.text = getString(R.string.request_number, requestAck.requestNumber)
        tv_title.text = requestAck.title
        tv_detail.text = requestAck.summary

        tv_see_more.setOnClickListener {
            val intent = HomeActivity.getLaunchIntent(this)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
