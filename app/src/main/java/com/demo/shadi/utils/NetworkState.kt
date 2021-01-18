package com.demo.shadi.utils

class NetworkState(private var status: Status, private var msg: String) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
    }

    enum class Status {
        RUNNING, SUCCESS, FAILED
    }


    fun getStatus(): Status? {
        return status
    }

    fun getMsg(): String? {
        return msg
    }
}