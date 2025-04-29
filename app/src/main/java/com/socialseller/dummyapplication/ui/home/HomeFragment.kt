package com.socialseller.dummyapplication.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.adapter.PagingLoadStateAdapter
import com.socialseller.dummyapplication.adapter.PlaceAdapter
import com.socialseller.dummyapplication.databinding.FragmentHomeBinding
import com.socialseller.dummyapplication.databinding.FragmentLoginBinding
import com.socialseller.dummyapplication.service.NetworkMonitor
import com.socialseller.dummyapplication.viewmodel.AddPlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPlaceViewModel by viewModels()
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

        viewModel.syncFromFirestore()

        binding.addPlaceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPlaceFragment)
        }
        binding.mapPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment)

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

    override fun onStart() {
        super.onStart()
        networkMonitor = NetworkMonitor(requireContext()) {
            Handler(Looper.getMainLooper()).post {
                viewModel.syncFromFirestore()
            }
        }

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
