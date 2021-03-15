package com.vidovicbranimir.githubdemo.ui.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.data.Format
import com.vidovicbranimir.githubdemo.data.loadImage
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.databinding.FragmentRepositoryDetailsBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import java.util.*

class RepositoryDetailsFragment :
    BaseFragment<RepositoryViewModel, FragmentRepositoryDetailsBinding, GitRepositoryRepository>() {

    private lateinit var repo: Repo

    companion object {
        const val REPOSITORY = "repository"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        arguments?.let {
            repo = it.getSerializable(REPOSITORY) as Repo
            with(binding) {
                avatar.loadImage(repo.owner.avatar_url)
                ownerName.text = repo.owner.login
                repositoryName.text = repo.name
                fork.text = repo.forks_count.toString()
                watcher.text = repo.watchers_count.toString()
                issues.text = repo.open_issues_count.toString()
                language.text = repo.language
                dateCreated.text = Format.asDateString(repo.created_at)
                ivRepositoryExternal.setOnClickListener {
                    openExternalUrl(repo.html_url)
                }
                ivOwnerExternal.setOnClickListener {
                    openExternalUrl(repo.owner.html_url)
                }
                description.text = getString(R.string.lorem_ipsum)
            }

            val animation = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)

            sharedElementEnterTransition = animation
            sharedElementReturnTransition = animation

            startPostponedEnterTransition()

            binding.avatar.transitionName = repo.owner.avatar_url
            binding.ownerName.transitionName = repo.owner.login
            binding.repositoryName.transitionName = repo.full_name
        }
    }

    private fun openExternalUrl(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(intent)
    }


    override fun getViewModel() = RepositoryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRepositoryDetailsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = GitRepositoryRepository(restClient.buildApi)


}