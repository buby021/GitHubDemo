package com.vidovicbranimir.githubdemo.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.ui.main.MainActivity
import com.vidovicbranimir.githubdemo.utils.handleApiError
import com.vidovicbranimir.githubdemo.data.network.ApiResult
import com.vidovicbranimir.githubdemo.data.repository.LoginRepository
import com.vidovicbranimir.githubdemo.databinding.FragmentLoginBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import com.vidovicbranimir.githubdemo.utils.Constants
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + Constants.CLIENT_ID + "&scope=repo&redirect_uri=" + Constants.REDIRECT_URI)
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
        val uri = activity?.intent?.data
        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            val code = uri.getQueryParameter(Constants.CODE)
            if (code != null) {
                viewModel.getAuthToken(code)
            }
            Toast.makeText(activity, code.toString(), Toast.LENGTH_LONG)
        } else if (uri?.getQueryParameter("error") != null) {
            // show an error message here
            Toast.makeText(activity, getString(R.string.error), Toast.LENGTH_LONG)
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getViewModel() = LoginViewModel::class.java

    override fun getFragmentRepository() = LoginRepository(restClient.buildApi(), userPreferences)
}