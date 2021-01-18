package com.demo.shadi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.shadi.data.MatchesDataFactory
import com.demo.shadi.data.MatchesDataSource
import com.demo.shadi.model.Result
import com.demo.shadi.repository.ApiInterface
import com.demo.shadi.repository.MatchesResultRepository
import com.demo.shadi.repository.RetrofitClientInstance
import com.demo.shadi.utils.NetworkState

class MatchesViewModel : ViewModel() {

    var matchesRepository: MatchesResultRepository? = MatchesResultRepository().getInstance()

    fun getMatchesList(page: Int): LiveData<List<Result>>? {
        return matchesRepository?.getUserData(page)
    }

    private val apiInterface =
        RetrofitClientInstance.getRetrofitInstance().create(ApiInterface::class.java)
    var matchesList: LiveData<PagedList<Result>>
    private val pageSize = 5
    private val matchesDataFactory: MatchesDataFactory

    init {
        matchesDataFactory = MatchesDataFactory(apiInterface)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        matchesList = LivePagedListBuilder(matchesDataFactory, config).build()
    }


    /**
     * Get Network state
     */
    fun getState(): LiveData<NetworkState> = Transformations.switchMap(
        matchesDataFactory.getMutableLiveData(),
        MatchesDataSource::networkState
    )

    /**
     * get Matches List
     */
    fun getMatchesLiveData(): LiveData<PagedList<Result>> {
        return matchesList
    }

}