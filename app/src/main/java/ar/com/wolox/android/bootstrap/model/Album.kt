package ar.com.wolox.android.bootstrap.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
) : Serializable
