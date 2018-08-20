package com.spgroup.spapp.presentation.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.presentation.view.DropdownSelectionView
import com.spgroup.spapp.presentation.viewmodel.CustomiseData
import com.spgroup.spapp.presentation.viewmodel.CustomiseNewViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.extension.formatPrice
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.obtainViewModel
import com.spgroup.spapp.util.extension.updateVisibility
import kotlinx.android.synthetic.main.activity_customise_new.*

class CustomiseNewActivity: BaseActivity() {

    companion object {
        const val EXTRA_SERVICE_ITEM = "EXTRA_SERVICE_ITEM"

        fun getLaunchIntent(context: Context, serviceItem: ComplexCustomisationService): Intent {
            val intent = Intent(context, CustomiseNewActivity::class.java)
            intent.putExtra(EXTRA_SERVICE_ITEM, serviceItem)
            return intent
        }
    }

    private lateinit var mViewModel: CustomiseNewViewModel
    private val listDropdownViews = mutableListOf<DropdownSelectionView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_customise_new)

        mViewModel = obtainViewModel(CustomiseNewViewModel::class.java, ViewModelFactory.getInstance())
        mViewModel.run {

            serviceName.observe(this@CustomiseNewActivity, Observer {
                it?.let {
                    tv_name.text = it
                }
            })

            serviceDescription.observe(this@CustomiseNewActivity, Observer {
                it?.let {
                    tv_description.text = it
                }
            })

            listData.observe(this@CustomiseNewActivity, Observer {
                it?.let {
                    setUpData(it)
                }
            })

            totalPriceData.observe(this@CustomiseNewActivity, Observer {
                it?.let {
                    tv_est_price.text = it.formatPrice()
                }
            })

            initData(intent.getSerializableExtra(EXTRA_SERVICE_ITEM) as ComplexCustomisationService)
        }

        setUpViews()
    }

    private fun setUpData(listData: List<CustomiseData>) {
        listData.forEachIndexed { index, data ->
            val view = DropdownSelectionView(this@CustomiseNewActivity).apply {
                setTitle(data.title)
                setData(data.options.map { it.label })
                setOnItemSelectedListener { selectedPos ->
                    mViewModel.notifyDataChanged(index, selectedPos)
                }
            }

            ll_option_container.addView(view)
            listDropdownViews.add(view)
        }
    }

    private fun setUpViews() {
        setUpKeyboardDetection()
        action_bar.setOnBackPress {
            this@CustomiseNewActivity.onBackPressed()
        }
    }



    private fun setUpKeyboardDetection() {
        view_root.viewTreeObserver.addOnGlobalLayoutListener {
            val diff = view_root.rootView.height - view_root.height
            val keyboardOpen = diff > getDimensionPixelSize(R.dimen.soft_keyboar_detection_height)
            updateBottomButtonVisibility(!keyboardOpen)
        }
    }


    private fun updateBottomButtonVisibility(show: Boolean) {
        bottom_button_container.updateVisibility(show)
        v_shadow.updateVisibility(show)
    }
}