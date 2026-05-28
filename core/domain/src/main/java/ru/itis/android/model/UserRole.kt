package ru.itis.android.model


enum class UserRole(val raw: String) {
    CLIENT("client"),
    MASTER("master"),
    ADMIN("admin");

    companion object {
        fun fromRaw(value: String?): UserRole =
            entries.firstOrNull { it.raw == value } ?: CLIENT
    }
}
