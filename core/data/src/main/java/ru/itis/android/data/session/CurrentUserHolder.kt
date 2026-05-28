package ru.itis.android.data.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CurrentUserHolder @Inject constructor(
    userDao: UserDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val user: StateFlow<UserEntity?> = userDao.observeCurrentUser()
        .stateIn(scope, SharingStarted.Eagerly, null)
}
