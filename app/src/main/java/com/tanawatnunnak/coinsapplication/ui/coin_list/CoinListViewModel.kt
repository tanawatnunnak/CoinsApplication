package com.tanawatnunnak.coinsapplication.ui.coin_list

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tanawatnunnak.coinsapplication.data.constant.SearchCoinType
import com.tanawatnunnak.coinsapplication.data.model.CoinResponse
import com.tanawatnunnak.coinsapplication.data.model.SearchCoinParam
import com.tanawatnunnak.coinsapplication.data.repository.CoinRepository
import com.tanawatnunnak.coinsapplication.extention.isAllUpperCase
import com.tanawatnunnak.coinsapplication.extention.isFirstUpperCase
import com.tanawatnunnak.coinsapplication.ui.base.BaseViewModel
import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinBaseItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

class CoinListViewModel(private val coinRepository: CoinRepository) : BaseViewModel() {

    private val _state: MutableLiveData<CoinListState> = MutableLiveData()
    val state: LiveData<CoinListState> = _state

    init {
        _state.value = CoinListState(isLoading = true)
    }

    fun getCoins() {
        _state.value?.let { state ->
            coinRepository.getCoins(state.limitPage, state.currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(::onSuccess, ::onError)
                .addTo(compositeDisposable)
        }
    }

    fun loadMore() {
        _state.value?.let { state ->
            state.currentPage++
            if (state.searchWord.isEmpty()) {
                getCoins()
            } else {
                search(state.searchWord)
            }
        }
    }

    fun refresh() {
        _state.value?.let { state ->
            state.currentPage = 0
            state.isRefreshing = true
            getCoins()
        }
    }

    fun search(searchWord: String) {
        _state.value?.let { state ->
            state.searchWord = searchWord
            state.currentPage = 0
            val searchCoinParam = SearchCoinParam(
                searchType = checkSearchType(searchWord),
                id = searchWord.toIntOrNull(),
                search = searchWord
            )
            coinRepository.searchCoin(searchCoinParam, state.limitPage, state.currentPage)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(::onSuccess, ::onError)
                .addTo(compositeDisposable)
        }
    }

    private fun checkSearchType(searchWord: String): Int {
        return when {
            searchWord.isDigitsOnly() -> SearchCoinType.ID
            else -> SearchCoinType.PREFIX
        }
    }

    private fun onError(throwable: Throwable) {
        val newState =
            _state.value?.copy(errorMessage = throwable.message ?: "something went wrong")
        _state.postValue(newState)
    }

    private fun onSuccess(response: Response<CoinResponse>) {
        if (response.isSuccessful && response.body() != null) {
            val coinBaseItemList = CoinConverter().convertToCoinItem(response.body()?.data?.coins)
            setCoinBaseItemList(coinBaseItemList)
        } else {
            setErrorState(response.message())
        }
    }

    private fun setErrorState(messageError: String) {
        val newState = _state.value?.copy(errorMessage = messageError, isLoading = false)
        _state.value = newState
    }

    private fun setCoinBaseItemList(coinBaseItemList: ArrayList<CoinBaseItem>?) {
        coinBaseItemList?.let { coinList ->
            val currentPage = _state.value?.currentPage ?: 0
            if (currentPage == 0) {
                _state.value?.coinList = ArrayList(coinList)
            } else {
                _state.value?.coinList?.addAll(coinList)
            }
            val newState = _state.value?.copy(
                errorMessage = "",
                isLoading = false,
                isRefreshing = false,
            )
            _state.value = newState
        }
    }

}