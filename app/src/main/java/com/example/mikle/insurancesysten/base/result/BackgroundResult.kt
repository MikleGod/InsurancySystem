package com.example.mikle.insurancesysten.base.result

class BackgroundResult(
    val success: String? = null,
    val error: String? = null,
    val status: Status
) {

    enum class Status{
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun loading(): BackgroundResult = BackgroundResult(null, null, Status.LOADING)
        fun success(message: String): BackgroundResult = BackgroundResult(message, null, Status.SUCCESS)
        fun error(message: String): BackgroundResult = BackgroundResult(null, message, Status.ERROR)
    }

}