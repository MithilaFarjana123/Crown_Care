package com.crowncement.crowncement_complain_management.data

import com.google.gson.annotations.SerializedName

data class Escalationresponce(

    @SerializedName("code"           ) var code          : String? = null,
    @SerializedName("message"        ) var message       : String? = null,
    @SerializedName("notific_status" ) var notificStatus : String? = null
)
