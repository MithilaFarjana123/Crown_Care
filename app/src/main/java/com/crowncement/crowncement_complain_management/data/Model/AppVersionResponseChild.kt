package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class AppVersionResponseChild(

    @SerializedName("version_no") var versionNo: String? = "",
    @SerializedName("version_name") var versionName: String? = "",
    @SerializedName("version_url") var version_url: String? = ""

)
