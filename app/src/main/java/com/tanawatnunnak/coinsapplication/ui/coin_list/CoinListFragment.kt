package com.tanawatnunnak.coinsapplication.ui.coin_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tanawatnunnak.coinsapplication.databinding.FragmentCoinListBinding
import com.tanawatnunnak.coinsapplication.extention.RecyclerViewLoadMoreScroll
import com.tanawatnunnak.coinsapplication.extention.setVisibility
import com.tanawatnunnak.coinsapplication.extention.toast
import com.tanawatnunnak.coinsapplication.ui.base.BaseFragment
import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinAdapter
import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinBaseItem
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinListFragment : BaseFragment<FragmentCoinListBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    RecyclerViewLoadMoreScroll.OnLoadMoreListener {

    private val coinViewModel: CoinListViewModel by viewModel()
    private val coinsAdapter: CoinAdapter by inject()

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var mLayoutManager: LinearLayoutManager? = null
    private var itemListState: Parcelable? = null
    private var handler: Handler? = null
    private var searchWord: String? = null

    private val runnable = Runnable {
        val searchWord = binding?.coinListSearchEdt?.text.toString()
        this.searchWord = searchWord
        coinViewModel.searchWord(searchWord)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            clearHandle()
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            handler?.postDelayed(runnable, SEARCH_DELAY)
        }

    }

    override fun getViewBinding() = FragmentCoinListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            this.searchWord = savedInstanceState.getString(SEARCH_WORD_KEY)
            this.itemListState = savedInstanceState.getParcelable(ITEM_LIST_STATE_KEY)
        } else {
            coinViewModel.fetchCoins()
        }
    }

    override fun onResume() {
        super.onResume()
        if (itemListState != null) {
            mLayoutManager?.onRestoreInstanceState(itemListState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_WORD_KEY, searchWord)
        outState.putParcelable(ITEM_LIST_STATE_KEY, mLayoutManager?.onSaveInstanceState())
    }

    override fun onDestroyView() {
        clearHandle()
        super.onDestroyView()
    }

    override fun initView() {
        handler = Handler(Looper.getMainLooper())
        binding?.coinListRefresh?.setOnRefreshListener(this)
        binding?.coinListSearchEdt?.addTextChangedListener(textWatcher)

        if (searchWord != null) {
            binding?.coinListSearchEdt?.setText(searchWord)
        }

        initRecyclerView()
    }

    override fun initObservable() {
        coinViewModel.state.observe(this, ::updateStateUI)
    }

    override fun onRefresh() {
        coinViewModel.refresh()
    }

    override fun onLoadMore() {
        coinsAdapter.addLoading()
        coinViewModel.loadMore()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView() {
        scrollListener = RecyclerViewLoadMoreScroll()
        scrollListener.setOnLoadMoreListener(this)
        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.coinListRcv?.apply {
            layoutManager = mLayoutManager
            adapter = coinsAdapter
            addOnScrollListener(scrollListener)
            setOnTouchListener { _, _ ->
                hideKeyboard()
                binding?.coinListSearchEdt?.clearFocus()
                false
            }
        }
    }

    private fun updateStateUI(state: CoinListState) {
        val isRefreshing = binding?.coinListRefresh?.isRefreshing ?: false
        if (isRefreshing) {
            binding?.coinListRefresh?.isRefreshing = false
        }

        binding?.coinListLoading?.setVisibility(state.isLoading)
        binding?.coinListSearchEmpty?.setVisibility(state.coinList.isEmpty() && !state.isLoading)

        if (state.errorMessage != "") {
            toast(state.errorMessage)
        }

        setAdapter(
            coinList = state.coinList,
            isFirstPage = state.currentPage == FIRST_PAGE,
            isLastPage = state.isLastPage
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(
        coinList: ArrayList<CoinBaseItem>,
        isFirstPage: Boolean,
        isLastPage: Boolean
    ) {
        setLoadMore(isLastPage)
        coinsAdapter.setItemList(ArrayList(coinList))
        if (isFirstPage) {
            coinsAdapter.notifyDataSetChanged()
        } else {
            val lastPosition = coinsAdapter.itemCount
            coinsAdapter.notifyItemChanged(lastPosition)
        }
    }

    private fun setLoadMore(isLastPage: Boolean) {
        if (scrollListener.getLoaded()) {
            coinsAdapter.deleteLoading()
            scrollListener.setLoaded()
        }
        scrollListener.setLastPage(isLastPage)
    }

    private fun clearHandle() {
        handler?.removeCallbacks(runnable)
    }

    companion object {
        private const val SEARCH_DELAY: Long = 400L
        private const val SEARCH_WORD_KEY = "search word"
        private const val ITEM_LIST_STATE_KEY = "recyclerview state"
        const val FIRST_PAGE = 0
        fun newInstance() = CoinListFragment()
    }

}