package com.example.dev_man.screens.modules_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dev_man.modules.models.Module
import com.example.dev_man.modules.services.ModulesListener
import com.example.dev_man.modules.services.ModulesServices
import com.example.dev_man.modules.tasks.*
import com.example.dev_man.screens.BaseViewModel

class ModuleListViewModel(
    private val modulesServices: ModulesServices
) : BaseViewModel() {

    private var _modules = MutableLiveData<Result<List<Module>>>()
    val modules: LiveData<Result<List<Module>>> = _modules

    private var modulesResult: Result<List<Module>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }
    private val listener: ModulesListener = {
        modulesResult = if(it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessResult(it)
        }
    }

    init {
        modulesServices.addListener(listener)
        loadModules()
    }

    override fun onCleared() {
        super.onCleared()
        modulesServices.removeListener(listener)
    }

    private fun loadModules() {
        modulesResult = PendingResult()
        modulesServices.loadModules()
            .onError {
                modulesResult = ErrorResult(it)
            }.autoCancel()
    }

    private fun notifyUpdates() {
        _modules.postValue(modulesResult.map { modules ->
            modules.map { module -> module }
        })
    }
}