package com.demo.shadi.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.demo.shadi.model.Result
import com.demo.shadi.repository.ApiInterface

class MatchesDataFactory(
    private var apiInterface: ApiInterface
) : DataSource.Factory<Int, Result>() {

    private val matchesDataSourceLiveData = MutableLiveData<MatchesDataSource>()

    fun getMutableLiveData(): MutableLiveData<MatchesDataSource> {
        return matchesDataSourceLiveData
    }

    override fun create(): DataSource<Int, Result> {
        val matchesDataSource = MatchesDataSource(apiInterface)
        matchesDataSourceLiveData.postValue(matchesDataSource)
        return matchesDataSource
    }
}