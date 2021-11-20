package com.tanawatnunnak.coinsapplication.data.repository

import com.tanawatnunnak.coinsapplication.data.api.CoinApi
import com.tanawatnunnak.coinsapplication.data.constant.SearchCoinType
import com.tanawatnunnak.coinsapplication.data.model.CoinResponse
import com.tanawatnunnak.coinsapplication.data.model.Resource
import com.tanawatnunnak.coinsapplication.data.model.SearchCoinParam
import com.tanawatnunnak.coinsapplication.extention.toResource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

interface CoinRepository {
    fun getCoins(limitPage: Int, currentPage: Int): Observable<Resource<CoinResponse>>
    fun searchCoin(
        searchParam: SearchCoinParam,
        limitPage: Int,
        currentPage: Int
    ): Flowable<Resource<CoinResponse>>
}

class CoinRepositoryImp(private val coinApi: CoinApi) : CoinRepository {
    override fun getCoins(limitPage: Int, currentPage: Int): Observable<Resource<CoinResponse>> {
        return coinApi.getCoins(limitPage, currentPage).toResource { it.body() }
    }

    override fun searchCoin(
        searchParam: SearchCoinParam,
        limitPage: Int,
        currentPage: Int
    ): Flowable<Resource<CoinResponse>> {
        return when (searchParam.searchType) {
            SearchCoinType.ID -> coinApi.searchId(searchParam.id ?: 0, limitPage, currentPage)
                .toResource { it.body() }
            else -> coinApi.searchPrefix(searchParam.search, limitPage, currentPage)
                .toResource { it.body() }
        }
    }
}