package com.example.dev_man.screens.navigation

import com.example.dev_man.modules.models.Module

interface Navigator {

    fun showDetails(module: Module)

    fun goBack()

    fun toast(messageRes: Int)
}