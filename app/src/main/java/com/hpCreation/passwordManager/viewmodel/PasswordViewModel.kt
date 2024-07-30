package com.hpCreation.passwordManager.viewmodel

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.data.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordRepository) :
    ViewModel() {

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible

    private val _isAddEditBottomSheetVisible = MutableStateFlow(false)
    val isAddEditBottomSheetVisible: StateFlow<Boolean> = _isAddEditBottomSheetVisible

    private val _allPasswords = MutableStateFlow<List<Password>>(emptyList())
    val allPasswords = _allPasswords.asStateFlow()

    val loading = mutableStateOf(false)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPassword().distinctUntilChanged().collect { listOfPassword ->
                if (listOfPassword.isEmpty()) {
                    Log.d("TAG", "EmptyList")
                } else {
                    _allPasswords.value = listOfPassword
                }
            }
        }
    }

    fun onShowBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun onDismissBottomSheet() {
        _isBottomSheetVisible.value = false
    }

    fun onShowAddEditBottomSheet() {
        _isAddEditBottomSheetVisible.value = true
    }

    fun onDismissAddEditBottomSheet() {
        _isAddEditBottomSheetVisible.value = false
    }

    fun addPassword(password: Password) = viewModelScope.launch {
        loading.value = true
        _eventFlow.emit(UiEvent.PasswordSaved)
        delay(1000)
        loading.value = false
        repository.addPassword(password)
    }

    fun updatePassword(passwordEntity: Password) = viewModelScope.launch {
        loading.value = true
        _eventFlow.emit(UiEvent.PasswordSaved)
        delay(1000)
        loading.value = false
        repository.updatePassword(passwordEntity)
    }

    fun deletePassword(passwordId: Int) = viewModelScope.launch {
        loading.value = true
        _eventFlow.emit(UiEvent.PasswordDeleted)
        delay(1000)
        loading.value = false
        repository.deletePassword(passwordId)
    }


    sealed class UiEvent {
        data object PasswordSaved : UiEvent()
        data object PasswordDeleted : UiEvent()
    }


    private var key: String = "ItsTopSecret🤫"
    private var secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
    fun encrypt(string: String): String {

        val cipher =
            Cipher.getInstance("AES/ECB/PKCS5Padding") //Specifying which mode of AES is to be used
        cipher.init(
            Cipher.ENCRYPT_MODE, secretKeySpec
        )
        val encryptBytes = cipher.doFinal(string.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptBytes, Base64.DEFAULT)

    }

    fun decrypt(string: String): String {

        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val decryptedBytes = cipher.doFinal(Base64.decode(string, Base64.DEFAULT))
        return String(decryptedBytes, Charsets.UTF_8)
    }
}