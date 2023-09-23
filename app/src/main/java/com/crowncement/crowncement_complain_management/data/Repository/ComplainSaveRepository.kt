package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.SaveResponce
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ComplainSaveRepository {

    //TODO complain save
    fun getComplainSave(
        user_id: String,
        dept_code: String,
        req_cat: String,
        req_type: String,
        req_title: String,
        req_det: String,
        occ_date: String,
        comp_mob: String,
        comp_email: String,
        req_prior: String,
        comp_name:String


    ): MutableLiveData<Resource<SaveResponce>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
        val dept_code = RequestBody.create("application/json".toMediaTypeOrNull(), dept_code)
        val req_cat = RequestBody.create("application/json".toMediaTypeOrNull(), req_cat)
        val req_type = RequestBody.create("application/json".toMediaTypeOrNull(), req_type)
        val req_title = RequestBody.create("application/json".toMediaTypeOrNull(), req_title)
        val req_det = RequestBody.create("application/json".toMediaTypeOrNull(), req_det)
        val occ_date = RequestBody.create("application/json".toMediaTypeOrNull(), occ_date)
        val comp_mob = RequestBody.create("application/json".toMediaTypeOrNull(), comp_mob)
        val comp_email = RequestBody.create("application/json".toMediaTypeOrNull(), comp_email)
        val req_prior = RequestBody.create("application/json".toMediaTypeOrNull(), req_prior)
        val comp_name = RequestBody.create("application/json".toMediaTypeOrNull(), comp_name)
        // val regis_info = profile_info.toRequestBody("application/json".toMediaTypeOrNull())
        val appInfo: MutableLiveData<Resource<SaveResponce>> =
            MutableLiveData<Resource<SaveResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.saveNewComPlain(user_id,dept_code,req_cat,req_type,
                req_title,req_det,occ_date,comp_mob,comp_email,req_prior,comp_name)
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
            Log.e("RegisterRepository", "ERROR : " + e.message)
        }
        return appInfo
    }



    //TODO complain save with img
    fun getComplainSaveIMG(
        user_id: String,
        dept_code: String,
        req_cat: String,
        req_type: String,
        req_title: String,
        req_det: String,
        occ_date: String,
        comp_mob: String,
        comp_email: String,
        req_prior: String,
        doc_ext: String,
        all_images: File,
        comp_name:String


    ): MutableLiveData<Resource<SaveResponce>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
        val dept_code = RequestBody.create("application/json".toMediaTypeOrNull(), dept_code)
        val req_cat = RequestBody.create("application/json".toMediaTypeOrNull(), req_cat)
        val req_type = RequestBody.create("application/json".toMediaTypeOrNull(), req_type)
        val req_title = RequestBody.create("application/json".toMediaTypeOrNull(), req_title)
        val req_det = RequestBody.create("application/json".toMediaTypeOrNull(), req_det)
        val occ_date = RequestBody.create("application/json".toMediaTypeOrNull(), occ_date)
        val comp_mob = RequestBody.create("application/json".toMediaTypeOrNull(), comp_mob)
        val comp_email = RequestBody.create("application/json".toMediaTypeOrNull(), comp_email)
        val req_prior = RequestBody.create("application/json".toMediaTypeOrNull(), req_prior)
        val doc_ext = RequestBody.create("application/json".toMediaTypeOrNull(), doc_ext)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), all_images)
        val comp_name=RequestBody.create("application/json".toMediaTypeOrNull(), comp_name)
        val complainImage: MultipartBody.Part =
            MultipartBody.Part.createFormData("all_images", all_images.name, requestFile)

        // val regis_info = profile_info.toRequestBody("application/json".toMediaTypeOrNull())
        val appInfo: MutableLiveData<Resource<SaveResponce>> =
            MutableLiveData<Resource<SaveResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.saveNewComPlainImg(user_id,dept_code,req_cat,req_type,
                req_title,req_det,occ_date,comp_mob,comp_email,req_prior,doc_ext,complainImage,comp_name)
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
            Log.e("RegisterRepository", "ERROR : " + e.message)
        }
        return appInfo
    }


}