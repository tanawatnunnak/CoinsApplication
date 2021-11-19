package com.tanawatnunnak.coinsapplication.ui.coin_list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinListFragment : BaseFragment<FragmentCoinListBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    RecyclerViewLoadMoreScroll.OnLoadMoreListener {

    private val coinViewModel: CoinListViewModel by viewModel()
    private val coinsAdapter: CoinAdapter by inject()

    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var handler: Handler? = null

    private val runnable = Runnable {
        val searchWord = binding?.coinListSearchEdt?.text.toString()
        if (searchWord.isEmpty()) {
            coinViewModel.refresh()
        } else {
            coinViewModel.search(searchWord)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            clearHandle()
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            handler?.postDelayed(runnable, SEARCH_DELAY)
        }

    }

    override fun getViewBinding() = FragmentCoinListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coinViewModel.getCoins()
    }

    override fun onDestroy() {
        clearHandle()
        super.onDestroy()
    }

    override fun initView() {
        handler = Handler(Looper.getMainLooper())
        binding?.coinListRefresh?.setOnRefreshListener(this)
        scrollListener = RecyclerViewLoadMoreScroll()
        scrollListener.setOnLoadMoreListener(this)
        binding?.coinListRcv?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coinsAdapter
            addOnScrollListener(scrollListener)
        }

        binding?.coinListSearchEdt?.addTextChangedListener(textWatcher)
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

    private fun updateStateUI(state: CoinListState) {
        val isRefreshing = binding?.coinListRefresh?.isRefreshing ?: false
        if (isRefreshing) {
            binding?.coinListRefresh?.isRefreshing = false
        }

        binding?.coinListLoading?.setVisibility(state.isLoading)

        if (state.errorMessage != "") {
            toast(state.errorMessage)
        }

        if(state.currentPage > 0){
            coinsAdapter.deleteLoading()
            scrollListener.setLoaded()
        }
        coinsAdapter.setItemList(ArrayList(state.coinList))

    }

    private fun clearHandle() {
        handler?.removeCallbacks(runnable)
    }

    companion object {
        private val SEARCH_DELAY: Long = 500L
        fun newInstance() = CoinListFragment()
    }

}