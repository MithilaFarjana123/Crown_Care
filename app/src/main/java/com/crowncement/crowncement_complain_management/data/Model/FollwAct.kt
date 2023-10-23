package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class FollwAct(

    @SerializedName("act_row"       ) var actRow       : Int?    = null,
    @SerializedName("rep_to"        ) var repTo        : String? = null,
    @SerializedName("rep_to_name"   ) var repToName    : String? = null,
    @SerializedName("ent_time"      ) var entTime      : String? = null,
    @SerializedName("action_det"    ) var actionDet    : String? = null,
    @SerializedName("line_no"       ) var lineNo       : Int?    = null,
    @SerializedName("rep_type"      ) var repType      : String? = null,
    @SerializedName("act_status"    ) var actStatus    : String? = null,
    @SerializedName("rep_user"      ) var repUser      : String? = null,
    @SerializedName("foll_img"      ) var follImg      : String? = null,
    @SerializedName("feedback_date" ) var feedbackDate : String? = null,
    @SerializedName("feedback_det"  ) var feedbackDet  : String? = null,
    @SerializedName("seen_status"   ) var seenStatus   : String? = null,
    @SerializedName("act_time"      ) var actTime      : String? = null
)
