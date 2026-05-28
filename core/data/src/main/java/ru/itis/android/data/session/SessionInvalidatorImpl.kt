package ru.itis.android.data.session

import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.database.dao.UserDao
import ru.itis.android.network.auth.SessionInvalidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionInvalidatorImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : SessionInvalidator {

    override suspend fun invalidate() {
        sessionManager.clearToken()
        userDao.clear()
    }
}
