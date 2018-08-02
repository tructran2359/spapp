package com.spgroup.spapp.presentation.adapter.item_decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.spgroup.spapp.R
import com.spgroup.spapp.util.extension.getDimensionPixelSize

class HomeMerchantItemtDecoration(val rowCount: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State) {

        val itemCount = parent.adapter?.itemCount ?: 0
        if (itemCount != 0) {
            val position = parent.getChildAdapterPosition(view)

            var colCount = itemCount / rowCount
            if (itemCount % rowCount != 0) colCount++

            val rowPos = position % rowCount
            val colPos = position / rowCount

            val isTopEdge = rowPos == 0
            val isLeftEdge = colPos == 0
            val isRightEdge = colPos == colCount - 1

            val spaceNormal = parent.getDimensionPixelSize(R.dimen.common_vert_medium_sub)
            val spaceEdge = parent.getDimensionPixelSize(R.dimen.common_horz_large)

            outRect.left = if (isLeftEdge) spaceEdge else spaceNormal
            outRect.right = if (isRightEdge) spaceEdge else 0
            outRect.top = if (isTopEdge) 0 else (spaceNormal * (rowPos + 1) / rowCount)
            outRect.bottom = 0

        }
    }
}