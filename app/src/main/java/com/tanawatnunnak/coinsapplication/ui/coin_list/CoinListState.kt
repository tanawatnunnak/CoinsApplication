package com.tanawatnunnak.coinsapplication.ui.coin_list

import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinBaseItem

data class CoinListState(
    var isLoading: Boolean,
    var coinList: ArrayList<CoinBaseItem> = ArrayList(),
    var errorMessage: String = "",
    var limitPage: Int = 10,
    var currentPage: Int = 0,
    var isRefreshing: Boolean = false,
    var searchWord: String = "",
    var isLastPage: Boolean = false
)