package com.demo.shadi.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.demo.shadi.model.PageInformation
import com.demo.shadi.model.Result
import com.demo.shadi.repository.ApiInterface
import com.demo.shadi.utils.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchesDataSource(private val apiInterface: ApiInterface) : PageKeyedDataSource<Int, Result>() {

    val TAG: String = MatchesDataSource::class.java.simpleName
    var networkState = MutableLiveData<NetworkState>()
    var initialLoading = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Result>
    ) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)
        apiInterface.getMatchesList(10)
            .enqueue(object : Callback<PageInformation> {
                override fun onResponse(
                    call: Call<PageInformation>,
                    response: Response<PageInformation>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.results?.let { callback.onResult(it, null, 2) }
                        initialLoading.postValue(NetworkState.LOADED)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        initialLoading.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )
                        networkState.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<PageInformation>, t: Throwable) {
                    val errorMessage = t.message ?: "unknown error"
                    Log.i(TAG, "error message:$errorMessage");
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        Log.i(TAG, "Loading " + params.key.toString() + " Count " + params.requestedLoadSize)
        networkState.postValue(NetworkState.LOADING)
        apiInterface.getMatchesList(10)
            .enqueue(object : Callback<PageInformation> {
                override fun onResponse(
                    call: Call<PageInformation>,
                    response: Response<PageInformation>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val nextKey: Int = response.body()?.info?.page?.plus(1) ?: 0
                        response.body()?.results?.let { callback.onResult(it, nextKey) }
                        networkState.postValue(NetworkState.LOADED)
                    } else networkState.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response.message()
                        )
                    )
                }

                override fun onFailure(call: Call<PageInformation>, t: Throwable) {
                    val errorMessage = t.message ?: "unknown error"
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }
}