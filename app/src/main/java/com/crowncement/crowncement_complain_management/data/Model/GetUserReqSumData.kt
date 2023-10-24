package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class GetUserReqSumData(
    @SerializedName("incident_no"         ) var incidentNo        : Int? = null,
    @SerializedName("incident_pending_no" ) var incidentPendingNo : Int? = null,
    @SerializedName("inquiry_no"          ) var inquiryNo         : Int? = null,
    @SerializedName("inquiry_pending_no"  ) var inquiryPendingNo  : Int? = null,
    @SerializedName("applied_no"          ) var appliedNo         : Int? = null,
    @SerializedName("resolved_no"         ) var resolvedNo        : Int? = null,
    @SerializedName("pending_no"          ) var pendingNo         : Int? = null
)
