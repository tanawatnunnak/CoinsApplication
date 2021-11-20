package com.tanawatnunnak.coinsapplication.ui.coin_list

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tanawatnunnak.coinsapplication.data.constant.SearchCoinType
import com.tanawatnunnak.coinsapplication.data.model.Coin
import com.tanawatnunnak.coinsapplication.data.model.CoinResponse
import com.tanawatnunnak.coinsapplication.data.model.Resource
import com.tanawatnunnak.coinsapplication.data.model.SearchCoinParam
import com.tanawatnunnak.coinsapplication.data.repository.CoinRepository
import com.tanawatnunnak.coinsapplication.ui.base.BaseViewModel
import com.tanawatnunnak.coinsapplication.ui.coin_list.CoinListFragment.Companion.FIRST_PAGE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinListViewModel(
    private val coinRepository: CoinRepository,
    private val coinConverter: CoinConverter
) : BaseViewModel() {

    private val _state: MutableLiveData<CoinListState> = MutableLiveData()
    val state: LiveData<CoinListState> = _state

    init {
        _state.value = CoinListState(isLoading = true)
    }

    fun fetchCoins() {
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
            state.currentPage += 10
            if (state.searchWord.isEmpty()) {
                fetchCoins()
            } else {
                search(state.searchWord)
            }
        }
    }

    fun refresh() {
        _state.value?.let { state ->
            state.currentPage = FIRST_PAGE
            if (state.searchWord.isEmpty()) {
                fetchCoins()
            } else {
                search(state.searchWord)
            }
        }
    }

    fun searchWord(searchWord: String) {
        _state.value?.let { state ->
            if (searchWord.isEmpty()) {
                state.searchWord = searchWord
                fetchCoins()
            } else {
                if (searchWord != state.searchWord) {
                    state.searchWord = searchWord
                    state.currentPage = FIRST_PAGE
                    search(searchWord)
                }
            }
        }
    }

    private fun search(searchWord: String) {
        _state.value?.let { state ->

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
        setErrorState(throwable.message ?: "something went wrong")
    }

    private fun onSuccess(resource: Resource<CoinResponse>) {
        when (resource) {
            is Resource.Error -> setErrorState(messageError = resource.message)
            is Resource.Success -> setCoinBaseItemList(
                coinList = resource.data?.data?.coins,
                total = resource.data?.data?.stats?.total ?: 0
            )
        }
    }

    private fun setErrorState(messageError: String?) {
        val newState = _state.value?.copy(errorMessage = messageError ?: "", isLoading = false)
        _state.value = newState
    }

    private fun setCoinBaseItemList(coinList: List<Coin?>?, total: Int) {
        coinList?.let { list ->
            val coinBaseItemList = coinConverter.convertToCoinItem(list)
            val currentPage = _state.value?.currentPage ?: FIRST_PAGE

            if (currentPage == FIRST_PAGE) {
                _state.value?.coinList = coinBaseItemList
            } else {
                _state.value?.coinList?.addAll(coinBaseItemList)
            }

            val newState = _state.value?.copy(
                errorMessage = "",
                isLoading = false,
                isLastPage = currentPage >= total || total == 0
            )

            _state.value = newState
        }
    }

}