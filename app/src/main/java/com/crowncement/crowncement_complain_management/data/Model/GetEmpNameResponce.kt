package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class GetEmpNameResponce(
    @SerializedName("code"    ) var code    : String?         = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<GetEmpName> = arrayListOf()
)
