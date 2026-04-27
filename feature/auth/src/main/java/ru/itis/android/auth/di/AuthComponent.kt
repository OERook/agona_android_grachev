package ru.itis.android.auth.di

import dagger.Component
import ru.itis.android.di.AuthDeps // Импортируем наш контракт из :core:di
import ru.itis.android.reparo.di.ViewModelFactory

@Component(
    modules = [AuthModule::class],
    dependencies = [AuthDeps::class]
)
interface AuthComponent {

    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        fun build(): AuthComponent

        fun authDeps(authDeps: AuthDeps): Builder
    }
}