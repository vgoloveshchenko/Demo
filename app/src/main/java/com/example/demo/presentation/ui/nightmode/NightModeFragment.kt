package com.example.demo.presentation.ui.nightmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.demo.databinding.FragmentNightModeBinding
import com.example.demo.domain.model.settings.NightMode
import com.example.demo.presentation.extension.applyHorizontalWindowInsets
import com.example.demo.presentation.extension.applyWindowInsets
import org.koin.androidx.viewmodel.ext.android.viewModel

class NightModeFragment : Fragment() {

    private var _binding: FragmentNightModeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<NightModeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNightModeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.applyWindowInsets()
            toolbar.setupWithNavController(findNavController())
            radioGroup.applyHorizontalWindowInsets()

            when (viewModel.selectedNightMode) {
                NightMode.DARK -> buttonDarkMode
                NightMode.LIGHT -> buttonLightMode
                NightMode.SYSTEM -> buttonSystemMode
            }.isChecked = true

            radioGroup.setOnCheckedChangeListener { _, buttonId ->
                val (prefsMode, systemMode) = when (buttonId) {
                    buttonDarkMode.id -> NightMode.DARK to AppCompatDelegate.MODE_NIGHT_YES
                    buttonLightMode.id -> NightMode.LIGHT to AppCompatDelegate.MODE_NIGHT_NO
                    buttonSystemMode.id -> NightMode.SYSTEM to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else -> error("incorrect buttonId $buttonId")
                }

                viewModel.selectedNightMode = prefsMode
                AppCompatDelegate.setDefaultNightMode(systemMode)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}