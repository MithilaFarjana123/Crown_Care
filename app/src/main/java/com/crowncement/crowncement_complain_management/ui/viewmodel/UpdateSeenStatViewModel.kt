package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.SaveResponce
import com.crowncement.crowncement_complain_management.data.Model.UpdateSeenStatResponce
import com.crowncement.crowncement_complain_management.data.Repository.ComplainSaveRepository
import com.crowncement.crowncement_complain_management.data.Repository.UpdateSeenStatRepository
import java.io.File

class UpdateSeenStatViewModel: ViewModel() {
    private var UpdateSeenStatMutableLiveData: MutableLiveData<Resource<UpdateSeenStatResponce>>? = null


    //Todo save new complain
    fun getUpdateSeenStatData(
        user_id:String,
        rq_trn_no:String
    ): MutableLiveData<Resource<UpdateSeenStatResponce>>? {

        UpdateSeenStatMutableLiveData =
            UpdateSeenStatRepository.getUpdateSeenStat(user_id,rq_trn_no)

        return UpdateSeenStatMutableLiveData

    }


}