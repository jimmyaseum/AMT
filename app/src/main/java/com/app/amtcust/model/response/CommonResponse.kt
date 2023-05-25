package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

class CommonResponse {

    @SerializedName("Status")
    var code: Int = 0

    @SerializedName("Message")
    var message: String = ""

    @SerializedName("Details")
    var Details: String = ""
}
