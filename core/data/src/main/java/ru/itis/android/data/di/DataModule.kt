package ru.itis.android.da

import dagger.Binds
import dagger.Module
import ru.itis.android.data.repository.CategoryRepositoryImpl
import ru.itis.android.repository.CategoryRepository

@Module
interface DataModule {

    @Binds
    fun bindCategoryRepository(impl : CategoryRepositoryImpl) : CategoryRepository
}