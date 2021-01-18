package com.demo.shadi.repository

import com.demo.shadi.model.PageInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/")
    fun getMatchesList(
        @Query("results") results: Any
    ): Call<PageInformation>
}