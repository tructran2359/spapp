package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.presentation.fragment.UnsavedDataDialog
import com.spgroup.spapp.presentation.view.DropdownSelectionView
import com.spgroup.spapp.presentation.viewmodel.CustomiseData
import com.spgroup.spapp.presentation.viewmodel.CustomiseNewViewModel
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_customise_new.*
import java.io.Serializable
import javax.inject.Inject

class CustomiseNewActivity: BaseActivity() {

    companion object {
        const val EXTRA_DISPLAY_DATA = "EXTRA_DISPLAY_DATA"
        const val EXTRA_IS_EDIT = "EXTRA_IS_EDIT"

        fun getLaunchIntent(
                context: Context,
                displayData: CustomiseDisplayData,
                isEdit: Boolean
        ): Intent {
            val intent = Intent(context, CustomiseNewActivity::class.java)
            intent.putExtra(EXTRA_DISPLAY_DATA, displayData)
            intent.putExtra(EXTRA_IS_EDIT, isEdit)
            return intent
        }
    }

    private lateinit var mViewModel: CustomiseNewViewModel
    private val listDropdownViews = mutableListOf<DropdownSelectionView>()
    private var mIsEdit = false

    @Inject lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_customise_new)

        mIsEdit = intent.getBooleanExtra(EXTRA_IS_EDIT, false)

        mViewModel = obtainViewModel(CustomiseNewViewModel::class.java, vmFactory)
        subcribesUI()


        mViewModel.initData(
                mIsEdit,
                intent.getSerializableExtra(EXTRA_DISPLAY_DATA) as CustomiseDisplayData
        )

        setUpViews()
    }

    private fun subcribesUI() {
        mViewModel.run {

            serviceName.observe(this@CustomiseNewActivity, Observer {
                it?.run {
                    tv_name.text = this
                }
            })

            serviceDescription.observe(this@CustomiseNewActivity, Observer {
                it?.run {
                    tv_description.text = it
                    tv_description.isGoneWithText(it)
                }
            })

            listData.observe(this@CustomiseNewActivity, Observer {
                it?.run {
                    setUpData(this)
                }
            })

            totalPriceData.observe(this@CustomiseNewActivity, Observer {
                it?.run {
                    tv_est_price.text = this.formatPrice()
                }
            })

            // If it is edit function from Order Summary, need to observe data change
            if (mIsEdit) {
                isDataChanged.observe(this@CustomiseNewActivity, Observer {
                    it?.run {
                        tv_add_to_request.setText(if (this) R.string.update_and_view_summary else R.string.back_to_view_summary)
                    }
                })
            } else {
                // If it is add service or update service from Partner Detail, need to check add new or update
                // to update text of Bottom button
                isUpdateSelectedService.observe(this@CustomiseNewActivity, Observer {
                    it?.let { isUpdate ->
                        val strResId = if (isUpdate) {
                            R.string.update_request
                        } else {
                            R.string.add_request
                        }
                        tv_add_to_request.setText(strResId)
                    }
                })

            }
        }
    }

    override fun onBackPressed() {
        if (!mIsEdit) {
            super.onBackPressed()
        } else {
            mViewModel.isDataChanged.value?.let { changed ->
                if (changed) {
                    showConfirmDialog()
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    private fun setUpData(listData: List<CustomiseData>) {
        listData.forEachIndexed { index, data ->
            val view = DropdownSelectionView(this@CustomiseNewActivity).apply {
                setTitle(data.title)
                setData(data.options.map { it.label })
                setOnItemSelectedListener { selectedPos ->
                    mViewModel.notifyDataChanged(index, selectedPos)
                }
                val selectedPos = mViewModel.getSelectedOptionMap()[index] ?: 0
                setSelectedPosition(selectedPos)
            }

            ll_option_container.addView(view)
            listDropdownViews.add(view)
        }
    }

    private fun setUpViews() {
//        setUpKeyboardDetection()
        if (mIsEdit) {
            action_bar.setTitle(R.string.edit)
            tv_add_to_request.setText(R.string.back_to_view_summary)
            et_instruction.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    var text = editable?.toString() ?: ""
                    mViewModel.notifyInstructionChanged(text)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }

        action_bar.setOnBackPress {
            this@CustomiseNewActivity.onBackPressed()
        }
        val hintResId = getCustomiseHint(mViewModel.getTopLevelCateId())
        if (hintResId != -1) {
            et_instruction.setHint(hintResId)
        }
        et_instruction.setText(mViewModel.getDisplayData().specialInstruction)
        tv_add_to_request.setOnClickListener {
            et_instruction.hideKeyboard()
            val intent = Intent()

            val newDisplayData = mViewModel.getDisplayData().apply {
                specialInstruction = et_instruction.text?.toString() ?: ""
                estPrice = mViewModel.totalPriceData.value ?: 0f
            }
            intent.putExtra(EXTRA_DISPLAY_DATA, newDisplayData)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        ll_content_container.setOnClickListener {
            it.hideKeyboard()
        }
    }



//    private fun setUpKeyboardDetection() {
//        view_root.viewTreeObserver.addOnGlobalLayoutListener {
//            val diff = view_root.rootView.height - view_root.height
//            val keyboardOpen = diff > getDimensionPixelSize(R.dimen.soft_keyboar_detection_height)
//            if (keyboardOpen) {
//                updateBottomButtonVisibility(false)
//            } else {
//                updateBottomButtonVisibility(true)
//            }
//        }
//    }


//    private fun updateBottomButtonVisibility(show: Boolean) {
//        bottom_button_container.updateVisibility(show)
//        v_shadow.updateVisibility(show)
//    }



    private fun showConfirmDialog() {
        val newDialog = UnsavedDataDialog()
        with(newDialog) {
            setActions(
                    { super.onBackPressed() },
                    null
            )
        }
        showDialog(newDialog)
    }
}

data class CustomiseDisplayData (
        var categoryId: String,
        var serviceItem: ComplexCustomisationService,
        var mapSelectedOption: HashMap<Int, Int>,
        var specialInstruction: String?,
        var estPrice: Float = 0f,
        var subCateName: String,
        val topLevelCateId: String
): Serializable {
    fun isSameSelectedOptionData(newObj: CustomiseDisplayData): Boolean {
        if (mapSelectedOption.size != newObj.mapSelectedOption.size) return false
        for ((key, _) in mapSelectedOption) {
            if (mapSelectedOption[key] != newObj.mapSelectedOption[key]) return false
        }
        if (specialInstruction != newObj.specialInstruction) return false
        return true
    }

    fun clone(): CustomiseDisplayData {
        val hashMap = HashMap<Int, Int>()
        for ((key, value) in mapSelectedOption) {
            hashMap[key] = value
        }
        return CustomiseDisplayData(categoryId, serviceItem, hashMap, specialInstruction, estPrice, subCateName, topLevelCateId)
    }
}