package com.techkintan.weathercast.helper

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        val data = block()
        Result.Success(data)
    } catch (e: IOException) {
        Result.Error("Network error. Please check your connection.", e)
    } catch (e: HttpException) {
        val code = e.code()
        val errorBody = e.response()?.errorBody()?.string()

        val message = when (code) {
            404 -> {
                try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("message", "City not found")
                } catch (ex: Exception) {
                    "City not found"
                }
            }
            else -> "HTTP $code error: ${e.message()}"
        }

        Result.Error("$message", e)
    } catch (e: Exception) {
        Result.Error("Unexpected error: ${e.localizedMessage}", e)
    }
}
