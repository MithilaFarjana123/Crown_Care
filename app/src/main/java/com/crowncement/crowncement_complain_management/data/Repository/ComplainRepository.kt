package com.crowncement.crowncement_complain_management.data.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.common.API.GetDataService
import com.crowncement.crowncement_complain_management.common.API.RetrofitClientInstance
import com.crowncement.crowncement_complain_management.data.Model.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


object ComplainRepository {


    //TODO getDepartment
    fun getSavedDepartment(): MutableLiveData<Resource<DepartmentResponce>> {

        val appInfo: MutableLiveData<Resource<DepartmentResponce>> =
            MutableLiveData<Resource<DepartmentResponce>>()


        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.savedDepartment()
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        appInfo.postValue(Resource.success(response))

                    },
                    { error ->
                        appInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("ComplainRepository", "ERROR : " + e.message)
        }
        return appInfo
    }



    //todo Emp name
    fun getSavedEmpName(
        dept_name: String
    ): MutableLiveData<Resource<GetEmpNameResponce>> {
        val dept_name = RequestBody.create("application/json".toMediaTypeOrNull(), dept_name)

        val appInfo: MutableLiveData<Resource<GetEmpNameResponce>> =
            MutableLiveData<Resource<GetEmpNameResponce>>()


        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance_forEmp.create(
                GetDataService::class.java
            )
            service.savedEmpname(dept_name)
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        appInfo.postValue(Resource.success(response))

                    },
                    { error ->
                        appInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("ComplainRepository", "ERROR : " + e.message)
        }
        return appInfo
    }

//Todo get incident/inquiry category
    fun getSavedInCategory(doc_cat:String,dept:String): MutableLiveData<Resource<CategoryResponce>> {


    val doc_cat = RequestBody.create("application/json".toMediaTypeOrNull(), doc_cat)
    val dept = RequestBody.create("application/json".toMediaTypeOrNull(), dept)
        val appInfo: MutableLiveData<Resource<CategoryResponce>> =
            MutableLiveData<Resource<CategoryResponce>>()


        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
        service.getInCategory(doc_cat,dept)
            ?.toObservable()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { response ->
                    appInfo.postValue(Resource.success(response))

                },
                { error ->
                    appInfo.postValue(Resource.error(error.message.toString(), null))

                },
            )

    } catch (e: Exception) {
        Log.e("ComplainRepository", "ERROR : " + e.message)
    }
    return appInfo
}

    //Todo title

    fun getSavedTitle(doc_cat:String,dept:String, trn_parent:String): MutableLiveData<Resource<TitleResponce>> {


        val doc_cat = RequestBody.create("application/json".toMediaTypeOrNull(), doc_cat)
        val dept = RequestBody.create("application/json".toMediaTypeOrNull(), dept)
        val  trn_parent = RequestBody.create("application/json".toMediaTypeOrNull(), trn_parent)
        val appInfo: MutableLiveData<Resource<TitleResponce>> =
            MutableLiveData<Resource<TitleResponce>>()


        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.getTitle(doc_cat,dept,trn_parent)
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        appInfo.postValue(Resource.success(response))

                    },
                    { error ->
                        appInfo.postValue(Resource.error(error.message.toString(), null))

                    },
                )

        } catch (e: Exception) {
            Log.e("ComplainRepository", "ERROR : " + e.message)
        }
        return appInfo
    }


    //Todo imageupload

    fun complainImageUpload(
        party_code: String,
        party_type: String,
        doc_type: String,
        doc_ext: String,
        all_images: ArrayList<File>

    ): MutableLiveData<Resource<ImageResponce>> {
        val party_code = RequestBody.create("application/json".toMediaTypeOrNull(), party_code)
        val party_type = RequestBody.create("application/json".toMediaTypeOrNull(), party_type)
        val doc_type = RequestBody.create("application/json".toMediaTypeOrNull(), doc_type)
        val ext = RequestBody.create("application/json".toMediaTypeOrNull(), doc_ext)


        val processImage: ArrayList<MultipartBody.Part> = ArrayList()
        var requestFile: RequestBody
        if (all_images.size > 0) {
            for (item in all_images) {
                // requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), item.absoluteFile)
                requestFile = if (doc_ext != "pdf") {
                    RequestBody.create("image/*".toMediaTypeOrNull(), all_images[0])
                } else {
                    RequestBody.create("application/pdf".toMediaTypeOrNull(), all_images[0])
                }

                processImage.add(
                    MultipartBody.Part.createFormData(
                        "all_images",
                        item.name,
                        requestFile
                    )
                )
            }
        } else {

            requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), "")
            processImage.add(MultipartBody.Part.createFormData("all_images", "", requestFile))
        }

        val saveCommonExpense: MutableLiveData<Resource<ImageResponce>> =
            MutableLiveData<Resource<ImageResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.compImageUpload(
                party_code,
                party_type,
                doc_type,
                ext,
                processImage[0]

            )
                ?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { response ->
                        saveCommonExpense.postValue(Resource.success(response))
                    },
                    { error ->
                        saveCommonExpense.postValue(Resource.error(error.message.toString(), null))
                    },
                )

        } catch (e: Exception) {
            Log.e("Complain Repository", "ERROR: " + e.message)
        }
        return saveCommonExpense
    }


    //Todo get Saved complain
    fun getSavedComplain(
        user_id: String
      //  curr_yr: String,
       // curr_mon: String
    ): MutableLiveData<Resource<GetComplainResponse>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
      //  val curr_yr = RequestBody.create("application/json".toMediaTypeOrNull(), curr_yr)
      //  val curr_mon = RequestBody.create("application/json".toMediaTypeOrNull(), curr_mon)

        val appInfo: MutableLiveData<Resource<GetComplainResponse>> =
            MutableLiveData<Resource<GetComplainResponse>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.GetComplainedInfo(user_id)
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


    //Todo get Saved complain
    fun getSavedComplainSummary(
        user_id: String
        //  curr_yr: String,
        // curr_mon: String
    ): MutableLiveData<Resource<GetUserReqSumResponce>> {
        val user_id = RequestBody.create("application/json".toMediaTypeOrNull(), user_id)
        //  val curr_yr = RequestBody.create("application/json".toMediaTypeOrNull(), curr_yr)
        //  val curr_mon = RequestBody.create("application/json".toMediaTypeOrNull(), curr_mon)

        val appInfo: MutableLiveData<Resource<GetUserReqSumResponce>> =
            MutableLiveData<Resource<GetUserReqSumResponce>>()

        try {
            val service: GetDataService = RetrofitClientInstance.retrofitInstance.create(
                GetDataService::class.java
            )
            service.GetUserReqSum(user_id)
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



