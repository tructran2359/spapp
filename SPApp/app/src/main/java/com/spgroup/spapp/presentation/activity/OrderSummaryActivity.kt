package com.spgroup.spapp.presentation.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.isGone
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.*
import com.spgroup.spapp.presentation.adapter.PreferredTimeAdapter
import com.spgroup.spapp.presentation.fragment.LoadingDialog
import com.spgroup.spapp.presentation.view.*
import com.spgroup.spapp.presentation.viewmodel.ComplexSelectedService
import com.spgroup.spapp.presentation.viewmodel.ISelectedService
import com.spgroup.spapp.presentation.viewmodel.OrderSummaryViewModel
import com.spgroup.spapp.presentation.viewmodel.SelectedService
import com.spgroup.spapp.util.ConstUtils
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.doLogE
import com.spgroup.spapp.util.extension.*
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.layout_summary_estimated.*
import javax.inject.Inject

class OrderSummaryActivity : BaseActivity() {

    companion object {

        const val EXTRA_SERVICE_MAP = "EXTRA_SERVICE_MAP"
        const val EXTRA_PARTNER_DETAIL = "EXTRA_PARTNER_DETAIL"

        fun getLaunchIntent(
                context: Context,
                mapSelectedServices: HashMap<String, MutableList<ISelectedService>>,
                partnerDetails: PartnerDetails
        ): Intent {
            val intent = Intent(context, OrderSummaryActivity::class.java)
            intent.putExtra(EXTRA_SERVICE_MAP, mapSelectedServices)
            intent.putExtra(EXTRA_PARTNER_DETAIL, partnerDetails)
            return intent
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private lateinit var mAnimAppear: Animation
    private lateinit var mAnimDisappear: Animation
    private var mFirstInvalidView: ValidationInputView? = null
    private lateinit var mViewModel: OrderSummaryViewModel
    private lateinit var mListValidationField: List<ValidationInputView>
    private var mErrorViewIsShowing = false

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appInstance.appComponent.inject(this)

        setContentView(R.layout.activity_order_summary)

        mViewModel = obtainViewModel(OrderSummaryViewModel::class.java, vmFactory)
        subscribeUI()
        val mapSelectedServices = intent.getSerializableExtra(EXTRA_SERVICE_MAP) as HashMap<String, MutableList<ISelectedService>>
        val partnerDetails = intent.getSerializableExtra(EXTRA_PARTNER_DETAIL) as PartnerDetails
        mViewModel.initData(partnerDetails, mapSelectedServices)
        
        initAnimations()
        setUpViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_EDIT && resultCode == RESULT_OK) {
            data?.run {
                val customiseDisplayData = data.getSerializableExtra(CustomiseNewActivity.EXTRA_DISPLAY_DATA) as CustomiseDisplayData
                mViewModel.updateComplexSelectedServiceItem(customiseDisplayData)
            }
        } else if (requestCode == RC_NO_INTERNET_FOR_SUBMIT_REQUEST && resultCode == Activity.RESULT_OK) {
            mViewModel.submitRequest(createContactInfo())
        } else if (requestCode == RC_EMPTY_REQUEST && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent()
        val mapSelectedServices = mViewModel.getSelectedServicesMap()
        intent.putExtra(EXTRA_SERVICE_MAP, mapSelectedServices)
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    private fun subscribeUI() {

        mViewModel.run {

            mMapSelectedServices.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    doLogD("Dummy", it.toString())
                    addView(it)
                }
            })

            mDeletedServiceId.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    val view = ll_item_container.findViewWithTag<View>(createServiceTag(it))
                    ll_item_container.removeView(view)
                }
            })

            mDeletedCateId.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    val view = ll_item_container.findViewWithTag<View>(createCateTag(it))
                    ll_item_container.removeView(view)
                }
            })

            mEmpty.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    if (it) {
                        startActivityForResult(EmptyRequestActivity.getLaunchIntent(this@OrderSummaryActivity), RC_EMPTY_REQUEST)
                    }
                }
            })

            mTotalCount.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    btn_summary.setCount(it)
                    btn_summary.isEnabled = it != 0
                }
            })

            mEstPrice.observe(this@OrderSummaryActivity, Observer {
                it?.run {


                    val listDiscountData: MutableList<Triple<String, Float, Boolean>> = mutableListOf() //<Label, Value, IsDiscount>

                    //Percentage Discount
                    if (percentageDiscountValue != 0F) {
                        val label = getString(R.string.discount_with_desc, percentageDiscountLabel.toPercentageText())
                        listDiscountData.add(Triple(label, percentageDiscountValue, true))
                    }

                    //Amount Discount
                    if (amountDiscount != 0F) {
                        val label = if (amountDiscountLabel.isEmpty()) {
                            getString(R.string.discount)
                        } else {
                            getString(R.string.discount_with_desc, amountDiscountLabel)
                        }
                        listDiscountData.add(Triple(label, amountDiscount, true))
                    }

                    //Surcharge
                    if (surcharge != 0F) {
                        val label = getString(R.string.surcharge_with_price, minimumOrderAmount.formatPrice())
                        listDiscountData.add(Triple(label, surcharge, false))
                    }

                    //Add view:
                    ll_discount_container.removeAllViews()
                    listDiscountData.forEachIndexed {index, discountData ->
                        val view = PriceDetailView(this@OrderSummaryActivity)
                        view.setLabel(discountData.first)
                        view.setPrice(discountData.second, discountData.third)
                        val layoutParams = getLinearLayoutParams()
                        if (index != 0) {
                            layoutParams.topMargin = getDimensionPixelSize(R.dimen.summary_discount_line_spacing)
                        }
                        view.layoutParams = layoutParams
                        ll_discount_container.addView(view)
                    }
                    ll_discount_container.isGone = listDiscountData.isEmpty()

                    tv_total_value.text = finalPrice.formatPrice()
                    btn_summary.setEstPrice(finalPrice)

                    tv_checkbox_additional_charge_notice.isVisible = showCheckboxAdditionChargeNotice
                }
            })

            mUpdatedComplexService.observe(this@OrderSummaryActivity, Observer {
                it?.let {
                    val serviceTag = createServiceTag(it.service.id)
                    val view = ll_item_container.findViewWithTag<SummaryItemViewCombo>(serviceTag)
                    if (view != null) {
                        setUpComplexData(view, it)
                    }
                }
            })

            mIsLoading.observe(this@OrderSummaryActivity, Observer {
                it?.let { isLoading ->
                    showLoadingDialog(isLoading)
                }
            })

            error.observe(this@OrderSummaryActivity, Observer {
                it?.let { throwable ->
                    doLogE("SubmitRequest", "Error: ${throwable.toString()} with message: ${throwable.message}")

                    // Start Error Activity without finishing this screen so can come back when press BACK in Error
                    startActivity(ApiErrorActivity.getLaunchIntent(this@OrderSummaryActivity))
                }
            })

            mRequestAck.observe(this@OrderSummaryActivity, Observer {
                it?.let { requestAck ->
                    doLogD("SubmitRequest", "Ack: ${requestAck}")
                    val intent = AcknowledgementActivity.getLaunchIntent(this@OrderSummaryActivity, requestAck)
                    startActivity(intent)
                    finish()
                }
            })
        }
    }

    private fun initAnimations() {
        mAnimAppear = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)
        mAnimDisappear = AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)

        mAnimAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                rl_error_cointainer.visibility = View.VISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })

        mAnimDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                rl_error_cointainer.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    private fun setUpViews() {

        action_bar.setTitle(R.string.summary)
        val partnerName = mViewModel.getPartnerName()

        tv_name.text = partnerName
        tv_go_back_instruction.text = getString(R.string.summary_go_back_instruction, partnerName)

        tv_go_back.setOnClickListener {
            onBackPressed()
        }

        mListValidationField = listOf(
                validation_postal_code,
                validation_address,
                validation_contact_no,
                validation_email,
                validation_name)

        validation_name.setValidation { name: String -> name.length > 1 }

        validation_email.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        validation_email.setValidation { emailAddress: String -> emailAddress.isValidEmail() }

        validation_contact_no.setInputType(InputType.TYPE_CLASS_NUMBER)
        validation_contact_no.setValidation { phone: String -> phone.length == 8 && phone.isNumberOnly() }

        validation_postal_code.setInputType(InputType.TYPE_CLASS_NUMBER)
        validation_postal_code.setValidation { postalCode: String -> postalCode.length == 6 && postalCode.isNumberOnly() }

        setUpSubmitButton()

        action_bar.setOnBackPress {
            onBackPressed()
        }

        val list = ConstUtils.LIST_PREFERRED_TIME.toMutableList()
        val adapter = PreferredTimeAdapter(this, R.layout.layout_preferred_time, list)

        spinner_preferred_time.adapter = adapter

        val hintResId = getSummaryHint(mViewModel.getTopLevelCategoryId())
        if (hintResId != -1) {
            et_notes.setHint(hintResId)
        }

        tv_disclaimer.setUpClickableUnderlineSpan(R.string.order_summary_disclaimer_with_format, R.string.tnc) {
            startActivity(PageActivity.getLaunchIntent(this@OrderSummaryActivity, PageActivity.TYPE_TNC))
        }

        rl_error_cointainer.setOnClickListener {
            mFirstInvalidView?.let {
                scroll_content.scrollTo(0, 0)
                val location = IntArray(2)
                it.getLocationOnScreen(location)
//                doLogD("Scroll", "Pos on screen: ${location[0]} , ${location[1]}")
                val position = location[1] - getDimensionPixelSize(R.dimen.action_bar_height) - getDimensionPixelSize(R.dimen.common_vert_large)
                scroll_content.scrollTo(0, position)
                it.requestFocus()
                showErrorView(false)
            }
        }

        setUpKeyboardDetection()
        fillSavedContactInfoIfNeed()
    }

    private fun fillSavedContactInfoIfNeed() {
        val remembered = mViewModel.isRemembered()
        cb_remember.isChecked = remembered
        if (remembered) {
            val contactInfo = mViewModel.getSavedContactInfo()
            contactInfo?.run {
                validation_name.setText(name)
                validation_email.setText(email)
                validation_contact_no.setText(contactNo)

                if (address.size == 2) {
                    validation_address.setText(address[0])
                    et_address_2.setText(address[1])
                } else if (!address.isEmpty()) {
                    validation_address.setText(address[0])
                }

                var selectedIndex = 0
                prefContactTime?.run {
                    ConstUtils.LIST_PREFERRED_TIME.forEachIndexed { index, time ->
                        if (time == this) {
                            selectedIndex = index
                            return@forEachIndexed
                        }
                    }
                }
                spinner_preferred_time.setSelection(selectedIndex)

                validation_postal_code.setText(postalCode)
                et_notes.setText(notes)
            }
        }
    }

    private fun setUpSubmitButton() {
        with(btn_summary) {
            isEnabled = true
            setText(getString(R.string.submit_request))
            setCount(1)
            setEstPrice(0.01f)
            setOnClickListener {
                val invalidCount = calculateInvalidCount()

                if (invalidCount == 0) {
                    // All fields are valid
                    rl_error_cointainer.visibility = View.GONE
                    val contactInfo = createContactInfo()

                    // Check to save contact info
                    if (cb_remember.isChecked) {
                        // Remember Me Enabled -> Save to Share Pref
                        mViewModel.saveContactInfo(contactInfo)
                    } else {
                        mViewModel.removeSavedContactInfo()
                    }

                    if (isOnline()) {
                        mViewModel.submitRequest(contactInfo)
                    } else {
                        startActivityForResult(
                                NoInternetActivity.getLaunchIntent(this@OrderSummaryActivity, null, NO_REQUEST_CODE),
                                RC_NO_INTERNET_FOR_SUBMIT_REQUEST)

                    }
                } else {
                    updateInvalidView(invalidCount)

                    mListValidationField.forEach { validationView ->
                        validationView.setCustomOnFocusChangeListener { focus ->
                            if (!focus) {
                                validationView.validate()
                                updateInvalidView(calculateInvalidCount())
                            }
                        }
                    }
                    showErrorView(true)
                }
            }
        }
    }

    private fun updateInvalidView(invalidCount: Int) {
        var errorMsg = if (invalidCount == 1) {
            this@OrderSummaryActivity.getString(R.string.error_detected_1)
        } else {
            this@OrderSummaryActivity.getString(R.string.error_detected_format, invalidCount)
        }
        tv_error_counter.setText(errorMsg)
    }

    private fun calculateInvalidCount(): Int {
        var invalidCount = 0
        mFirstInvalidView = null

        mListValidationField.forEach {
            val valid = it.validate()
            if (!valid) {
                invalidCount++
                mFirstInvalidView = it
            }
        }
        return invalidCount
    }

    private fun setUpKeyboardDetection() {
        view_root.viewTreeObserver.addOnGlobalLayoutListener {
            val diff = view_root.rootView.height - view_root.height
            val keyboardOpen = diff > getDimensionPixelSize(R.dimen.soft_keyboar_detection_height)
            updateBottomButtonVisibility(!keyboardOpen)
        }
    }

    private fun updateBottomButtonVisibility(show: Boolean) {
        rl_summary_button_container.isGone = !show
        v_shadow.isGone = !show
        if (show) {
            rl_error_cointainer.isGone = !mErrorViewIsShowing
        } else {
            rl_error_cointainer.isGone = true
        }
    }

    private fun addView(mapSelectedServices: HashMap<String, MutableList<ISelectedService>>) {
        ll_item_container.removeAllViews()

        val inflater = LayoutInflater.from(this)

        for ((cateId, listSelectedServce) in mapSelectedServices) {
            if (listSelectedServce.isNotEmpty()) {
                val cateName = mViewModel.getCateName(cateId) ?: ""
                addHeader(inflater, cateName, cateId)

                for (selectedService in listSelectedServce) {
                    when (selectedService) {

                        is SelectedService -> {
                            if (selectedService.service is MultiplierService) {
                                addItemCounter(cateId, selectedService)
                            } else if (selectedService.service is CheckboxService) {
                                addItemCheckbox(cateId, selectedService)
                            }
                        }

                        is ComplexSelectedService -> addItemCombo(cateId, selectedService)
                    }
                }
            }
        }
    }

    private fun addHeader(inflater: LayoutInflater, cateName: String, cateId: String) {
        val tag = createCateTag(cateId)

        val tvHeader = inflater.inflate(R.layout.item_summary_header, null, false) as TextView
        val layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_large)
            leftMargin = getDimensionPixelSize(R.dimen.common_horz_large)
            rightMargin = getDimensionPixelSize(R.dimen.common_horz_large)
        }

        tvHeader.run {
            setText(cateName)
            setLayoutParams(layoutParams)
            setTag(tag)
        }
        ll_item_container.addView(tvHeader)
    }

    private fun addItemCounter(cateId: String, item: SelectedService) {
        val service = item.service as MultiplierService
        val tag = createServiceTag(service.id)
        val view = SummaryItemView(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(service.label)
            setPrice(service.price * item.count)
            initData(service.min, service.max, item.count)
            setTag(tag)
            setOnCountChangedListener(object : CounterView.OnCountChangeListener {
                override fun onPlus() {
                    val count = getCount() + 1
                    setCount(count)
                    setPrice(service.price * count)
                    mViewModel.updateNormalSelectedServiceItem(
                            service,
                            count,
                            cateId
                    )
                }

                override fun onMinus() {
                    val count = getCount() - 1
                    setCount(count)
                    setPrice(service.price * count)
                    mViewModel.updateNormalSelectedServiceItem(
                            service,
                            count,
                            cateId
                    )
                }
            })
            setOnDeleteListener {
                mViewModel.deleteService(cateId, service.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCheckbox(cateId: String, item: SelectedService) {
        val service = item.service as CheckboxService
        val tag = createServiceTag(service.id)
        val view = SummaryItemViewEstimated(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }

        view.run {
            setLayoutParams(layoutParams)
            setName(service.label)
            setDescription(getString(R.string.order_summary_checkbox_item_desc))
            setTag(tag)
            setOnDeleteListener {
                mViewModel.deleteService(cateId, service.id)
            }
        }

        ll_item_container.addView(view)
    }

    private fun addItemCombo(cateId: String, item: ComplexSelectedService) {
        val tag = createServiceTag(item.service.getServiceId())
        val service = item.service
        val subCateName = mViewModel.getSubCateName(cateId, item.getId())
        val view = SummaryItemViewCombo(this)
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        layoutParams.run {
            topMargin = getDimensionPixelSize(R.dimen.common_vert_small)
            bottomMargin = getDimensionPixelSize(R.dimen.common_vert_medium)
        }
        view.layoutParams = layoutParams
        view.tag = tag
        view.setOnEditClickListener {
            val displayData = CustomiseDisplayData(
                    categoryId = cateId,
                    serviceItem = service,
                    mapSelectedOption = item.selectedCustomisation ?: HashMap(),
                    specialInstruction = item.specialInstruction,
                    subCateName = subCateName,
                    topLevelCateId = mViewModel.getTopLevelCategoryId()
            )
            val intent = CustomiseNewActivity.getLaunchIntent(
                    context = this@OrderSummaryActivity,
                    displayData = displayData,
                    isEdit = true)
            startActivityForResult(intent, RC_EDIT)

        }
        view.setOnDeleteListener {
            mViewModel.deleteService(cateId, item.service.id)
        }

        setUpComplexData(view, item)


        ll_item_container.addView(view)
    }

    private fun setUpComplexData(
            view: SummaryItemViewCombo,
            item: ComplexSelectedService) {

        val service = item.service

        // Append service name with Sub Category name at the beginning
        val subCateName = mViewModel.getSubCateNameByServiceId(item.getId())
        val serviceName = if (subCateName == null || subCateName.isEmpty()) {
            service.label
        } else {
            subCateName + " - " + service.label
        }
        view.setServiceName(serviceName)
        view.setServiceDescription(service.serviceDescription)
        view.setInstruction(item.specialInstruction ?: "")
        view.clearOption()

        item.selectedCustomisation?.let { hashMap ->
            for ((index, selectedPos) in hashMap) {
                val customisation = item.service.customisations[index]
                when (customisation) {
                    is BooleanCustomisation -> {
                        if (selectedPos == 0) {
                            //Yes
                            view.addOption("Yes", customisation.price)
                        } else if (selectedPos == 1) {
                            //No
                            view.addOption("No", 0f)
                        }
                    }

                    is MatrixCustomisation -> {
                        val matrixOptionItem = customisation.matrixOptions[selectedPos]
                        view.addOption(matrixOptionItem.value.toString(), matrixOptionItem.price)
                    }

                    is DropdownCustomisation -> {
                        val dropdownOptionItem = customisation.dropdownOptions[selectedPos]
                        view.addOption(dropdownOptionItem.label, dropdownOptionItem.price)
                    }

                    is NumberCustomisation -> {
                        val count = selectedPos + customisation.min
                        view.addOption((count).toString(), count * customisation.price)
                    }
                }
            }
        }
    }

    private fun showErrorView(show: Boolean) {
        if (show && rl_error_cointainer.visibility != View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimAppear)
            mErrorViewIsShowing = true
        } else if (!show && rl_error_cointainer.visibility == View.VISIBLE) {
            rl_error_cointainer.startAnimation(mAnimDisappear)
            mErrorViewIsShowing = false
        }
    }

    private fun createCateTag(cateId: String) = "Cate" + cateId

    private fun createServiceTag(serviceId: Int) = "ServiceID${serviceId}"

    private fun createContactInfo(): ContactInfo {
        val name = validation_name.getInputedText()
        val email = validation_email.getInputedText()
        val contactNo = validation_contact_no.getInputedText()
        val address1 = validation_address.getInputedText()
        val address2 = et_address_2.text?.toString() ?: ""
        val address = listOf(address1, address2)
        val selectedPreferredTime = spinner_preferred_time.selectedItemPosition
        val preferredTime = if (selectedPreferredTime == 0) "" else ConstUtils.LIST_PREFERRED_TIME[selectedPreferredTime]
        val postalCode = validation_postal_code.getInputedText()
        val notes = et_notes.text?.toString() ?: ""

        return ContactInfo(name, email, contactNo, address, preferredTime, postalCode, notes)
    }

    private fun showLoadingDialog(show: Boolean) {
        doLogD("Loading", "Show: $show")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prevDialog = supportFragmentManager.findFragmentByTag(ConstUtils.TAG_DIALOG)
        if (show) {
            if (prevDialog != null) {
                fragmentTransaction.remove(prevDialog)
            }

            val newDialog = LoadingDialog()
            newDialog.show(fragmentTransaction, ConstUtils.TAG_DIALOG)

            doLogD("Loading", "Show done")
        } else {
            if (prevDialog != null) {
                (prevDialog as DialogFragment).dismiss()
                doLogD("Loading", "Hide done")
            }
        }
    }
}
