package com.socialseller.dummyapplication.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialseller.clothcrew.utility.GlideHelper
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.adapter.PagingLoadStateAdapter
import com.socialseller.dummyapplication.adapter.PlaceAdapter
import com.socialseller.dummyapplication.databinding.FragmentHomeBinding
import com.socialseller.dummyapplication.databinding.FragmentLoginBinding
import com.socialseller.dummyapplication.service.NetworkMonitor
import com.socialseller.dummyapplication.ui.auth.AuthActivity
import com.socialseller.dummyapplication.viewmodel.AddPlaceViewModel
import com.socialseller.dummyapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPlaceViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val adapter by lazy { PlaceAdapter() }
    private var networkMonitor: NetworkMonitor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePlaces()

        //observe user data
        observeUserData()
        observeErrors()

        viewModel.syncFromFirestore()

        binding.addPlaceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPlaceFragment)
        }
        binding.mapPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
        }

        binding.logoutBtn.setOnClickListener {
            logoutUser()
        }
    }

    private fun setupRecyclerView() = with(binding.recyclearPlaces) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@HomeFragment.adapter
    }

    private fun observePlaces() {
        viewModel.places.observe(viewLifecycleOwner) { places ->
            Log.d("places", "observePlaces: $places")
            adapter.submitList(places)
        }
    }
    private fun observeUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.user.collect { user ->
                    user?.let {
                        GlideHelper.loadImage(binding.profileImage, it.profileImageUrl)
                    }
                }
            }
        }
    }

    private fun observeErrors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.error.collect { errorMsg ->
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        networkMonitor = NetworkMonitor(requireContext()) {
            Handler(Looper.getMainLooper()).post {
                viewModel.syncFromFirestore()
            }
        }

    }

    private fun logoutUser() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                userViewModel.logout()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onStop() {
        super.onStop()
        networkMonitor?.unregister()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
