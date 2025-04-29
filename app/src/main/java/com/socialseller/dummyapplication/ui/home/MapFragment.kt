package com.socialseller.dummyapplication.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.viewmodel.AddPlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _mapView: MapView? = null
    private val mapView get() = _mapView!!

    private val viewModel: AddPlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        _mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        setupMap()
        return view
    }

    private fun setupMap() {
        mapView.getMapAsync { googleMap ->
            viewModel.places.observe(viewLifecycleOwner) { places ->
                googleMap.clear()
                places.forEach { place ->
                    loadImageAsBitmap(place.imageUrl) { bitmap ->
                        val markerOptions = MarkerOptions()
                            .position(LatLng(place.latitude, place.longitude))
                            .title(place.name)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        googleMap.addMarker(markerOptions)
                    }
                }
            }
            viewModel.loadPlaces()
        }
    }

    private fun loadImageAsBitmap(url: String, callback: (Bitmap) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val input = URL(url).openStream()
                val bitmap = BitmapFactory.decodeStream(input)
                val resized = Bitmap.createScaledBitmap(bitmap, 100, 100, false) // Resize to thumbnail
                withContext(Dispatchers.Main) {
                    callback(resized)
                }
            } catch (e: Exception) {
                Log.e("MapFragment", "Image load failed: ${e.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _mapView = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
