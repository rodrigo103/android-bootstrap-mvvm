package ar.com.wolox.android.bootstrap.network.service

import ar.com.wolox.android.bootstrap.model.Album
import ar.com.wolox.android.bootstrap.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumService {

    @GET("/albums")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("/users/{userId}/albums")
    suspend fun getAlbums(@Path("userId") userId: Int): Response<List<Album>>

    @GET("/albums/{albumId}/photos")
    suspend fun getPhotos(@Path("albumId") albumId: Int): Response<List<Photo>>

    @GET("/photos/{photoId}")
    suspend fun getPhoto(@Path("photoId") photoId: Int): Response<Photo>
}
