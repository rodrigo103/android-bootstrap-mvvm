package ar.com.wolox.android.bootstrap.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.wolox.android.bootstrap.Constants
import ar.com.wolox.android.bootstrap.model.Album
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.repository.AlbumRepository
import ar.com.wolox.android.bootstrap.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val albumsRepository: AlbumRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _requestStatus = MutableLiveData<RequestStatus>()
    val requestStatus: LiveData<RequestStatus>
        get() = _requestStatus

    private fun toggleRequestStatus() {
        _requestStatus.apply {
            value = if (value != RequestStatus.Loading) {
                RequestStatus.Loading
            } else {
                RequestStatus.Finished
            }
        }
    }

    private fun onRequestFailed(error: Int) {
        _requestStatus.value = RequestStatus.Failure(error)
    }

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>>
        get() = _albums

    fun getAlbums(userId: Int) {
        viewModelScope.launch {
            toggleRequestStatus()
            val result = albumsRepository.getAlbums(userId)
            if (result.isSuccessful) {
                _albums.value = result.body()!!
                toggleRequestStatus()
            } else {
                onRequestFailed(result.code())
            }
        }
    }

    val isUserLogged: Boolean
        get() = sharedPreferencesManager[Constants.USER_IS_LOGGED_KEY, false]
}
