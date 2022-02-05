package ar.com.wolox.android.bootstrap.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.wolox.android.bootstrap.Constants
import ar.com.wolox.android.bootstrap.model.User
import ar.com.wolox.android.bootstrap.network.util.RequestStatus
import ar.com.wolox.android.bootstrap.repository.UserRepository
import ar.com.wolox.android.bootstrap.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val usersRepository: UserRepository,
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

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUser(userId: Int) {
        viewModelScope.launch {
            toggleRequestStatus()
            val result = usersRepository.getUser(userId)
            if (result.isSuccessful) {
                _user.value = result.body()!!
                toggleRequestStatus()
            } else {
                onRequestFailed(result.code())
            }
        }
    }

    fun logout() {
        sharedPreferencesManager.store(Constants.USER_IS_LOGGED_KEY, false)
        sharedPreferencesManager.store(Constants.USER_ID_KEY, 0)
    }

    val isUserLogged: Boolean
        get() = sharedPreferencesManager[Constants.USER_IS_LOGGED_KEY, false]

    val loggedUserId: Int
        get() = sharedPreferencesManager[Constants.USER_ID_KEY, 0]
}
