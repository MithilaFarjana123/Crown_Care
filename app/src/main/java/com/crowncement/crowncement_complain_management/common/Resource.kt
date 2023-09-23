package com.crowncement.crowncement_complain_management.common

data class Resource<out T>(

    val status: Status, val responseData: T?, val message: String?) {

    companion object {

        fun <T> success(responseData: T?): Resource<T> {
            return Resource(Status.SUCCESS, responseData, null)
        }

        fun <T> error(msg: String, responseData: T?): Resource<T> {
            return Resource(Status.ERROR, responseData, msg)
        }

        fun <T> loading(responseData: T?): Resource<T> {
            return Resource(Status.LOADING, responseData, null)
        }

    }
}
