package com.example.dev_man.screens.modules_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dev_man.databinding.FragmentModulesListBinding
import com.example.dev_man.modules.adapter.ModulesActionListener
import com.example.dev_man.modules.adapter.ModulesListAdapter
import com.example.dev_man.modules.models.Module
import com.example.dev_man.modules.tasks.EmptyResult
import com.example.dev_man.modules.tasks.ErrorResult
import com.example.dev_man.modules.tasks.PendingResult
import com.example.dev_man.modules.tasks.SuccessResult
import com.example.dev_man.screens.factory
import com.example.dev_man.screens.navigator

class ModuleListFragment : Fragment() {
    private lateinit var binding: FragmentModulesListBinding
    private lateinit var adapter: ModulesListAdapter

    private val viewModel: ModuleListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulesListBinding.inflate(inflater, container, false)
        adapter = ModulesListAdapter(object : ModulesActionListener {
            override fun onModuleDetails(module: Module) {
                navigator().showDetails(module)
            }
        })

        viewModel.modules.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is SuccessResult -> {
                    binding.fragmentModulesListRecycler.visibility = View.VISIBLE
                    adapter.modules = it.data
                    println("123123123123123123")
                }
                is ErrorResult -> {
                    binding.fragmentModulesNoModules.visibility = View.VISIBLE
                }
                is PendingResult -> {
                    binding.fragmentModulesNoModules.visibility = View.VISIBLE
                }
                is EmptyResult -> {
                    binding.fragmentModulesNoModules.visibility = View.VISIBLE
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentModulesListRecycler.layoutManager = layoutManager
        binding.fragmentModulesListRecycler.adapter = adapter

        clickListener()

        return binding.root
    }

    private fun hideAll() {
        binding.fragmentModulesListRecycler.visibility = View.INVISIBLE
        binding.fragmentModulesNoModules.visibility = View.INVISIBLE
    }

    private fun clickListener() {
        binding.fragmentModulesArrowBack.setOnClickListener {
            navigator().goBack()
        }

        binding.fragmentModulesExit.setOnClickListener {
            navigator().goBack()
        }
    }
}