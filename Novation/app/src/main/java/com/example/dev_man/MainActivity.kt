package com.example.dev_man

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dev_man.databinding.ActivityMainBinding
import com.example.dev_man.modules.models.Module
import com.example.dev_man.screens.module_detail.ModuleDetailsFragment
import com.example.dev_man.screens.modules_list.ModuleListFragment
import com.example.dev_man.screens.navigation.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.activity_main__people).setOnClickListener {
            if (savedInstanceState == null && supportFragmentManager.backStackEntryCount == 0) {
                supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.activity_main__fragment_container, ModuleListFragment())
                    .commit()
            }
        }
    }

    override fun showDetails(module: Module) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.activity_main__fragment_container, ModuleDetailsFragment.newInstance(module.ip))
            .commit()
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_LONG).show()
    }
}
