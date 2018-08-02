package com.spgroup.spapp.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize
import com.spgroup.spapp.util.extension.inflate
import kotlinx.android.synthetic.main.layout_top_level_promotion.view.*

class HomePromotionAdapter(
        val context: Context,
        val screenWidth: Int
): RecyclerView.Adapter<HomePromotionAdapter.TopCatePromotionVH>() {

    ///////////////////////////////////////////////////////////////////////////
    // Override
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCatePromotionVH {
        val view = parent.inflate(R.layout.layout_top_level_promotion, false)
        with(view) {
            iv_image.setRatio(16, 9, true)
            val layoutParams = view_root.layoutParams
            val padding = context.getDimensionPixelSize(R.dimen.common_horz_large)
            val shadow = context.getDimensionPixelSize(R.dimen.card_view_shadow_width)
            layoutParams.width = screenWidth - (2 * padding) + (2 * shadow)
            view_root.layoutParams = layoutParams
        }
        return TopCatePromotionVH(view)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(viewHolder: TopCatePromotionVH, position: Int) {
    }


    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    class TopCatePromotionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}