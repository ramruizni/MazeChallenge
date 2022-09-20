package com.example.mazechallenge.common

enum class Status {
    LOADING,
    ERROR,
    SUCCESS
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> loading(data: T?) = Resource(Status.LOADING, data, null)
        fun <T> error(message: String, data: T?) = Resource(Status.ERROR, null, message)
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)
    }
}
