package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.GetActivity4AppResponse
import com.crowncement.crowncement_complain_management.data.Repository.ComplainSolverRepository

class ComplainSolverViewModel: ViewModel() {

    private var getComForSolvertMutableLiveData: MutableLiveData<Resource<GetActivity4AppResponse>>? = null



    fun getComSolverData(
        user_id:String
    ): MutableLiveData<Resource<GetActivity4AppResponse>>? {

        getComForSolvertMutableLiveData = ComplainSolverRepository.getComplain4Solver(user_id)

        return getComForSolvertMutableLiveData
    }
}