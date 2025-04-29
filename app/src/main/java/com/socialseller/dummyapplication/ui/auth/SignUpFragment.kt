package com.socialseller.dummyapplication.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.socialseller.dummyapplication.R
import com.socialseller.dummyapplication.databinding.FragmentSignUpBinding
import com.socialseller.dummyapplication.ui.home.HomeActivity
import com.socialseller.dummyapplication.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    private var selectedImageUri: Uri? = null

    companion object {
        private const val IMAGE_MIME_TYPE = "image/*"
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        binding.fullNameEditText.setText("John Doe")
        binding.emailEditText.setText("abc@gmail.com")
        binding.passwordEditText.setText("Password@123")
        binding.confirmPasswordEditText.setText("Password@123")

        galleryImage.setOnClickListener { openGallery() }

        signupBtn.setOnClickListener {
            if (validateInputs()) {
                selectedImageUri?.let { imageUri ->
                    toggleLoading(true)
                    authViewModel.register(
                        email = emailEditText.text.toString(),
                        password = passwordEditText.text.toString(),
                        name = fullNameEditText.text.toString(),
                        imageUri = imageUri
                    )
                } ?: Toast.makeText(context, getString(R.string.select_image_prompt), Toast.LENGTH_SHORT).show()
            }
        }

        collectRegistrationResult()
    }

    private fun collectRegistrationResult() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.registerState.collect { result ->
                    toggleLoading(false)
                    result.onSuccess {
                        Toast.makeText(context, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    }.onFailure {
                        Toast.makeText(context, it.localizedMessage ?: getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun onImageSelected(uri: Uri) = with(binding) {
        selectedImageUri = uri
        profileImage.setImageURI(uri)
        profileImage.visibility = View.VISIBLE
        galleryImage.visibility = View.GONE
    }

    private fun navigateToHome() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()
    }

    private fun openGallery() {
        pickImageLauncher.launch(IMAGE_MIME_TYPE)
    }

    private fun toggleLoading(isLoading: Boolean) = with(binding) {
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        signupBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun validateInputs(): Boolean = with(binding) {
        val fullName = fullNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        return when {
            fullName.isBlank() -> {
                fullNameEditText.error = getString(R.string.error_name_required)
                false
            }

            fullName.length < 3 -> {
                fullNameEditText.error = getString(R.string.error_name_short)
                false
            }

            email.isBlank() -> {
                emailEditText.error = getString(R.string.error_email_required)
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailEditText.error = getString(R.string.error_email_invalid)
                false
            }

            password.isBlank() -> {
                passwordEditText.error = getString(R.string.error_password_required)
                false
            }

            password.length < 8 -> {
                passwordEditText.error = getString(R.string.error_password_short)
                false
            }

            !password.matches(Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*]).{8,}$")) -> {
                passwordEditText.error = getString(R.string.error_password_complexity)
                false
            }

            confirmPassword.isBlank() -> {
                confirmPasswordEditText.error = getString(R.string.error_confirm_required)
                false
            }

            password != confirmPassword -> {
                confirmPasswordEditText.error = getString(R.string.error_password_mismatch)
                false
            }

            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
