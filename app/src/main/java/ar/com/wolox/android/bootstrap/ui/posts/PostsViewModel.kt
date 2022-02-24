package ar.com.wolox.android.bootstrap.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.com.wolox.android.bootstrap.Constants
import ar.com.wolox.android.bootstrap.model.Post
import ar.com.wolox.android.bootstrap.repository.PostRepository
import ar.com.wolox.android.bootstrap.ui.base.BaseViewModel
import ar.com.wolox.android.bootstrap.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    fun getPosts(userId: Int) {
        viewModelScope.launch {
            toggleRequestStatus()
            val result = postsRepository.getPosts(userId)
            if (result.isSuccessful) {
                _posts.value = result.body()!!
                toggleRequestStatus()
            } else {
                onRequestFailed(result.code())
            }
        }
    }

    val isUserLogged: Boolean
        get() = sharedPreferencesManager[Constants.USER_IS_LOGGED_KEY, false]
}
