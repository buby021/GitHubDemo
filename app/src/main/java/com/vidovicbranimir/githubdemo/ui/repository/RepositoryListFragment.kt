package com.vidovicbranimir.githubdemo.ui.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.databinding.FragmentRepositoryListBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class RepositoryListFragment :
    BaseFragment<RepositoryViewModel, FragmentRepositoryListBinding, GitRepositoryRepository>() {

    companion object {
        const val LAST_SEARCH_QUERY: String = "last_search_query"
        const val DEFAULT_QUERY: String = "android"
        val sortBy = listOf<String>("stars", "forks", "updated")
    }

    private lateinit var repoAdapter: RepoListAdapter
    private var searchJob: Job? = null
    private var desc = true
    private var order = "desc"

    private fun search(query: String, sort: String, order: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query, sort, order).collect {
                repoAdapter.submitData(it)
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        repoAdapter = RepoListAdapter({ repo, avatar, repoName, repoOwner ->
            openRepoDetails(
                repo,
                avatar,
                repoName,
                repoOwner
            )
        },
            { url -> openUserDetailsExtenaly(url) })
        val layoutManager = LinearLayoutManager(activity)
        layoutManager?.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = repoAdapter.withLoadStateHeaderAndFooter(
            header = RepoListLoadStateAdapter { repoAdapter.retry() },
            footer = RepoListLoadStateAdapter { repoAdapter.retry() }
        )
        repoAdapter.addLoadStateListener { loadState ->
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    activity,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }


        binding.retryButton.setOnClickListener {
            repoAdapter.retry()
        }

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        lifecycleScope.launch {
            repoAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
//                .collect { binding.recyclerView.scrollToPosition(0) }
        }


        binding.spinnerSort.adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, sortBy)
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sort = parent?.getItemAtPosition(position)
                search(binding.searchRepo.text.toString(), sort.toString(), order)
            }
        }

//        search(query, binding.spinnerSort.selectedItem.toString(), order)

        binding.sortDirection.setOnClickListener {
            it?.animate()?.rotationBy(180f)
            desc = !desc
            order = if (desc) "desc" else "asc"
            search(query, binding.spinnerSort.selectedItem.toString(), order)
        }

        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun openUserDetailsExtenaly(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(intent)
    }


    private fun updateRepoListFromInput() {
        binding.searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                binding.recyclerView.scrollToPosition(0)
                search(it.toString(), binding.spinnerSort.selectedItem.toString(), order)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text.trim().toString())
    }


    private fun openRepoDetails(
        repo: Repo,
        avatar: ImageView,
        repoName: TextView,
        repoOwner: TextView
    ) {
        val action =
            RepositoryListFragmentDirections.actionRepositoryListFragmentToRepositoryDetailsFragment(
                repository = repo
            )

        val p1 = Pair(avatar, repo.owner.avatar_url)
        val p2 = Pair(repoName, repo.owner.login)
        val p3 = Pair(repoOwner, repo.full_name)

        val extras = FragmentNavigatorExtras(p1, p2, p3)
        findNavController().navigate(action, extras)
    }

    override fun getViewModel() = RepositoryViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRepositoryListBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = GitRepositoryRepository(restClient.buildApi)


}