package ar.com.wolox.android.bootstrap.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.ViewholderAlbumBinding
import ar.com.wolox.android.bootstrap.model.Album
import ar.com.wolox.android.bootstrap.ui.albums.AlbumsFragmentDirections

class AlbumsAdapter : ListAdapter<Album, AlbumViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewholderAlbumBinding.inflate(layoutInflater)
        return AlbumViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.binding.divider.visibility = if (isLastPosition(position)) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        holder.bind(getItem(position))
    }

    private fun isLastPosition(position: Int) = (itemCount - 1 == position)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
                oldItem == newItem
        }
    }
}

class AlbumViewHolder(
    val binding: ViewholderAlbumBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private fun navigateToAlbum(albumId: Int, view: View) {
        view.findNavController().navigate(
            AlbumsFragmentDirections.actionAlbumFragmentToPhotosFragment(albumId)
        )
    }

    fun bind(item: Album) {
        with(binding) {
            userId.text = context.getString(R.string.album_item_user_id, item.userId.toString())
            id.text = context.getString(R.string.album_item_id, item.id.toString())
            title.text = context.getString(R.string.album_item_title, item.title)
            root.setOnClickListener {
                navigateToAlbum(item.id, it)
            }
        }
    }
}
