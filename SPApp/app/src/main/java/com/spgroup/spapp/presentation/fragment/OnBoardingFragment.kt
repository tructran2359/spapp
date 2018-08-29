package com.spgroup.spapp.presentation.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.presentation.viewmodel.OnBoardingViewModel
import com.spgroup.spapp.presentation.viewmodel.ViewModelFactory
import com.spgroup.spapp.util.doLogD
import com.spgroup.spapp.util.extension.inflate
import com.spgroup.spapp.util.extension.obtainViewModelOfActivity
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoardingFragment: BaseFragment() {

    companion object {
        const val EXTRA_POSITION = "EXTRA_POSITION"
        fun newInstance(position: Int): OnBoardingFragment {
            val fragment = OnBoardingFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mPosition = -1
    private lateinit var mViewModel: OnBoardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = arguments?.getInt(EXTRA_POSITION, -1) ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_on_boarding, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = obtainViewModelOfActivity(OnBoardingViewModel::class.java, ViewModelFactory.getInstance())

        when(mPosition) {
            0 -> {
                tv_content.setText(R.string.onboarding_text_0)
            }

            1 -> {
                tv_content.setText(R.string.onboarding_text_1)
            }

            2 -> {
                tv_content.setText(R.string.onboarding_text_2)
            }

            3 -> {
                tv_content.setText(R.string.onboarding_text_3)
            }
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        mViewModel.mPageAnimationState.observe(this, Observer {
            it?.let { pair ->
                doLogD("Page", "Page: ${mPosition} UI Pair: $pair")
                val position = pair.first
                val completed = pair.second
                if (position == mPosition) {
                    if (!completed) {
                        iv_gif.setImageResource(getImageResource())
                        mViewModel.setAnimationCompleted(mPosition)
                    }
                }
            }
        })
    }

    private fun getImageResource() = when(mPosition) {
        0 -> R.drawable.onboarding_ani_scene01

        1 -> R.drawable.onboarding_ani_scene01

        2 -> R.drawable.onboarding_ani_scene01

        3 -> R.drawable.onboarding_ani_scene01

        else -> -1
    }
}