package ru.itis.android.network.models

import com.google.gson.annotations.SerializedName

data class NetworkCategory(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String
)
