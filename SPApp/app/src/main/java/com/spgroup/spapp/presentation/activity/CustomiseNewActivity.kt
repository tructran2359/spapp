package com.spgroup.spapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.view.DropdownSelectionView
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.updateVisibility
import kotlinx.android.synthetic.main.activity_customise_new.*

class CustomiseNewActivity: BaseActivity() {

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            val intent = Intent(context, CustomiseNewActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_customise_new)

        setUpViews()
    }

    private fun setUpViews() {
        for (i in 1..5) {
            val view = DropdownSelectionView(this).apply {
                setTitle("Text $i")
                setData(listOf("1", "2", "3"))
            }
            ll_option_container.addView(view)
        }

        setUpKeyboardDetection()
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