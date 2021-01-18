package com.demo.shadi.interfaces

import com.demo.shadi.model.STATUS

interface MatchesItemClick {

    fun onItemClick(position: Int, status: STATUS)
}