package ru.itis.android.network.di

import kotlinx.coroutines.flow.Flow

interface TokenProvider {
    val tokenFlow: Flow<String?>
}