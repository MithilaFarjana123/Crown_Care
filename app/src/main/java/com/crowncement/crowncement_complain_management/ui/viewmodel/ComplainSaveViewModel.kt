package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.DepartmentResponce
import com.crowncement.crowncement_complain_management.data.Model.SaveResponce
import com.crowncement.crowncement_complain_management.data.Repository.ComplainRepository
import com.crowncement.crowncement_complain_management.data.Repository.ComplainSaveRepository
import java.io.File

class ComplainSaveViewModel : ViewModel(){

    private var NewSaveComplainMutableLiveData: MutableLiveData<Resource<SaveResponce>>? = null

    private var NewSaveComplainImgMutableLiveData: MutableLiveData<Resource<SaveResponce>>? = null

    //Todo save new complain
    fun getNewComplainData(
        user_id:String,
        dept_code:String,
        req_cat:String,
        req_type:String,
        req_title:String,
        req_det:String,
        occ_date:String,
        comp_mob:String,
        comp_email:String,
        req_prior:String,
        comp_name:String
    ): MutableLiveData<Resource<SaveResponce>>? {

        NewSaveComplainMutableLiveData = ComplainSaveRepository.getComplainSave(user_id,dept_code,
            req_cat,req_type,req_title,req_det,occ_date,comp_mob,comp_email,req_prior,comp_name
                    )

        return NewSaveComplainMutableLiveData

    }


    //Todo save new complain
    fun getNewComplainImgData(
        user_id:String,
        dept_code:String,
        req_cat:String,
        req_type:String,
        req_title:String,
        req_det:String,
        occ_date:String,
        comp_mob:String,
        comp_email:String,
        req_prior:String,
        doc_ext: String,
        all_images: File,
        comp_name: String
    ): MutableLiveData<Resource<SaveResponce>>? {

        NewSaveComplainImgMutableLiveData = ComplainSaveRepository.getComplainSaveIMG(user_id,dept_code,
            req_cat,req_type,req_title,req_det,occ_date,comp_mob,comp_email,req_prior,doc_ext,all_images,comp_name
        )

        return NewSaveComplainImgMutableLiveData

    }



}