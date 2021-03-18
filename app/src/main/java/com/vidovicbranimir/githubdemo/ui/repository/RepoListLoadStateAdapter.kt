package com.vidovicbranimir.githubdemo.ui.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vidovicbranimir.githubdemo.utils.visible
import com.vidovicbranimir.githubdemo.databinding.LoadStateBinding

class RepoListLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RepoListLoadStateAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: LoadStateBinding, private val retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textViewError.text = loadState.error.localizedMessage
            }
            binding.progressbar.visible(loadState is LoadState.Loading)
            binding.buttonRetry.visible(loadState is LoadState.Error)
            binding.textViewError.visible(loadState is LoadState.Error)
            binding.buttonRetry.setOnClickListener {
                retry()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = ViewHolder(
        LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)
}