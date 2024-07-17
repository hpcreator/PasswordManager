package com.hpCreation.passwordManager.util

fun String.encryptPassword(): String {
    return EncryptionHelper.encrypt(this).toString()
}

fun String.decryptPassword(): String {
    return EncryptionHelper.decrypt(this).toString()
}