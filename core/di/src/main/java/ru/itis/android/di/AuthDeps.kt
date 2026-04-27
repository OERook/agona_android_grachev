package ru.itis.android.di

import android.content.Context
import ru.itis.android.network.api.AuthApi

interface AuthDeps {
    fun context(): Context
    fun authApi(): AuthApi
}