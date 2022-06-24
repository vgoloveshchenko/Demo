package com.example.demo.presentation.ui.users.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.domain.model.users.UserDetails
import com.example.demo.domain.usecase.GetUserDetailsUseCase
import com.example.demo.presentation.model.LceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class UserDetailsViewModel(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val login: String
) : ViewModel() {

    val userDetailsFlow: Flow<LceState<UserDetails>> = flow {
        val state = getUserDetailsUseCase(login)
            .fold(
                onSuccess = { LceState.Content(it) },
                onFailure = { LceState.Error(it) }
            )
        emit(state)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LceState.Loading
    )
}