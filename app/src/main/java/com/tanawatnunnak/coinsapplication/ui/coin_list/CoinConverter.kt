package com.tanawatnunnak.coinsapplication.ui.coin_list

import com.tanawatnunnak.coinsapplication.data.model.Coin
import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinBaseItem

class CoinConverter {
    fun convertToCoinItem(coinList: List<Coin>?): ArrayList<CoinBaseItem> {
        val coinBaseItem = ArrayList<CoinBaseItem>()
        coinList?.forEachIndexed { index, coin ->
            val positionCondition = (index + 1) % 5
            if (positionCondition == 0) {
                val rightCoinItem = CoinBaseItem.RightCoinItem(
                    id = coin.id,
                    imageUrl = coin.iconUrl,
                    name = coin.name
                )
                coinBaseItem.add(rightCoinItem)
            } else {
                val leftCoinItem = CoinBaseItem.LeftCoinItem(
                    id = coin.id,
                    imageUrl = coin.iconUrl,
                    name = coin.name,
                    description = coin.description
                )
                coinBaseItem.add(leftCoinItem)
            }
        }
        return coinBaseItem
    }
}