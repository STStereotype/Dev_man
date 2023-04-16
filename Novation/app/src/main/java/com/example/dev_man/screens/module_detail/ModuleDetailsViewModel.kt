package com.example.dev_man.screens.module_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dev_man.modules.models.Module
import com.example.dev_man.modules.services.ModulesServices
import com.example.dev_man.screens.BaseViewModel

class ModuleDetailsViewModel (
    private val modulesServices: ModulesServices
) : BaseViewModel() {

    private val _moduleDetails = MutableLiveData<Module>()
    val moduleDetails: LiveData<Module> = _moduleDetails

    fun loadModule(ip: String) {
        if(_moduleDetails.value != null) return
        modulesServices.getByIp(ip)
            .onSuccess {
                _moduleDetails.value = it
            }
            .onError {
                // todo
            }
            .autoCancel()
    }

    fun setTemperature(value: Float) {
        modulesServices.setTemperature(value, null)

    }
}