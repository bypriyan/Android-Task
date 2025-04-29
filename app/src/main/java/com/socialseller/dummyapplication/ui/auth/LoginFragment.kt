package com.socialseller.dummyapplication.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.databinding.FragmentLoginBinding
import com.socialseller.dummyapplication.ui.home.HomeActivity
import com.socialseller.dummyapplication.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeLoginResult()
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            if (validateInput()) {
                toggleLoading(true)
                viewModel.login(
                    binding.phoneNumberET.text.toString().trim(),
                    binding.passwordEt.text.toString().trim()
                )
            }
        }
    }

    private fun observeLoginResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    toggleLoading(false)
                    result.onSuccess {
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    }.onFailure {
                        Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun toggleLoading(show: Boolean) {
        binding.progressbar.visibility = if (show) View.VISIBLE else View.GONE
        binding.loginBtn.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun navigateToHome() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val email = binding.phoneNumberET.text?.toString()?.trim().orEmpty()
        val password = binding.passwordEt.text?.toString()?.trim().orEmpty()

        with(binding.phoneNumberET) {
            error = when {
                email.isEmpty() -> {
                    isValid = false
                    "Email is required"
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    isValid = false
                    "Enter a valid email"
                }
                else -> null
            }
        }

        with(binding.passwordEt) {
            error = when {
                password.isEmpty() -> {
                    isValid = false
                    "Password is required"
                }
                password.length < 6 -> {
                    isValid = false
                    "Password must be at least 6 characters"
                }
                else -> null
            }
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
