package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.data.Model.AppVersionResponse


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

object SplashRepository {

    fun getUserInfo(
        appName: String,
        date: String
    ): MutableLiveData<Resource<AppVersionResponse>> {
        val app_name = RequestBody.create("application/json".toMediaTypeOrNull(), appName)
        val cur_date = RequestBody.create("application/json".toMediaTypeOrNull(), date)
        val appInfo: MutableLiveData<Resource<AppVersionResponse>> =
            MutableLiveData<Resource<AppVersionResponse>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.getAppVersion(app_name, cur_date)
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
            Log.e("LoginRepository", "ERROR login: " + e.message)
        }
        return appInfo
    }

}


