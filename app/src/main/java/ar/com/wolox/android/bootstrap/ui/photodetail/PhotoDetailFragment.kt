package ar.com.wolox.android.bootstrap.ui.photodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ar.com.wolox.android.bootstrap.Constants.INTERNAL_SERVER_ERROR_STATUS_CODE
import ar.com.wolox.android.bootstrap.Constants.NOT_FOUND_STATUS_CODE
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.FragmentPhotoDetailBinding
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.ui.photos.PhotoDetailViewModel
import ar.com.wolox.android.bootstrap.ui.root.RootViewModel
import ar.com.wolox.android.bootstrap.utils.SnackbarFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    private var _binding: FragmentPhotoDetailBinding? = null

    private val binding get() = _binding!!

    val viewModel: PhotoDetailViewModel by viewModels()
    private val rootViewModel: RootViewModel by activityViewModels()

    private val args: PhotoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPhotos(args.photoId)
        setObservers()
    }

    private fun showLoading() {
        rootViewModel.showLoading()
    }

    private fun hideLoading() {
        rootViewModel.hideLoading()
    }

    private fun setObservers() {
        viewModel.requestStatus.observe(viewLifecycleOwner) {
            when (it) {
                RequestStatus.Loading -> showLoading()
                is RequestStatus.Failure -> {
                    hideLoading()
                    when (it.error) {
                        // Handle every possible error here
                        NOT_FOUND_STATUS_CODE -> showErrorSnackbar()
                        INTERNAL_SERVER_ERROR_STATUS_CODE -> showErrorSnackbar()
                        else -> showErrorSnackbar()
                    }
                }
                else -> hideLoading()
            }
        }
    }

    private fun showErrorSnackbar() {
        SnackbarFactory.create(
            binding.title,
            getString(R.string.posts_error),
            getString(R.string.ok)
        )
    }

    private fun showEmptyListSnackbar() {
        SnackbarFactory.create(
            binding.title,
            getString(R.string.no_posts_to_show),
            getString(R.string.ok)
        )
    }

    private fun getPhotos(photoId: Int) {
        viewModel.apply {
            getPhoto(photoId)
            photo.observe(viewLifecycleOwner) {
                with(binding) {
                    albumId.text = it.albumId.toString()
                    id.text = it.id.toString()
                    title.text = it.title
                    Glide.with(image)
                        .load(
                            GlideUrl(
                                it.url,
                                LazyHeaders.Builder()
                                    .addHeader("User-Agent", "5")
                                    .build()
                            )
                        )
                        .placeholder(R.drawable.ic_android)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image)
                }
            }
        }
    }
}
