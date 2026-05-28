package ru.itis.android.data.di

import dagger.Binds
import dagger.Module
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.data.repository.CategoryRepositoryImpl
import ru.itis.android.data.repository.ChatRepositoryImpl
import ru.itis.android.data.repository.MasterRepositoryImpl
import ru.itis.android.data.repository.OrdersRepositoryImpl
import ru.itis.android.data.repository.ReviewsRepositoryImpl
import ru.itis.android.data.repository.ServicesRepositoryImpl
import ru.itis.android.data.session.SessionInvalidatorImpl
import ru.itis.android.network.auth.AuthTokenProvider
import ru.itis.android.network.auth.SessionInvalidator
import ru.itis.android.repository.CategoryRepository
import ru.itis.android.repository.ChatRepository
import ru.itis.android.repository.MasterRepository
import ru.itis.android.repository.OrdersRepository
import ru.itis.android.repository.ReviewsRepository
import ru.itis.android.repository.ServicesRepository

@Module
interface DataModule {

    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindServicesRepository(impl: ServicesRepositoryImpl): ServicesRepository

    @Binds
    fun bindOrdersRepository(impl: OrdersRepositoryImpl): OrdersRepository

    @Binds
    fun bindMasterRepository(impl: MasterRepositoryImpl): MasterRepository

    @Binds
    fun bindReviewsRepository(impl: ReviewsRepositoryImpl): ReviewsRepository

    @Binds
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindAuthTokenProvider(impl: SessionManager): AuthTokenProvider

    @Binds
    fun bindSessionInvalidator(impl: SessionInvalidatorImpl): SessionInvalidator
}
