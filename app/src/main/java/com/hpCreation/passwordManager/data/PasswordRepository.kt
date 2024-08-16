package com.hpCreation.passwordManager.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val passwordDao: PasswordDao) {
    suspend fun addPassword(password: Password) = passwordDao.addPassword(password)
    suspend fun updatePassword(password: Password) = passwordDao.updatePassword(password)
    suspend fun deletePassword(passwordId: Int) = passwordDao.deletePassword(passwordId)
    fun getAllPassword(): Flow<List<Password>> =
        passwordDao.getAllPasswords().flowOn(Dispatchers.IO).conflate()
}