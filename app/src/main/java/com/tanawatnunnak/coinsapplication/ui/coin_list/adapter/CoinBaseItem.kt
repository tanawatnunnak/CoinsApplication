package com.tanawatnunnak.coinsapplication.ui.coin_list.adapter

sealed class CoinBaseItem(open var id: Int, var type: Int) {
    data class LeftCoinItem(override var id: Int, var imageUrl: String, var name: String, var description: String):CoinBaseItem(id, CoinType.LEFT_TYPE)
    data class RightCoinItem(override var id: Int, var imageUrl: String, var name: String):CoinBaseItem(id, CoinType.RIGHT_TYPE)
    data class LoadingItem(override var id: Int):CoinBaseItem(id, CoinType.LOADING_TYPE)
}