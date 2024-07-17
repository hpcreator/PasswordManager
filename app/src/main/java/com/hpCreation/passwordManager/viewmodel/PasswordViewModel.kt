package com.hpCreation.passwordManager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.data.PasswordRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasswordViewModel(private val repository: PasswordRepository) : ViewModel() {

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible

    private val _isAddEditBottomSheetVisible = MutableStateFlow(false)
    val isAddEditBottomSheetVisible: StateFlow<Boolean> = _isAddEditBottomSheetVisible

    private val _allPasswords = MutableStateFlow<List<Password>>(emptyList())
    val allPasswords: StateFlow<List<Password>> = _allPasswords.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    init {
        viewModelScope.launch {
            repository.allPasswords.collect { passwords ->
                _allPasswords.value = passwords
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


    fun insert(password: Password) {
        viewModelScope.launch {
            repository.insert(password)
            _eventFlow.emit(UiEvent.PasswordSaved)
        }
    }

    fun update(password: Password) {
        viewModelScope.launch {
            repository.update(password)
            _eventFlow.emit(UiEvent.PasswordSaved)
        }
    }

    fun delete(passwordId: Int) {
        viewModelScope.launch {
            repository.delete(passwordId)
            _eventFlow.emit(UiEvent.PasswordDeleted)
        }
    }


    sealed class UiEvent {
        data object PasswordSaved : UiEvent()
        data object PasswordDeleted : UiEvent()
    }
}