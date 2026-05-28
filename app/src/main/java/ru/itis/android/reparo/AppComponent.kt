package ru.itis.android.reparo

import android.content.Context
import dagger.Component
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.data.di.DataModule
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.di.DatabaseModule
import ru.itis.android.di.AppModule
import ru.itis.android.di.AuthDeps
import ru.itis.android.di.ViewModelFactory
import ru.itis.android.chat.di.ChatModule
import ru.itis.android.main.di.MainModule
import ru.itis.android.order_creation.di.OrderCreationModule
import ru.itis.android.orders.di.OrdersModule
import ru.itis.android.profile.di.ProfileModule
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.di.NetworkModule
import ru.itis.android.repository.CategoryRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    DataModule::class,
    MainModule::class,
    OrderCreationModule::class,
    ProfileModule::class,
    OrdersModule::class,
    ChatModule::class
])
interface AppComponent : AuthDeps {
    override fun authApi(): AuthApi
    override fun context(): Context
    override fun userDao(): UserDao
    override fun sessionManager(): SessionManager
    fun categoryRepository(): CategoryRepository
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
        fun networkModule(networkModule: NetworkModule): Builder
    }
}