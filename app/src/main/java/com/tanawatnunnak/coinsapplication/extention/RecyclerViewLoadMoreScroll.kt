package com.tanawatnunnak.coinsapplication.extention

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll : RecyclerView.OnScrollListener() {

    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var isLoading: Boolean = false
    private var isLastPage = false

    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded(): Boolean {
        return isLoading
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    fun setLastPage(lastPage: Boolean) {
        isLastPage = lastPage
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return
        var visibleItemCount = 0
        var totalItemCount = 0

        recyclerView.layoutManager?.childCount?.let { visibleItemCount = it }
        recyclerView.layoutManager?.itemCount?.let { totalItemCount = it }

        val firstVisibleItemPosition = getFirstVisibleItemPosition(recyclerView)
        val lastVisibleItemPosition = getLastVisibleItemPosition(recyclerView)

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && lastVisibleItemPosition == totalItemCount
            && firstVisibleItemPosition >= 0
            && !isLoading
            && !isLastPage
        ) {
            isLoading = true
            mOnLoadMoreListener.onLoadMore()
        }
    }

    private fun getFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        return recyclerView.let { (it.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() }
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        return recyclerView.let { (it.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1 }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}