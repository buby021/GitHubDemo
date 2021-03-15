package com.vidovicbranimir.githubdemo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.vidovicbranimir.githubdemo.data.network.RestClient
import com.vidovicbranimir.githubdemo.data.repository.BaseRepository

abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {

    //    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val restClient = RestClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
//        lifecycleScope.launch { userPreferences.authToken.first() }
        postponeEnterTransition()
        return binding.root
    }

    //   fun logout() = lifecycleScope.launch{
    //      val authToken = userPreferences.authToken.first()
    //       val api = RestClient.buildApi
    //       viewModel.logout(api)
    //       userPreferences.clear()
    //       requireActivity().startNewActivity(AuthActivity::class.java)
    //   }


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}