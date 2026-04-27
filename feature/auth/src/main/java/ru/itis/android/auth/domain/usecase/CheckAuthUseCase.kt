package ru.itis.android.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.itis.android.auth.domain.repository.AuthRepository
import javax.inject.Inject
class CheckAuthUseCase @Inject constructor(
    private val repository: AuthRepository
){
    operator fun invoke(): Flow<Boolean> {
        return repository.checkAuthStatus()
    }
}