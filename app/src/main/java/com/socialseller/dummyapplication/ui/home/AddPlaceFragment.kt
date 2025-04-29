package com.socialseller.dummyapplication.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.databinding.FragmentAddPlaceBinding
import com.socialseller.dummyapplication.databinding.FragmentHomeBinding
import com.socialseller.dummyapplication.viewmodel.AddPlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPlaceFragment : Fragment() {

    private var _binding: FragmentAddPlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var selectedLatLng: LatLng? = null
    private var imageUri: Uri? = null
    private var cameraImageUri: Uri? = null

    private val viewModel: AddPlaceViewModel by viewModels()

    // Handle Gallery
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            binding.imageView.setImageURI(it)
        }
    }

    // Handle Camera
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri != null) {
            imageUri = cameraImageUri
            binding.imageView.setImageURI(cameraImageUri)
        }
    }

    // Handle Permissions
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            showImagePickerDialog()
        } else {
            Toast.makeText(requireContext(), "Please allow all permissions", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        setupMap()
        setupClickListeners()
        observeResult()

        return binding.root
    }

    private fun setupMap() {
        mapView.getMapAsync { map ->
            map.setOnMapClickListener { latLng ->
                selectedLatLng = latLng
                map.clear()
                map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnPickImage.setOnClickListener {
            checkAndRequestPermissions()
        }

        binding.submitBtn.setOnClickListener {
            val name = binding.etPlaceName.text.toString().trim()
            val desc = binding.etPlaceDesc.text.toString().trim()

            if (name.isNotEmpty() && desc.isNotEmpty() && selectedLatLng != null && imageUri != null) {
                toggleLoading(true)
                viewModel.uploadPlace(imageUri!!, name, desc, selectedLatLng!!)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeResult() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collectLatest { result ->
                    toggleLoading(false)
                    if (result.isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Place added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        clearForm()
                    } else {
                        Log.d("ere", "observeResult: ${result.exceptionOrNull()?.message}")
                        Toast.makeText(
                            requireContext(),
                            "Failed: ${result.exceptionOrNull()?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun clearForm() {
        binding.etPlaceName.text?.clear()
        binding.etPlaceDesc.text?.clear()
        binding.imageView.setImageDrawable(null)
        mapView.getMapAsync { it.clear() }
        selectedLatLng = null
        imageUri = null
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        permissions.add(Manifest.permission.CAMERA)

        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image From")
            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
                when (which) {
                    0 -> launchCamera()
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun launchCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Picture")
            put(MediaStore.Images.Media.DESCRIPTION, "Captured from Camera")
        }

        val resolver = requireContext().contentResolver
        cameraImageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (cameraImageUri != null) {
            cameraLauncher.launch(cameraImageUri)
        } else {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleLoading(show: Boolean) {
        binding.progressbar.visibility = if (show) View.VISIBLE else View.GONE
        binding.submitBtn.visibility = if (show) View.GONE else View.VISIBLE
    }

    // MapView Lifecycle
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
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
