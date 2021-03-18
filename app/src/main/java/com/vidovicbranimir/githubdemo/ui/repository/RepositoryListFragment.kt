package com.vidovicbranimir.githubdemo.ui.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.data.model.enums.SortOrder
import com.vidovicbranimir.githubdemo.data.model.enums.SortType
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.databinding.FragmentRepositoryListBinding
import com.vidovicbranimir.githubdemo.ui.base.BaseFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class RepositoryListFragment :
    BaseFragment<RepositoryViewModel, FragmentRepositoryListBinding, GitRepositoryRepository>() {

    companion object {
        const val LAST_SEARCH_QUERY: String = "last_search_query"
        const val DEFAULT_QUERY: String = "android"
    }

    private lateinit var repoAdapter: RepoListAdapter
    private var searchJob: Job? = null
    private var order = SortOrder.DESCENDING
    private var compositeDisposable = CompositeDisposable()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY

        initView(query)
        initRecyclerViewDebounce()

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.search.query.trim().toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_logout, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_logout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(query: String) {
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

        lifecycleScope.launch {
            repoAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
//                .collect { binding.recyclerView.scrollToPosition(0) }
        }


        binding.retryButton.setOnClickListener {
            repoAdapter.retry()
        }

        binding.spinnerSort.adapter =
            ArrayAdapter<SortType>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                SortType.values()
            )

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sort = parent?.getItemAtPosition(position)
                search(binding.search.query.trim().toString(), sort.toString(), order.direction)
            }
        }

//        search(query, binding.spinnerSort.selectedItem.toString(), order)

        binding.sortDirection.setOnClickListener {
            it?.animate()?.rotationBy(180f)
            order = order.getOpposite()
            search(query, binding.spinnerSort.selectedItem.toString(), order.direction)
        }
    }


    private fun initRecyclerViewDebounce() {
        repoAdapter.addLoadStateListener { loadState ->
//            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible =
                loadState.source.refresh is LoadState.Error && !binding.search.query.trim()
                    .toString().isNullOrEmpty()
            binding.emptyList.isVisible = repoAdapter.itemCount == 0
        }

        val observableQueryText: Observable<String> =
            Observable.create(object : ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>?) {
                    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (!emitter!!.isDisposed()) {
                                emitter.onNext(newText)
                            }
                            return false
                        }
                    })
                }

            }).debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())

        observableQueryText.subscribe(object : Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onNext(t: String?) {
                updateRepoListFromInput(t)
            }

            override fun onError(e: Throwable?) {
            }
        })
    }

    private fun search(query: String, sort: String, order: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query, sort, order).collect {
                repoAdapter.submitData(it)

                binding.recyclerView.scrollToPosition(0)
            }
        }
    }


    private fun openUserDetailsExtenaly(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(intent)
    }


    private fun updateRepoListFromInput(query: String?) {
        if (!query.isNullOrEmpty()) {
            search(query, binding.spinnerSort.selectedItem.toString(), order.direction)
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.retryButton.isVisible = false
                binding.emptyList.isVisible = true
            }
        }
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

    override fun getFragmentRepository(): GitRepositoryRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        return GitRepositoryRepository(restClient.buildApi(token), userPreferences)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}