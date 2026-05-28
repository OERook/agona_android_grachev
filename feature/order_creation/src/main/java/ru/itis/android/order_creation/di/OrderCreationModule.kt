package ru.itis.android.order_creation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.itis.android.di.ViewModelKey

import ru.itis.android.order_creation.presentation.OrderCreationViewModel

@Module
interface OrderCreationModule {
    @Binds
    @IntoMap
    @ViewModelKey(OrderCreationViewModel::class)
    fun bindOrderCreationViewModel(viewModel: OrderCreationViewModel): ViewModel
}
