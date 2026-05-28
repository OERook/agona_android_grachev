package ru.itis.android.orders.presentation

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.android.repository.ReviewFileInput
import ru.itis.android.repository.ReviewsRepository
import javax.inject.Inject

class WriteReviewViewModel @Inject constructor(
    private val context: Context,
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WriteReviewState())
    val state: StateFlow<WriteReviewState> = _state.asStateFlow()

    fun init(orderId: Long) {
        _state.value = WriteReviewState(orderId = orderId)
    }

    fun setRating(rating: Int) {
        _state.update { it.copy(rating = rating.coerceIn(0, 5), errorMessage = null) }
    }

    fun updateComment(value: String) {
        _state.update { it.copy(comment = value, errorMessage = null) }
    }

    fun addAttachments(uris: List<Uri>) {
        if (uris.isEmpty()) return
        viewModelScope.launch {
            val resolved = withContext(Dispatchers.IO) {
                uris.mapNotNull { uri -> describe(uri) }
            }
            _state.update { s -> s.copy(attachments = s.attachments + resolved) }
        }
    }

    fun removeAttachment(uri: String) {
        _state.update { it.copy(attachments = it.attachments.filterNot { a -> a.uri == uri }) }
    }

    fun submit() {
        val s = _state.value
        if (s.isSubmitting) return
        if (s.rating !in 1..5) {
            _state.update { it.copy(errorMessage = "Поставьте оценку от 1 до 5") }
            return
        }
        _state.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            val inputs = withContext(Dispatchers.IO) {
                s.attachments.mapNotNull { att -> readToBytes(att) }
            }
            reviewsRepository.createReview(
                orderId = s.orderId,
                rating = s.rating,
                comment = s.comment.trim().ifBlank { null },
                files = inputs
            ).onSuccess {
                _state.update { it.copy(isSubmitting = false, submitted = true) }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Не удалось отправить отзыв"
                    )
                }
            }
        }
    }


    private fun describe(uri: Uri): PendingAttachment? {
        val resolver: ContentResolver = context.contentResolver
        val mime = resolver.getType(uri) ?: "application/octet-stream"
        val name = queryDisplayName(resolver, uri) ?: "attachment-${System.currentTimeMillis()}"
        return PendingAttachment(
            uri = uri.toString(),
            mimeType = mime,
            displayName = name
        )
    }

    private fun queryDisplayName(resolver: ContentResolver, uri: Uri): String? {
        return try {
            resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                if (c.moveToFirst()) c.getString(0) else null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun readToBytes(att: PendingAttachment): ReviewFileInput? {
        return try {
            val uri = Uri.parse(att.uri)
            val bytes = context.contentResolver.openInputStream(uri)
                ?.use { it.readBytes() }
                ?: return null
            ReviewFileInput(
                bytes = bytes,
                fileName = att.displayName,
                mimeType = att.mimeType
            )
        } catch (_: Exception) {
            null
        }
    }
}
