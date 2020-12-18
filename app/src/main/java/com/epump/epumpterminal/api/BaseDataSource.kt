package com.epump.epumpterminal.api

import com.epump.epumpterminal.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful || response.code() == 200) {
                val body = response.body()
                return if (body != null) {
                    Resource.success(body)
                } else{
                    Resource(Resource.Status.SUCCESS, body,null)
                }

            }
            return error("${response.code()} ${response.message()}")

        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network call has failed for a following reason: $message")
    }
}