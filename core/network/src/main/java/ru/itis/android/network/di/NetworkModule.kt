package ru.itis.android.network.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.android.network.api.AuthApi
import ru.itis.android.network.api.CategoryApi
import ru.itis.android.network.api.ChatApi
import ru.itis.android.network.api.MastersApi
import ru.itis.android.network.api.OrdersApi
import ru.itis.android.network.api.ReviewsApi
import ru.itis.android.network.api.ServicesApi
import ru.itis.android.network.auth.AuthInterceptor
import ru.itis.android.network.auth.UnauthorizedInterceptor
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        unauthorizedInterceptor: UnauthorizedInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideServicesApi(retrofit: Retrofit): ServicesApi {
        return retrofit.create(ServicesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrdersApi(retrofit: Retrofit): OrdersApi {
        return retrofit.create(OrdersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMastersApi(retrofit: Retrofit): MastersApi {
        return retrofit.create(MastersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewsApi(retrofit: Retrofit): ReviewsApi {
        return retrofit.create(ReviewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }
}
