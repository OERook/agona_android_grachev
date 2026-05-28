package ru.itis.android.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.itis.android.model.ChatMessage
import ru.itis.android.repository.ChatRepository
import javax.inject.Inject

data class ChatRoomUiState(
    val roomId: String? = null,
    val peerName: String = "",
    val draft: String = "",
    val errorMessage: String? = null
)

class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(ChatRoomUiState())
    val ui: StateFlow<ChatRoomUiState> = _ui.asStateFlow()

    private val _activeRoomId = MutableStateFlow<String?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<ChatMessage>> = _activeRoomId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else chatRepository.observeMessages(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun bindRoom(roomId: String, peerName: String) {
        _ui.value = _ui.value.copy(roomId = roomId, peerName = peerName)
        _activeRoomId.value = roomId
        viewModelScope.launch { chatRepository.subscribeRoom(roomId) }
        viewModelScope.launch { chatRepository.refreshMessages(roomId) }
    }

    fun updateDraft(text: String) {
        _ui.value = _ui.value.copy(draft = text, errorMessage = null)
    }

    fun send() {
        val state = _ui.value
        val roomId = state.roomId ?: return
        val text = state.draft.trim()
        if (text.isEmpty()) return
        _ui.value = state.copy(draft = "")
        viewModelScope.launch {
            chatRepository.sendMessage(roomId, text).onFailure { err ->
                _ui.value = _ui.value.copy(errorMessage = err.message)
            }
        }
    }

    fun clearError() {
        _ui.value = _ui.value.copy(errorMessage = null)
    }

    fun unbind() {
        _activeRoomId.value = null
        _ui.value = ChatRoomUiState()
    }
}
