package ar.com.wolox.android.bootstrap.network.service

import ar.com.wolox.android.bootstrap.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostService {

    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("/users/{userId}/posts")
    suspend fun getPosts(@Path("userId") userId: Int): Response<List<Post>>
}
