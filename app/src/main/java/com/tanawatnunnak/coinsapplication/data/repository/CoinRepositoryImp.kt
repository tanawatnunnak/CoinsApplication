package com.tanawatnunnak.coinsapplication.data.repository

import com.tanawatnunnak.coinsapplication.data.api.CoinApi
import com.tanawatnunnak.coinsapplication.data.constant.SearchCoinType
import com.tanawatnunnak.coinsapplication.data.model.CoinResponse
import com.tanawatnunnak.coinsapplication.data.model.SearchCoinParam
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

interface CoinRepository {
    fun getCoins(limitPage: Int, currentPage: Int): Observable<Response<CoinResponse>>
    fun searchCoin(searchParam: SearchCoinParam, limitPage: Int, currentPage: Int): Flowable<Response<CoinResponse>>
}

class CoinRepositoryImp(private val coinApi: CoinApi) : CoinRepository {
    override fun getCoins(limitPage: Int, currentPage: Int): Observable<Response<CoinResponse>> {
        return coinApi.getCoins(limitPage, currentPage)
    }

    override fun searchCoin(searchParam: SearchCoinParam, limitPage: Int, currentPage: Int): Flowable<Response<CoinResponse>> {
        return when (searchParam.searchType) {
            SearchCoinType.ID -> coinApi.searchId(searchParam.id ?: 0, limitPage, currentPage)
            else -> coinApi.searchPrefix(searchParam.search, limitPage, currentPage)
        }
    }
}