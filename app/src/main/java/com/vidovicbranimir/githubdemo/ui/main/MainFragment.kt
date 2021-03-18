package com.vidovicbranimir.githubdemo.ui.main

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.data.network.ApiResult

import com.vidovicbranimir.githubdemo.data.repository.MainRepository
import com.vidovicbranimir.githubdemo.utils.startNewActivity
import com.vidovicbranimir.githubdemo.utils.visible
import com.vidovicbranimir.githubdemo.databinding.FragmentMainBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import com.vidovicbranimir.githubdemo.ui.login.LoginActivity
import com.vidovicbranimir.githubdemo.ui.repository.RepositoryListActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding, MainRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initBinding()
        initObservers()
    }


    private fun initBinding() {
        binding.loginName.visible(false)
        binding.animationView.speed = 2.5f

        var authToken: String? = null
        lifecycleScope.launch {
            authToken = userPreferences.authToken.first()
        }
        if (authToken.isNullOrEmpty()) {
            requireActivity().startNewActivity(LoginActivity::class.java)
        } else {
            viewModel.getUser()
            binding.animationView.playAnimation()
        }

        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                lifecycleScope.launch {
                    val activity =
                        if (authToken.isNullOrEmpty()
                        ) LoginActivity::class.java else RepositoryListActivity::class.java
                    requireActivity().startNewActivity(activity)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    private fun initObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResult.Success -> {
                    animate()
                    binding.loginName.text = it.value.login
                }
                is ApiResult.Failure -> {
                }
            }
        })
    }


    private fun animate() {
        binding.loginName.visible(true)
        binding.loginName.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.animationView.playAnimation()
    }


    override fun getViewModel() = MainViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): MainRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        return MainRepository(restClient.buildApi(token))
    }
}