package ru.itis.android.di

import android.content.Context
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.network.api.AuthApi
import ru.itis.android.database.dao.UserDao

interface AuthDeps {
    fun context(): Context
    fun authApi(): AuthApi
    fun userDao(): UserDao
    fun sessionManager(): SessionManager
}