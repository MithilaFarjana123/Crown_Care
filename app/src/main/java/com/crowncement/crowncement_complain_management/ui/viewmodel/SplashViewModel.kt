package com.crowncement.crowncement_complain_management.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crowncement.crowncement_complain_management.common.Resource
import com.crowncement.crowncement_complain_management.data.Model.AppVersionResponse
import com.crowncement.crowncement_complain_management.data.Repository.SplashRepository


class SplashViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<AppVersionResponse>>? = null

    fun getAppVersion(
        appName: String,
        dateTime: String
    ): MutableLiveData<Resource<AppVersionResponse>>? {

        mutableLiveData = SplashRepository.getUserInfo(appName, dateTime)

        return mutableLiveData
    }

}