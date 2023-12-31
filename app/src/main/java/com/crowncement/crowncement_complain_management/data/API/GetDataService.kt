package com.crowncement.crowncement_complain_management.common.API

import com.crowncement.crowncement_complain_management.data.Model.*
import io.reactivex.rxjava3.core.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GetDataService {


        //TODO ------> CHeck APP version
        @Multipart
        @POST("VersionCheck")
        fun getAppVersion(
            @Part("app_name") app_name: RequestBody,
            @Part("cur_date") cur_date: RequestBody

        ): Flowable<AppVersionResponse>?


    //TODO -----> LOGIN

    @Multipart
    @POST("login")
    fun userLogin(
        @Part("user_id") user_id: RequestBody,
        @Part("user_password") user_password: RequestBody,
        @Part("device_id") device_id: RequestBody,
        @Part("fcm_token") fcm_token: RequestBody
    ): Flowable<UserInfo>?


    //todo department
    @GET("GetDept")
    fun savedDepartment(): Flowable<DepartmentResponce>?


    //Todo for GetEmp name
    @Multipart
    @POST("DeptEmpList")
    fun savedEmpname(
        @Part("dept_name") dept_name : RequestBody,
    ): Flowable<GetEmpNameResponce>?

    //todo inquiry/incident Category
    @Multipart
    @POST("GetTrnType")
    fun getInCategory(
        @Part("doc_cat") doc_cat: RequestBody,
        @Part("dept") dept: RequestBody
    ): Flowable<CategoryResponce>?

    //Todo title
    @Multipart
    @POST("GetTrnSub")
    fun getTitle(
        @Part("doc_cat") doc_cat: RequestBody,
        @Part("dept") dept: RequestBody,
        @Part("trn_parent")  trn_parent: RequestBody
    ): Flowable<TitleResponce>?

    //Todo for Image
    @Multipart
    @POST("PartyDocUpload")
    fun compImageUpload(
        @Part("party_code") party_code: RequestBody,
        @Part("party_type") party_type: RequestBody,
        @Part("doc_type") doc_type: RequestBody,
        @Part("doc_ext") doc_ext: RequestBody,
        @Part all_images: MultipartBody.Part

    ): Flowable<ImageResponce>?

    //Todo save complain
    @Multipart
    @POST("ReqSubmit")
    fun saveNewComPlain(
        @Part("user_id") user_id: RequestBody,
        @Part("dept_code") dept_code: RequestBody,
        @Part("req_cat") req_cat: RequestBody,
        @Part("req_type") req_type: RequestBody,
        @Part("req_title") req_title: RequestBody,
        @Part("req_det") req_det: RequestBody,
        @Part("occ_date") occ_date: RequestBody,
        @Part("comp_mob") comp_mob: RequestBody,
        @Part("comp_email") comp_email: RequestBody,
        @Part("req_prior") req_prior: RequestBody,
        @Part("comp_name" ) comp_name: RequestBody,
        @Part("exp_solve_date" ) exp_solve_date: RequestBody

        ) : Flowable<SaveResponce>?


    //Todo Save complain with Img
    @Multipart
    @POST("ReqSubmitImg")
    fun saveNewComPlainImg(
        @Part("user_id") user_id: RequestBody,
        @Part("dept_code") dept_code: RequestBody,
        @Part("req_cat") req_cat: RequestBody,
        @Part("req_type") req_type: RequestBody,
        @Part("req_title") req_title: RequestBody,
        @Part("req_det") req_det: RequestBody,
        @Part("occ_date") occ_date: RequestBody,
        @Part("comp_mob") comp_mob: RequestBody,
        @Part("comp_email") comp_email: RequestBody,
        @Part("req_prior") req_prior: RequestBody,
        @Part("doc_ext") doc_ext: RequestBody,
        @Part all_images: MultipartBody.Part,
        @Part("comp_name") comp_name: RequestBody,
        @Part("exp_solve_date") exp_solve_date: RequestBody


    ) : Flowable<SaveResponce>?


    //todo get saved data
    @Multipart
    @POST("GetUserActivity")
    fun GetComplainedInfo(
        @Part("user_id") user_id: RequestBody
      //  @Part("curr_yr") cur_date: RequestBody,
       // @Part("curr_mon") rep_type: RequestBody
    ): Flowable<GetComplainResponse>?


    //Todo GetActivity4App
    @Multipart
    @POST("GetActivity4App")
    fun GetActivity4Solver(
        @Part("user_id") user_id: RequestBody
    ) : Flowable<GetActivity4AppResponse>?


    //TOdO UpdateSeenStat
    @Multipart
    @POST("UpdateSeenStat")
    fun UpdateSeenStat(
        @Part("user_id") user_id: RequestBody,
        @Part("rq_trn_no") rq_trn_no: RequestBody

        ) : Flowable<UpdateSeenStatResponce>?

    //Todo UpdateAction
    @Multipart
    @POST("UpdateAction")
    fun UpdateAction(
        @Part("user_id") user_id: RequestBody,
        @Part("rq_trn_no") rq_trn_no: RequestBody,
        @Part("rq_trn_row") rq_trn_row: RequestBody,
        @Part("feedback_date") feedback_date: RequestBody,
        @Part("feedback_det") feedback_det: RequestBody,
        @Part("solution_det") solution_det: RequestBody,
        @Part("action_type") action_type: RequestBody,
        @Part("doc_ext") doc_ext: RequestBody

        ) : Flowable<UpdateActionResponce>?


    //Todo Escalate
    @Multipart
    @POST("EscalateTrn")
    fun EscalateTrn(
        @Part("user_id") user_id: RequestBody,
        @Part("rq_trn_no") rq_trn_no: RequestBody,
        @Part("rq_trn_row") rq_trn_row: RequestBody,
        @Part("esc_to") esc_to: RequestBody,
        @Part("esc_remark") esc_remark: RequestBody

        ) : Flowable<Escalationresponce>?


    //todo GetUserReqSum
    @Multipart
    @POST("GetUserReqSum")
    fun GetUserReqSum(
        @Part("user_id") user_id: RequestBody
    ) : Flowable<GetUserReqSumResponce>?
}

