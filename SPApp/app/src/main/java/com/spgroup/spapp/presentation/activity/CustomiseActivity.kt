package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ServiceItem
import com.spgroup.spapp.domain.model.ServiceItemCombo
import com.spgroup.spapp.util.extension.toast
import com.spgroup.spapp.util.ConstUtils
import kotlinx.android.synthetic.main.activity_customise.*

class CustomiseActivity : BaseActivity() {

    companion object {

        fun getLaunchIntent(context: Context, item: ServiceItem): Intent {
            val intent = Intent(context, CustomiseActivity::class.java)
            intent.putExtra(ConstUtils.EXTRA_SERVICE_ITEM, item)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    lateinit var mServiceItem: ServiceItemCombo

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customise)

        mServiceItem = intent.getSerializableExtra(ConstUtils.EXTRA_SERVICE_ITEM) as ServiceItemCombo

        initViews()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun initViews() {
        tv_name.setText(mServiceItem.name)
        tv_description.setText(mServiceItem.description)

        custom_view_1.setName("No. of Pax")
        custom_view_1.setOption("[min. 1 pax]")
        custom_view_1.setLimit(1, 10)
        custom_view_1.setCount(1)

        custom_view_2.setName("Plain Rice")
        custom_view_2.setOption("[optional]")
        custom_view_2.setLimit(0, 10)
        custom_view_2.setCount(1)

        tv_add_to_request.setOnClickListener {
            this.toast("CLICKED")
        }

        action_bar.setOnBackPress {
            onBackPressed()
        }
    }
}
