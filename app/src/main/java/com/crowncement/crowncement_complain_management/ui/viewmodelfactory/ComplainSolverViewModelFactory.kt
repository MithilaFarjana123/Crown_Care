package com.crowncement.crowncement_complain_management.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSolverViewModel

class ComplainSolverViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComplainSolverViewModel::class.java)) {
            return ComplainSolverViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")

    }

}