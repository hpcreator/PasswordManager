package com.hpCreation.passwordManager.data

import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val passwordDao: PasswordDao) {


    val allPasswords: Flow<List<Password>> = passwordDao.getAllPasswords()

    suspend fun insert(password: Password) {
        passwordDao.insert(password)
    }

    suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    suspend fun delete(passwordId: Int) {
        passwordDao.delete(passwordId)
    }
}