package ar.com.wolox.android.bootstrap.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.com.wolox.android.bootstrap.Constants.INTERNAL_SERVER_ERROR_STATUS_CODE
import ar.com.wolox.android.bootstrap.Constants.NOT_FOUND_STATUS_CODE
import ar.com.wolox.android.bootstrap.R
import ar.com.wolox.android.bootstrap.databinding.FragmentProfileBinding
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.ui.base.BaseFragment
import ar.com.wolox.android.bootstrap.utils.SnackbarFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel>() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isUserLogged) {
            // TODO: Is there a better way to always show the actionbar in a particular fragment?
            (requireActivity() as AppCompatActivity).supportActionBar?.show()

            viewModel.apply { getUser(loggedUserId) }
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
            findNavController().navigate(R.id.login_fragment)
        }
    }

    override fun setListeners() {
        binding.apply {
            albumsButton.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToAlbumFragment(viewModel.loggedUserId)
                )
            }

            todosButton.setOnClickListener {}

            postsButton.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToPostFragment(viewModel.loggedUserId)
                )
            }

            logoutButton.setOnClickListener {
                viewModel.logout()
                findNavController().navigate(
                    R.id.login_fragment
                )
            }
        }
    }

    override fun setObservers() {
        viewModel.apply {
            requestStatus.observe(viewLifecycleOwner) {
                if (it is RequestStatus.Failure) {
                    when (it.error) {
                        // Handle every possible error here
                        NOT_FOUND_STATUS_CODE -> showErrorSnackbar()
                        INTERNAL_SERVER_ERROR_STATUS_CODE -> showErrorSnackbar()
                        else -> showErrorSnackbar()
                    }
                }
            }

            user.observe(viewLifecycleOwner) {
                with(binding) {
                    userId.text = getString(R.string.profile_user_id, user.value?.userId.toString())
                    name.text = getString(R.string.profile_name, user.value?.name)
                    username.text = getString(R.string.profile_username, user.value?.username)
                    email.text = getString(R.string.profile_email, user.value?.email)
                    phone.text = getString(R.string.profile_phone, user.value?.phone)
                    website.text = getString(R.string.profile_website, user.value?.website)
                }
            }
        }
    }

    private fun showErrorSnackbar() {
        SnackbarFactory.create(
            binding.buttonsBar,
            getString(R.string.posts_error),
            getString(R.string.ok)
        )
    }

    private fun showEmptyListSnackbar() {
        SnackbarFactory.create(
            binding.buttonsBar,
            getString(R.string.no_posts_to_show),
            getString(R.string.ok)
        )
    }
}
