package com.spgroup.spapp.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.domain.model.TopLevelPromotion
import com.spgroup.spapp.presentation.adapter.viewholder.HomePromotionVH
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_top_level_promotion.view.*

class HomePromotionAdapter(
        private val screenWidth: Int,
        private var mListener: OnPromotionClickListener
): RecyclerView.Adapter<HomePromotionVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Property
    ///////////////////////////////////////////////////////////////////////////

    private val mData = mutableListOf<TopLevelPromotion>()

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePromotionVH {
        val view = parent.inflate(R.layout.layout_top_level_promotion, false)
        with(view) {
            iv_image.setRatio(16, 9, true)
            val layoutParams = view_root.layoutParams
            val padding = context.getDimensionPixelSize(R.dimen.common_horz_large)
            val shadow = context.getDimensionPixelSize(R.dimen.card_view_shadow_width)
            layoutParams.width = screenWidth - (2 * padding) + (2 * shadow)
            view_root.layoutParams = layoutParams
        }
        return HomePromotionVH(view, mListener)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(viewHolder: HomePromotionVH, position: Int) {
        viewHolder.bind(mData[position])
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other
    ///////////////////////////////////////////////////////////////////////////

    fun setOnPromotionClickListener(listener: OnPromotionClickListener) {
        mListener = listener
    }

    fun setData(data: List<TopLevelPromotion>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    interface OnPromotionClickListener {
        fun onPromotionClick(promotion: TopLevelPromotion, position: Int)
    }

}