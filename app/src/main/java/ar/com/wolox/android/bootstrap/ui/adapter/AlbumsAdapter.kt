package ar.com.wolox.android.bootstrap.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.ViewholderAlbumBinding
import ar.com.wolox.android.bootstrap.model.Album

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    interface AlbumsInteractionListener {
        fun onAlbumItemClick(id: Int, view: View)
    }

    var listener: AlbumsInteractionListener? = null

    private var albums = SortedList<Album>(
        Album::class.java,
        object : SortedListAdapterCallback<Album>(this) {
            override fun compare(o1: Album?, o2: Album?): Int {
                return 0
            }

            override fun areContentsTheSame(oldItem: Album?, newItem: Album?): Boolean {
                return false
            }

            override fun areItemsTheSame(item1: Album?, item2: Album?): Boolean {
                return item1 == item2
            }
        }
    )

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
        holder.bind(albums[position])
//        holder.itemView.setOnClickListener { listener(album) }
    }

    private fun isLastPosition(position: Int) = (itemCount - 1 == position)

    override fun getItemCount(): Int {
        return albums.size()
    }

    fun addAlbums(albums: List<Album>) {
        this.albums.addAll(albums)
    }

    fun setInteractionListener(listener: AlbumsInteractionListener) {
        this.listener = listener
    }

    inner class AlbumViewHolder(
        val binding: ViewholderAlbumBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Album) {
            with(binding) {
                userId.text = context.getString(R.string.album_item_user_id, item.userId.toString())
                id.text = context.getString(R.string.album_item_id, item.id.toString())
                title.text = context.getString(R.string.album_item_title, item.title)
                root.setOnClickListener {
                    listener?.onAlbumItemClick(item.id, it)
                }
            }
        }
    }
}
