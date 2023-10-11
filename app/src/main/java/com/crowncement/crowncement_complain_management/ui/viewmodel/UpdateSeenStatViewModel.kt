package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.SaveResponce
import com.crowncement.crowncement_complain_management.data.Model.UpdateActionResponce
import com.crowncement.crowncement_complain_management.data.Model.UpdateSeenStatResponce
import com.crowncement.crowncement_complain_management.data.Repository.ComplainSaveRepository
import com.crowncement.crowncement_complain_management.data.Repository.UpdateSeenStatRepository
import java.io.File

class UpdateSeenStatViewModel: ViewModel() {
    private var UpdateSeenStatMutableLiveData: MutableLiveData<Resource<UpdateSeenStatResponce>>? = null

    private var UpdateActionMutableLiveData: MutableLiveData<Resource<UpdateActionResponce>>? = null



    //Todo save UpdateSeenStat
    fun getUpdateSeenStatData(
        user_id:String,
        rq_trn_no:String
    ): MutableLiveData<Resource<UpdateSeenStatResponce>>? {

        UpdateSeenStatMutableLiveData =
            UpdateSeenStatRepository.getUpdateSeenStat(user_id,rq_trn_no)

        return UpdateSeenStatMutableLiveData

    }


    //Todo save UpdateAction
    fun getUpdateActionData(
        user_id:String,
        rq_trn_no:String,
        rq_trn_row:String,
        feedback_date:String,
        feedback_det:String,
        solution_det:String,
        action_type:String
    ): MutableLiveData<Resource<UpdateActionResponce>>? {

        UpdateActionMutableLiveData =
            UpdateSeenStatRepository.getUpdateAction(user_id,rq_trn_no
                    ,rq_trn_row,feedback_date,feedback_det,solution_det,action_type
            )

        return UpdateActionMutableLiveData

    }




}