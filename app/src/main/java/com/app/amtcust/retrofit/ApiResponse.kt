package com.app.amtcust.retrofit


class ApiResponse<T>(var status: Boolean=false, var data: T? =null, var message: String="") {
}