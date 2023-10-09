package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class UpdateSeenStatResponce(

    @SerializedName("code"    ) var code    : String? = null,
    @SerializedName("message" ) var message : String? = null

)
