package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
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

}