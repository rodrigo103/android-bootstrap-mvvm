package ar.com.wolox.android.bootstrap.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.bootstrap.databinding.ViewholderPhotoBinding
import ar.com.wolox.android.bootstrap.model.Photo
import ar.com.wolox.android.bootstrap.ui.photos.PhotosFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PhotosAdapter : ListAdapter<Photo, PhotoViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ViewholderPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.divider.visibility = if (isLastPosition(position)) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }

        holder.bind(getItem(position))
    }

    private fun isLastPosition(position: Int) = (itemCount - 1 == position)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }
}

class PhotoViewHolder(
    val binding: ViewholderPhotoBinding
) : RecyclerView.ViewHolder(binding.root) {

    private fun navigateToPhoto(photoId: Int, view: View) {
        view.findNavController().navigate(
            PhotosFragmentDirections.actionPhotosFragmentToPhotoDetailFragment(photoId)
        )
    }

    fun bind(item: Photo) {
        with(binding) {
            albumId.text = item.albumId.toString()
            id.text = item.id.toString()
            title.text = item.title

            Glide.with(image)
                .load(
                    GlideUrl(
                        item.thumbnailUrl,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "5")
                            .build()
                    )
                )
                .dontAnimate()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)

            root.setOnClickListener {
                navigateToPhoto(item.id, it)
            }
        }
    }
}
