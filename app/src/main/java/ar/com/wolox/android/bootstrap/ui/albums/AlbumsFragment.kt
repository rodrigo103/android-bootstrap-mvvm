package ar.com.wolox.android.bootstrap.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.wolox.android.bootstrap.Constants.INTERNAL_SERVER_ERROR_STATUS_CODE
import ar.com.wolox.android.bootstrap.Constants.NOT_FOUND_STATUS_CODE
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.FragmentAlbumsBinding
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.ui.adapter.AlbumsAdapter
import ar.com.wolox.android.bootstrap.ui.root.RootViewModel
import ar.com.wolox.android.bootstrap.utils.SnackbarFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null

    private val binding get() = _binding!!

    val viewModel: AlbumsViewModel by viewModels()
    private val rootViewModel: RootViewModel by activityViewModels()

    private val args: AlbumsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getAlbums(args.userId)
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
                    binding.albumsRecyclerView.visibility = View.GONE
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
            binding.albumsRecyclerView,
            getString(R.string.posts_error),
            getString(R.string.ok)
        )
    }

    private fun showEmptyListSnackbar() {
        SnackbarFactory.create(
            binding.albumsRecyclerView,
            getString(R.string.no_posts_to_show),
            getString(R.string.ok)
        )
    }

    private fun getAlbums(userId: Int) {
        viewModel.apply {
            getAlbums(userId)
            albums.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.albumsRecyclerView.apply {
                        adapter = AlbumsAdapter().apply {
                            submitList(it)
                        }
                        layoutManager = LinearLayoutManager(requireContext())
                        isNestedScrollingEnabled = false
                        isFocusable = false
                        visibility = View.VISIBLE
                    }
                } else {
                    binding.albumsRecyclerView.visibility = View.GONE
                    showEmptyListSnackbar()
                }
            }
        }
    }
}
