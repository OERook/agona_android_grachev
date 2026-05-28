package ru.itis.android.orders.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.itis.android.di.ViewModelKey
import ru.itis.android.orders.presentation.OrdersViewModel
import ru.itis.android.orders.presentation.WriteReviewViewModel

@Module
interface OrdersModule {
    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    fun bindOrdersViewModel(viewModel: OrdersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WriteReviewViewModel::class)
    fun bindWriteReviewViewModel(viewModel: WriteReviewViewModel): ViewModel
}
