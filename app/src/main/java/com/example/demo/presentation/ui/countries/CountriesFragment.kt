package com.example.demo.presentation.ui.countries

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.demo.R
import com.example.demo.databinding.FragmentCountriesBinding
import com.example.demo.presentation.extension.doOnApplyWindowInsets
import com.example.demo.presentation.extension.hasPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<CountriesViewModel>()

    private var googleMap: GoogleMap? = null
    private var locationListener: LocationSource.OnLocationChangedListener? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isEnabled ->
        setLocationEnabled(isEnabled)
        if (isEnabled) {
            observeLocationChanges()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCountriesBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        initGoogleMap { map ->
            binding.map.doOnApplyWindowInsets { _, insets, _ ->
                val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                map.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    0
                )
                insets
            }

            map.setOnMapClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            viewModel
                .countriesFlow
                .onEach { countries ->
                    countries.forEach {
                        map.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        )
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        binding.bottomSheet.doOnApplyWindowInsets { bottomSheet, insets, _ ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomSheet.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = systemBarsInsets.left
                rightMargin = systemBarsInsets.right
                bottomMargin = systemBarsInsets.bottom
            }
            insets
        }

        viewModel
            .selectedCountryFlow
            .onEach {
                with(binding.bottomSheetContent) {
                    imageFlag.load(it.flagUrl)
                    textCity.text = it.capital
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.map.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.map.onDestroy()
        googleMap = null
        _binding = null
    }

    private fun initGoogleMap(action: (GoogleMap) -> Unit) {
        binding.map.getMapAsync { map ->
            googleMap = map.apply {
                initMapStyle()

                uiSettings.isCompassEnabled = true
                uiSettings.isZoomControlsEnabled = true

                setLocationSource(object : LocationSource {
                    override fun activate(listener: LocationSource.OnLocationChangedListener) {
                        locationListener = listener
                    }

                    override fun deactivate() {
                        locationListener = null
                    }
                })

                setOnMarkerClickListener {
                    viewModel.onMarkerClicked(it.position.latitude, it.position.longitude)
                    true
                }
            }

            val hasLocationPermission =
                requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            setLocationEnabled(hasLocationPermission)

            if (requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                observeLocationChanges()
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            action(map)
        }
    }

    private fun observeLocationChanges() {
        viewModel
            .startLocationFlow
            .onEach(::moveCameraToLocation)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel
            .locationFlow
            .onEach {
                locationListener?.onLocationChanged(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("MissingPermission")
    private fun setLocationEnabled(enabled: Boolean) {
        googleMap?.isMyLocationEnabled = enabled
        googleMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    private fun moveCameraToLocation(location: Location) {
        val current = LatLng(location.latitude, location.longitude)
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(current, DEFAULT_CAMERA_ZOOM)
        )
    }

    private fun GoogleMap.initMapStyle() {
        // https://developers.google.com/maps/documentation/android-sdk/styling
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_json)
            )
        }
    }

    companion object {
        private const val DEFAULT_CAMERA_ZOOM = 17f
    }
}