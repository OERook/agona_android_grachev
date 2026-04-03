package ru.itis.android.auth.domain.usecase

import ru.itis.android.auth.domain.model.ValidationError
import ru.itis.android.auth.domain.repository.AuthRepository
import ru.itis.android.auth.domain.validation.AuthValidator
import ru.itis.android.auth.presentation.RegistrationState
import ru.itis.android.data.model.UserInfo
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(state: RegistrationState): Result<UserInfo.User> {
       return when {
           !AuthValidator.validatePhone(state.phone) ->
               Result.failure(Exception(ValidationError.InvalidPhone.message))

           !AuthValidator.validateEmail(state.email) ->
               Result.failure(Exception(ValidationError.InvalidEmail.message))

           !AuthValidator.validatePassword(state.password) ->
               Result.failure(Exception(ValidationError.WeakPassword.message))

           state.fullName.isBlank() ->
               Result.failure(Exception(ValidationError.EmptyFullName.message))

           state.role == "master" && (state.about.isNullOrBlank() || (state.experienceYears ?: 0) <= 0) ->
               Result.failure(Exception(ValidationError.MasterDataMissing.message))

           else -> repository.register(state)
       }
    }
}