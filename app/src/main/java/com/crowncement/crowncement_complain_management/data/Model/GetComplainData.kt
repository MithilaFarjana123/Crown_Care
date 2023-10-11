package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class GetComplainData(


    @SerializedName("req_no"               ) var reqNo              : String?             = null,
    @SerializedName("trn_date"             ) var trnDate            : String?             = null,
    @SerializedName("req_emp"              ) var reqEmp             : String?             = null,
    @SerializedName("req_emp_name"         ) var reqEmpName         : String?             = null,
    @SerializedName("dept_nam"             ) var deptNam            : String?             = null,
    @SerializedName("req_cat"              ) var reqCat             : String?             = null,
    @SerializedName("req_type"             ) var reqType            : String?             = null,
    @SerializedName("req_title"            ) var reqTitle           : String?             = null,
    @SerializedName("req_det"              ) var reqDet             : String?             = null,
    @SerializedName("req_date"             ) var reqDate            : String?             = null,
    @SerializedName("comp_name"            ) var compName           : String?             = null,
    @SerializedName("comp_mob"             ) var compMob            : String?             = null,
    @SerializedName("comp_email"           ) var compEmail          : String?             = null,
    @SerializedName("expected_resolv_date" ) var expectedResolvDate : String?             = null,
    @SerializedName("comp_prio"            ) var compPrio           : String?             = null,
    @SerializedName("req_img"              ) var reqImg             : String?             = null,
    @SerializedName("ent_user"             ) var entUser            : String?             = null,
    @SerializedName("trn_status"           ) var trnStatus          : String?             = null,
    @SerializedName("trn_flag"             ) var trnFlag            : String?             = null,
    @SerializedName("last_feedback_det"    ) var lastFeedbackDet    : String?             = null,
    @SerializedName("last_feedback_date"   ) var lastFeedbackDate   : String?             = null,
    @SerializedName("completed_date"       ) var completedDate      : String?             = null,
    @SerializedName("pending_to"           ) var pendingTo          : String?             = null,
    @SerializedName("follw_act"            ) var follwAct           : ArrayList<FollwAct> = arrayListOf()



)
