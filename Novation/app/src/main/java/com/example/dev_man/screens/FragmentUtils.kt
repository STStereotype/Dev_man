package com.example.dev_man.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dev_man.App
import com.example.dev_man.screens.module_detail.ModuleDetailsViewModel
import com.example.dev_man.screens.navigation.Navigator
import com.example.dev_man.screens.modules_list.ModuleListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            ModuleListViewModel::class.java -> {
                ModuleListViewModel(app.modulesService)
            }
            ModuleDetailsViewModel::class.java -> {
                ModuleDetailsViewModel(app.modulesService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator
