package com.spgroup.spapp.presentation.adapter.item_decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class VerticalItemDecoration(val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State) {

        val pos = parent.getChildAdapterPosition(view)
        outRect.top = if (pos == 0) 0 else space
    }
}