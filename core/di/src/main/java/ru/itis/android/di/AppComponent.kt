package ru.itis.android.di

import android.content.Context
import dagger.Component
import javax.inject.Singleton
import ru.itis.android.network.di.NetworkModule


@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
])
interface AppComponent {
    fun context(): Context

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }
}
