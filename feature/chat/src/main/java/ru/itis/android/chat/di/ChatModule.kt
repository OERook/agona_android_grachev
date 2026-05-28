package ru.itis.android.chat.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.itis.android.chat.presentation.ChatListViewModel
import ru.itis.android.chat.presentation.ChatRoomViewModel
import ru.itis.android.di.ViewModelKey

@Module
interface ChatModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatListViewModel::class)
    fun bindChatListViewModel(viewModel: ChatListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatRoomViewModel::class)
    fun bindChatRoomViewModel(viewModel: ChatRoomViewModel): ViewModel
}
