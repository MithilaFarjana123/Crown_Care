package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class GetActivity4AppResponse(
    @SerializedName("code"    ) var code    : String?         = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<RequestDetails> = arrayListOf()
)
