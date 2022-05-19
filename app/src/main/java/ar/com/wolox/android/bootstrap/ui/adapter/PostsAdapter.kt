package ar.com.wolox.android.bootstrap.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.bootstrap.databinding.ViewholderPostBinding
import ar.com.wolox.android.bootstrap.model.Post
import ar.com.wolox.android.bootstrap.utils.extensions.removeLineBreaks

class PostsAdapter : ListAdapter<Post, PostViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewholderPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.divider.visibility = if (isLastPosition(position)) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }

        holder.bind(getItem(position))
    }

    private fun isLastPosition(position: Int) = (itemCount - 1 == position)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }
}

class PostViewHolder(
    val binding: ViewholderPostBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Post) {
        with(binding) {
            title.text = item.title
            body.text = item.body.removeLineBreaks()
        }
    }
}
