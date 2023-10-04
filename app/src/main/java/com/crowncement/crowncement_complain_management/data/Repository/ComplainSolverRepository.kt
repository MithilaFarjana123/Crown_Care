package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.GetActivity4AppResponse
import com.crowncement.crowncement_complain_management.data.Model.SaveResponce
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

object ComplainSolverRepository {

    //TODO complain save
    fun getComplain4Solver(
        user_id: String
    ): MutableLiveData<Resource<GetActivity4AppResponse>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)

        val appInfo: MutableLiveData<Resource<GetActivity4AppResponse>> =
            MutableLiveData<Resource<GetActivity4AppResponse>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.GetActivity4Solver(user_id)
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
            Log.e("ComSolverRepository", "ERROR : " + e.message)
        }
        return appInfo
    }
}