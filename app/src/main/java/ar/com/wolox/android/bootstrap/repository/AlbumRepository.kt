package ar.com.wolox.android.bootstrap.repository

import ar.com.wolox.android.bootstrap.network.service.AlbumService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepository @Inject constructor(private val albumsService: AlbumService) {

    suspend fun getAlbums() = withContext(Dispatchers.IO) {
        albumsService.getAlbums()
    }

    suspend fun getAlbums(userId: Int) = withContext(Dispatchers.IO) {
        albumsService.getAlbums(userId)
    }

    suspend fun getPhotos(albumId: Int) = withContext(Dispatchers.IO) {
        albumsService.getPhotos(albumId)
    }

    suspend fun getPhoto(photoId: Int) = withContext(Dispatchers.IO) {
        albumsService.getPhoto(photoId)
    }
}
