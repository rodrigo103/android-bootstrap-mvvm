package ar.com.wolox.android.bootstrap.network.service

import ar.com.wolox.android.bootstrap.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): Response<User>
}
