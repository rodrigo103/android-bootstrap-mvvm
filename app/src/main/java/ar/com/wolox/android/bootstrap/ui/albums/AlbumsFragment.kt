package ar.com.wolox.android.bootstrap.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.bootstrap.Constants.INTERNAL_SERVER_ERROR_STATUS_CODE
import ar.com.wolox.android.bootstrap.Constants.NOT_FOUND_STATUS_CODE
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.FragmentAlbumsBinding
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.ui.adapter.AlbumsAdapter
import ar.com.wolox.android.bootstrap.ui.root.RootViewModel
import ar.com.wolox.android.bootstrap.utils.SnackbarFactory
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumsFragment : Fragment(), AlbumsAdapter.AlbumsInteractionListener {

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
        setListeners()
        setObservers()
    }

    private fun showLoading() {
        rootViewModel.showLoading()
    }

    private fun hideLoading() {
        rootViewModel.hideLoading()
    }

    private fun setListeners() {
//        binding.appBarLayout.addOnOffsetChangedListener(
//            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//                val scale = 1 + verticalOffset / appBarLayout.totalScrollRange.toFloat()
//                val balanceViewNewAlpha = 1 + verticalOffset / (appBarLayout.totalScrollRange.toFloat() /3)
//
//                binding.apply {
////                    textview.alpha = balanceViewNewAlpha
//                }
//            }
//        )
    }

    private fun setObservers() {
        viewModel.requestStatus.observe(viewLifecycleOwner) {
            when (it) {
                RequestStatus.Loading -> showLoading()
                is RequestStatus.Failure -> {
                    hideLoading()
//                    binding.albumsRecyclerView.visibility = View.GONE
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
//        SnackbarFactory.create(
//            binding.albumsRecyclerView,
//            getString(R.string.posts_error),
//            getString(R.string.ok)
//        )
    }

    private fun showEmptyListSnackbar() {
//        SnackbarFactory.create(
//            binding.albumsRecyclerView,
//            getString(R.string.no_posts_to_show),
//            getString(R.string.ok)
//        )
    }

    private fun getAlbums(userId: Int) {
        viewModel.apply {
            getAlbums(userId)
            albums.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
//                    binding.albumsRecyclerView.apply {
//                        adapter = AlbumsAdapter().apply {
//                            addAlbums(it)
//                            setInteractionListener(this@AlbumsFragment)
//                        }
//                        layoutManager = LinearLayoutManager(requireContext())
////                        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//                        isNestedScrollingEnabled = false
//                        isFocusable = false
//                        visibility = View.VISIBLE
//                    }
                } else {
//                    binding.albumsRecyclerView.visibility = View.GONE
                    showEmptyListSnackbar()
                }
            }
        }
    }

    override fun onAlbumItemClick(id: Int, view: View) {
        navigateToAlbum(id, view)
    }

    private fun navigateToAlbum(albumId: Int, view: View) {
        view.findNavController().navigate(
            AlbumsFragmentDirections.actionAlbumFragmentToPhotosFragment(albumId)
        )
    }
}
