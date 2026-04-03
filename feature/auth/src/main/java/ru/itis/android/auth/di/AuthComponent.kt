package ru.itis.android.auth.di

import dagger.Component
import ru.itis.android.di.AppComponent
import javax.inject.Scope

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class AuthScope

@AuthScope
@Component(
    dependencies = [AppComponent::class],
    modules = [AuthModule::class]
)
interface AuthComponent {
    fun viewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder
        fun build(): AuthComponent
    }
}
