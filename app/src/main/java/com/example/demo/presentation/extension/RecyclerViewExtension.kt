package com.example.demo.presentation.extension

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addVerticalSpaceDecoration(@DimenRes bottomSpaceRes: Int) {
    val bottomSpace = context.resources.getDimensionPixelSize(bottomSpaceRes)
    addItemDecoration(
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position != 0 && position != parent.adapter?.itemCount) {
                    outRect.top = bottomSpace
                }
            }
        }
    )
}

fun RecyclerView.addPaginationScrollListener(
    layoutManager: LinearLayoutManager,
    itemsToLoad: Int,
    onLoadMore: () -> Unit
) {
    addOnScrollListener(PagingScrollListener(layoutManager, itemsToLoad, onLoadMore))
}

private class PagingScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val itemsToLoad: Int,
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (dy != 0 && totalItemCount <= (lastVisibleItem + itemsToLoad)) {
            recyclerView.post(onLoadMore)
        }
    }
}