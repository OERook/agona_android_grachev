package ru.itis.android.reparo

import android.app.Application
import ru.itis.android.di.AppModule

class ReparoApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
