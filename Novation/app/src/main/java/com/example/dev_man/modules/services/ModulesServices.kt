package com.example.dev_man.modules.services

import com.example.dev_man.ModuleNotFoundException
import com.example.dev_man.modules.models.Module
import com.example.dev_man.modules.tasks.SimpleTask
import com.example.dev_man.modules.tasks.Task
import java.util.concurrent.Callable

typealias ModulesListener = (modules: List<Module>) -> Unit

class ModulesServices {
    private var modules = mutableListOf<Module>()

    private val listeners = mutableSetOf<ModulesListener>()
    private val modulesApi = ModulesApi()

    private fun searchModules() {
        val accessPoint = modulesApi.host
        for (i in 1..255 step 5) {
            Thread {
                for (j in i..i + 3) {
                    val host = accessPoint + j
                    val module = modulesApi.searchModule(host)
                    if (module != null && modulesApi.host != host)
                        addModule(module)
                }
            }.start()
        }
    }

    private fun addModule(module: Module) : Task<Unit> = SimpleTask<Unit>(Callable{
        modules.add(module)
        notifyChanges()
    })

    fun loadModules(): Task<Unit> = SimpleTask<Unit>(Callable{
        if (modules.isNotEmpty())
            modules.clear()
        searchModules()
    })

    fun setTemperature(value: Float, module: Module?): Task<Unit> = SimpleTask<Unit>(Callable {
        modulesApi.setTemperature(module!!.ip, value)
    })

    fun getByIp(ip: String): Task<Module> = SimpleTask<Module>(Callable {
        return@Callable modules.firstOrNull() { it.ip == ip } ?: throw ModuleNotFoundException()
    })

    fun addListener(listener: ModulesListener) {
        listeners.add(listener)
        listener.invoke(modules)
    }

    fun removeListener(listener: ModulesListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(modules) }
    }
}