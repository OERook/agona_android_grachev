package ru.itis.android.di

import android.content.Context
import dagger.Component
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.di.DatabaseModule
import ru.itis.android.network.api.AuthApi
import javax.inject.Singleton
import ru.itis.android.network.di.NetworkModule


@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class
])
interface AppComponent {
    fun authApi(): AuthApi
    fun context(): Context
    fun userDao(): UserDao

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
        fun networkModule(networkModule: NetworkModule): Builder
    }
}
