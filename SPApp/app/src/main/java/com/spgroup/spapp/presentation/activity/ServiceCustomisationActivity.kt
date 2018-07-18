package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.extension.formatPriceWithUnit
import com.spgroup.spapp.util.ConstUtils
import kotlinx.android.synthetic.main.activity_service_customisation.*

class ServiceCustomisationActivity : BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context, item: ServiceItem): Intent {
            val intent = Intent(context, ServiceCustomisationActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_SERVICE_ITEM, item)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mServiceItem: ServiceItem

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_customisation)

        mServiceItem = intent.getSerializableExtra(ConstUtils.EXTRA_SERVICE_ITEM) as ServiceItem

        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {
        val message = "Service:\nName: ${mServiceItem.name}\nPrice: ${mServiceItem.price.formatPriceWithUnit(mServiceItem.unit)}"
        tv_content.setText(message)
    }
}
