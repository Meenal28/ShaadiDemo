package com.demo.shadi.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.shadi.model.Result


@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatchesList(matchesList: List<Result>?)

    @Query("SELECT * FROM Matches")
    fun getMatchesList(): DataSource<Int, Result>
}