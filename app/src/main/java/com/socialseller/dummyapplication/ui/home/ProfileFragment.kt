package com.socialseller.dummyapplication.ui.home

import android.os.Bundle
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
import com.socialseller.clothcrew.utility.GlideHelper
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.databinding.FragmentAddPlaceBinding
import com.socialseller.dummyapplication.databinding.FragmentHomeBinding
import com.socialseller.dummyapplication.databinding.FragmentProfileBinding
import com.socialseller.dummyapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe user data
        observeUserData()
        observeErrors()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun observeUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.user.collect { user ->
                    user?.let {
                        GlideHelper.loadImage(binding.profileImage, it.profileImageUrl)
                        binding.userName.text = it.name
                        binding.userEmail.text = it.email
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}