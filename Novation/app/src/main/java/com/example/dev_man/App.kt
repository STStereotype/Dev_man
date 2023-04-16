package com.example.dev_man

import android.app.Application
import com.example.dev_man.modules.services.ModulesServices

class App: Application() {
    var modulesService = ModulesServices()
}