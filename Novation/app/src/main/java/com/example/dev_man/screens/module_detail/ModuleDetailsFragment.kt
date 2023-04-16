package com.example.dev_man.screens.module_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dev_man.databinding.FragmentModuleDetailsBinding
import com.example.dev_man.screens.factory
import com.example.dev_man.screens.navigator

class ModuleDetailsFragment : Fragment() {
    private lateinit var binding: FragmentModuleDetailsBinding
    private val viewModel: ModuleDetailsViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getString(ARG_MODULE_IP)?.let { viewModel.loadModule(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModuleDetailsBinding.inflate(layoutInflater, container, false)
        viewModel.moduleDetails.observe(viewLifecycleOwner, Observer {
            binding.fragmentDetailsTemperatureValue.text = it.temperature.toString()
            binding.fragmentDetailsBatteryChargeValue.text = it.batteryLevel.toString()
            binding.fragmentDetailsIpValue.text = it.ip
            binding.fragmentDetailsRouterValue.text = it.host
            binding.fragmentDetailsSignalLvlValue.text = "Олично"
            binding.fragmentDetailsSubnetMaskValue.text = it.mask
        })

        clickListener()

        return binding.root
    }

    private fun clickListener() {
        binding.fragmentDetailsTemperatureCell.setOnClickListener {
            viewModel.setTemperature(0.0f)
            // todo
        }

        binding.fragmentDetailsArrowBack.setOnClickListener {
            navigator().goBack()
        }

        binding.fragmentDetailsExit.setOnClickListener {
            navigator().goBack()
        }
    }
    companion object {

        private const val ARG_MODULE_IP = "ARG_MODULE_IP"

        fun newInstance(ip: String) : ModuleDetailsFragment {
            val fragment = ModuleDetailsFragment()
            fragment.arguments = bundleOf(ARG_MODULE_IP to ip)
            return fragment
        }
    }
}