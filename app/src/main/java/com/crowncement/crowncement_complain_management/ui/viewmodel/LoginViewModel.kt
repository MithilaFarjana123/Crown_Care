package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.UserInfo
import com.crowncement.crowncement_complain_management.data.Repository.LoginRepository


class LoginViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<UserInfo>>? = null

    init {
      //  mutableLiveData = LoginRepository.getUserInfo()
    }

    fun getUserInfo(id:String,pass:String,deviceId:String,token:String): MutableLiveData<Resource<UserInfo>> {

        mutableLiveData = LoginRepository.getUserInfo(id, pass, deviceId,token)

        return mutableLiveData!!
    }

}