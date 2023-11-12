package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("user_id"                 ) var userId               : String? = null,
    @SerializedName("user_role"               ) var userRole             : String? = null,
    @SerializedName("user_name"               ) var userName             : String? = null,
    @SerializedName("user_mobile"             ) var userMobile           : String? = null,
    @SerializedName("user_location"           ) var userLocation         : String? = null,
    @SerializedName("user_dept"               ) var userDept             : String? = null,
    @SerializedName("user_desig"              ) var userDesig            : String? = null,
    @SerializedName("user_join"               ) var userJoin             : String? = null,
    @SerializedName("user_bid"                ) var userBid              : String? = null,
    @SerializedName("user_profile_image_path" ) var userProfileImagePath : String? = null,
    @SerializedName("user_access"             ) var userAccess           : String? = null,
    @SerializedName("user_email"              ) var userEmail            : String? = null



)

