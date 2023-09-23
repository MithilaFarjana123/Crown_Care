package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.data.Model.UserInfo

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


object LoginRepository {

    fun getUserInfo(
        id: String,
        pass: String,
        deviceId: String,
        fcmToken: String
    ): MutableLiveData<Resource<UserInfo>> {
        val username = RequestBody.create("application/json".toMediaTypeOrNull(), id)
        val password = RequestBody.create("application/json".toMediaTypeOrNull(), pass)
        val device = RequestBody.create("application/json".toMediaTypeOrNull(), deviceId)
        val token = RequestBody.create("application/json".toMediaTypeOrNull(), fcmToken)

        val userInfo: MutableLiveData<Resource<UserInfo>> = MutableLiveData<Resource<UserInfo>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.userLogin(username, password, device, token)
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        userInfo.postValue(Resource.success(response))
                    },
                    { error -> //

                        userInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("LoginRepository", "ERROR login: " + e.message)
        }
        return userInfo
    }


}