package com.example.dev_man.modules.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dev_man.R
import com.example.dev_man.databinding.ItemModuleBinding
import com.example.dev_man.modules.models.Module

interface ModulesActionListener {

    fun onModuleDetails(module: Module)
}

class ModulesListAdapter(
    private val actionListener: ModulesActionListener
) : RecyclerView.Adapter<ModulesListAdapter.ModulesViewHolder>(), View.OnClickListener {

    class ModulesViewHolder(
        val binding: ItemModuleBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var modules: List<Module> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val module = v.tag as Module
        when(v.id) {
            R.id.modules_list__temperature -> {
                // todo
            }
            else -> {
                actionListener.onModuleDetails(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModulesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemModuleBinding.inflate(inflater, parent, false)

        binding.modulesListDetails.setOnClickListener(this)
        binding.modulesListTemperature.setOnClickListener(this)

        return ModulesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModulesViewHolder, position: Int) {
        val module = modules[position]
        with(holder.binding) {

            modulesListDetails.tag = module
            modulesListTemperature.tag = module

            modulesListNameModule.text = module.nameModule
            modulesListTemperature.text = module.temperature.toString()
        }
    }

    override fun getItemCount(): Int = modules.size
}