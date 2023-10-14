package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.data.Model.UpdateActionResponce
import com.crowncement.crowncement_complain_management.data.Model.UpdateSeenStatResponce
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

object UpdateSeenStatRepository {



    //Todo UpdateSeenStat
    fun getUpdateSeenStat(
        user_id: String,
        rq_trn_no:String
    ): MutableLiveData<Resource<UpdateSeenStatResponce>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
        val rq_trn_no = RequestBody.create("application/json".toMediaTypeOrNull(), rq_trn_no)
        //  val curr_mon = RequestBody.create("application/json".toMediaTypeOrNull(), curr_mon)

        val appInfo: MutableLiveData<Resource<UpdateSeenStatResponce>> =
            MutableLiveData<Resource<UpdateSeenStatResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.UpdateSeenStat(user_id,rq_trn_no)
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        appInfo.postValue(Resource.success(response))
                    },
                    { error -> //
                        appInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("Complain Repository", "ERROR : " + e.message)
        }
        return appInfo
    }


    //Todo UpdateSeenStat
    fun SaveUpdateAction(
        user_id: String,
        rq_trn_no:String,
        rq_trn_row:String,
        feedback_date:String,
        feedback_det:String,
        solution_det:String,
        action_type:String,
        doc_ext:String

        ): MutableLiveData<Resource<UpdateActionResponce>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
        val rq_trn_no = RequestBody.create("application/json".toMediaTypeOrNull(), rq_trn_no)
        val rq_trn_row = RequestBody.create("application/json".toMediaTypeOrNull(), rq_trn_row)
        val feedback_date = RequestBody.create("application/json".toMediaTypeOrNull(), feedback_date)
        val feedback_det = RequestBody.create("application/json".toMediaTypeOrNull(), feedback_det)
        val solution_det = RequestBody.create("application/json".toMediaTypeOrNull(), solution_det)
        val action_type = RequestBody.create("application/json".toMediaTypeOrNull(), action_type)
        val doc_ext = RequestBody.create("application/json".toMediaTypeOrNull(), doc_ext)


        val appInfo: MutableLiveData<Resource<UpdateActionResponce>> =
            MutableLiveData<Resource<UpdateActionResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.UpdateAction(user_id,rq_trn_no,rq_trn_row
            ,feedback_date,feedback_det,solution_det,action_type,doc_ext
            )
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        appInfo.postValue(Resource.success(response))
                    },
                    { error -> //
                        appInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("Complain Repository", "ERROR : " + e.message)
        }
        return appInfo
    }




}






