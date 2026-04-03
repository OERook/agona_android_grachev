package ru.itis.android.auth.domain.model

sealed class ValidationError(val message: String) {
    object InvalidPhone : ValidationError("Номер должен быть в формате +7 (XXX) XXX-XX-XX")
    object InvalidEmail : ValidationError("Некорректный формат Email")
    object WeakPassword : ValidationError("Пароль должен быть > 6 символов, содержать цифру и заглавную букву")
    object EmptyFullName : ValidationError("Введите имя и фамилию")
    object MasterDataMissing : ValidationError("Мастер должен указать опыт и описание")
}