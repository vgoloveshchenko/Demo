package com.example.demo.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.demo.databinding.FragmentSettingsBinding
import com.example.demo.presentation.extension.applyHorizontalWindowInsets
import com.example.demo.presentation.extension.applyWindowInsets

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.applyWindowInsets()
            buttonNightMode.applyHorizontalWindowInsets()
            buttonLanguage.applyHorizontalWindowInsets()

            buttonNightMode.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toNightMode())
            }
            buttonLanguage.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toLanguage())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}