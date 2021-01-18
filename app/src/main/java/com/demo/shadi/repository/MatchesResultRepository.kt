package com.demo.shadi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.shadi.model.Info
import com.demo.shadi.model.PageInformation
import com.demo.shadi.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MatchesResultRepository {

    private var apiInterface: ApiInterface =
        RetrofitClientInstance.getRetrofitInstance().create(ApiInterface::class.java)
    private lateinit var usersData: MutableLiveData<PageInformation>
    private lateinit var mCurrentPage: Info

    private var matchesResultRepository: MatchesResultRepository? = null

    fun getInstance(): MatchesResultRepository? {
        if (matchesResultRepository == null) {
            matchesResultRepository = MatchesResultRepository()
        }
        return matchesResultRepository
    }

    fun getUserData(page: Int): LiveData<List<Result>> {
        var liveData: MutableLiveData<List<Result>> = MutableLiveData()
        apiInterface.getMatchesList(10).enqueue(object : Callback<PageInformation> {
            override fun onResponse(
                call: Call<PageInformation>?,
                response: Response<PageInformation>
            ) {
                Log.d("TAG1", "response$response")
                if (response.body() != null && response.isSuccessful) {
                    mCurrentPage = response.body()?.info ?: Info()

                    var matchesList: List<Result> = response.body()?.results ?: emptyList()
                    liveData.postValue(matchesList)
                }
            }

            override fun onFailure(call: Call<PageInformation>?, t: Throwable?) {
                Log.d("TAG1", "error : " + t?.message)
            }
        })
        return liveData
    }
}