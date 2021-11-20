package com.tanawatnunnak.coinsapplication.extention

import com.tanawatnunnak.coinsapplication.data.model.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

fun <T : Response<P>, P, R> Observable<T>.toResource(data: (T) -> R?): Observable<Resource<R>> {
    return this.map { response ->
        if (response.isSuccessful) {
            Resource.Success(data(response))
        } else {
            Resource.Error(data = null, message = response.message())
        }
    }
}

fun <T : Response<P>, P, R> Flowable<T>.toResource(data: (T) -> R?): Flowable<Resource<R>> {
    return this.map { response ->
        if (response.isSuccessful) {
            Resource.Success(data(response))
        } else {
            Resource.Error(data = null, message = response.message())
        }
    }
}