package ru.itis.android.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.itis.android.model.ChatRoom
import ru.itis.android.repository.ChatRepository
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val rooms: StateFlow<List<ChatRoom>> = chatRepository.observeRooms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { chatRepository.connect() }
        viewModelScope.launch { chatRepository.refreshRooms() }
    }

    fun refresh() {
        viewModelScope.launch { chatRepository.refreshRooms() }
    }


    fun openRoomForOrder(
        orderId: Long,
        onOpened: (roomId: String, peerName: String, orderId: Long) -> Unit
    ) {
        viewModelScope.launch {
            chatRepository.openRoomForOrder(orderId).onSuccess { room ->
                onOpened(room.id, room.peerName, room.orderId ?: orderId)
            }
        }
    }
}
