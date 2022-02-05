package ar.com.wolox.android.bootstrap.ui.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.wolox.android.bootstrap.model.Photo
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val albumsRepository: AlbumRepository,
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

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    fun getPhoto(photoId: Int) {
        viewModelScope.launch {
            toggleRequestStatus()
            val result = albumsRepository.getPhoto(photoId)
            if (result.isSuccessful) {
                _photo.value = result.body()!!
                toggleRequestStatus()
            } else {
                onRequestFailed(result.code())
            }
        }
    }
}
