package com.crowncement.crowncement_complain_management.data.Model

import com.google.gson.annotations.SerializedName

data class GetEmpName(

    @SerializedName("emp_id"     ) var empId     : String? = null,
    @SerializedName("emp_name"   ) var empName   : String? = null,
    @SerializedName("emp_desig"  ) var empDesig  : String? = null,
    @SerializedName("emp_dept"   ) var empDept   : String? = null,
    @SerializedName("emp_cat"    ) var empCat    : String? = null,
    @SerializedName("emp_loc"    ) var empLoc    : String? = null,
    @SerializedName("emp_email"  ) var empEmail  : String? = null,
    @SerializedName("emp_mobile" ) var empMobile : String? = null,
    @SerializedName("emp_status" ) var empStatus : String? = null,
    @SerializedName("emp_join"   ) var empJoin   : String? = null,
    @SerializedName("emp_bid"    ) var empBid    : Int?    = null,
    @SerializedName("emp_img"    ) var empImg    : String? = null,
    @SerializedName("bus_name"   ) var busName   : String? = null
)
