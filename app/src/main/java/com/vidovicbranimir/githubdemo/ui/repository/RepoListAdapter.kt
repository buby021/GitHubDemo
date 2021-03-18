package com.vidovicbranimir.githubdemo.ui.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vidovicbranimir.githubdemo.utils.loadImage
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.databinding.ListRepoItemBinding
import com.vidovicbranimir.githubdemo.enums.versionType


class RepoListAdapter(
    private val itemClick: (Repo, ImageView, TextView, TextView) -> Unit,
    private val avatarClick: (String) -> Unit
) :
    PagingDataAdapter<Repo, RepoListAdapter.RepoViewHolder>(RepoComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            ListRepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindRepo(it) }
    }


    inner class RepoViewHolder(private val binding: ListRepoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindRepo(item: Repo) = with(binding) {
            avatar.loadImage(item.owner.avatar_url)
            avatar.transitionName = item.owner.avatar_url

            ownerName.text = item.owner.login
            ownerName.transitionName = item.owner.login

            repositoryName.text = item.name
            repositoryName.transitionName = item.full_name

            fork.text = item.forks_count.toString()
            watcher.text = item.watchers_count.toString()
            issues.text = item.open_issues_count.toString()

            if (versionType.type == versionType.VersionType.PAID) {
                avatar.setOnClickListener { avatarClick(item.owner.html_url) }
                itemView.setOnClickListener { itemClick(item, avatar, ownerName, repositoryName) }
            }
        }
    }
}

object RepoComparator : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }

}
