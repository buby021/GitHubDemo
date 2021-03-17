package com.vidovicbranimir.githubdemo.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vidovicbranimir.githubdemo.ui.main.MainActivity
import com.vidovicbranimir.githubdemo.data.handleApiError
import com.vidovicbranimir.githubdemo.data.network.ApiResult
import com.vidovicbranimir.githubdemo.data.repository.LoginRepository
import com.vidovicbranimir.githubdemo.databinding.FragmentLoginBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginRepository>() {

    private val clientId = "07f1a1a2948e882fa91f"
    private val clientSecret = "0ba762306e0f0e15bbd7f7c99df817c1ef9d4773"
    private val redirectUri = "githubdemo://callback"


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&scope=repo&redirect_uri=" + redirectUri)
            )
            startActivity(intent)
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResult.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value)
                        requireActivity().finish()
                        requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
                is ApiResult.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        val uri = activity?.intent?.data
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            val code = uri.getQueryParameter("code")
            if (code != null) {
                // get access token
                // we'll do that in a minute
                viewModel.getAuthToken(code)
            }
            Toast.makeText(activity, code.toString(), Toast.LENGTH_LONG)
        } else if (uri?.getQueryParameter("error") != null) {
            // show an error message here
            Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG)
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun getFragmentRepository() = LoginRepository(restClient.buildApi(), userPreferences)
}