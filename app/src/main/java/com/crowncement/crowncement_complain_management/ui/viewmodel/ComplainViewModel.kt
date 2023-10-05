package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.*
import com.crowncement.crowncement_complain_management.data.Repository.ComplainRepository
import java.io.File

class ComplainViewModel: ViewModel() {

    private var getsavedDepartmentMutableLiveData: MutableLiveData<Resource<DepartmentResponce>>? = null

    private var getsavedInCategoryMutableLiveData: MutableLiveData<Resource<CategoryResponce>>? = null

    private var getSavedTitleMutableLiveData: MutableLiveData<Resource<TitleResponce>>? = null

    private var getImageMutableLiveData:MutableLiveData<Resource<ImageResponce>>? = null

    private var getsavedComplainMutableLiveData:MutableLiveData<Resource<GetComplainResponse>>? = null

    //Todo get depertment
    fun getDepartmentData(

        ): MutableLiveData<Resource<DepartmentResponce>>? {

        getsavedDepartmentMutableLiveData = ComplainRepository.getSavedDepartment()

        return getsavedDepartmentMutableLiveData
    }

    //Todo get category
    fun getInCategoryData(
        doc_cat:String,dept:String
    ): MutableLiveData<Resource<CategoryResponce>>? {

        getsavedInCategoryMutableLiveData = ComplainRepository.getSavedInCategory(doc_cat,dept)

        return getsavedInCategoryMutableLiveData
    }

    //Todo Title
    fun getTitleData(
        doc_cat:String,dept:String,trn_parent:String
    ): MutableLiveData<Resource<TitleResponce>>? {

        getSavedTitleMutableLiveData = ComplainRepository.getSavedTitle(doc_cat,dept,trn_parent)

        return getSavedTitleMutableLiveData
    }

    //Todo Image
    fun VisitorImageUpload(
        party_code: String,
        party_type: String,
        doc_type: String,
        doc_ext: String,
        all_images: File
    ): MutableLiveData<Resource<ImageResponce>>? {

        getImageMutableLiveData = ComplainRepository.getImageUpload(party_code, party_type,doc_type,
            doc_ext,all_images)

        return getImageMutableLiveData
    }



    //Todo get Saved complain
    fun getSavedcomplain(
        user_id: String
      //  curr_yr: String,
       // curr_mon: String
    ): MutableLiveData<Resource<GetComplainResponse>>? {

        getsavedComplainMutableLiveData = ComplainRepository.getSavedComplain(user_id)

        return getsavedComplainMutableLiveData
    }


}