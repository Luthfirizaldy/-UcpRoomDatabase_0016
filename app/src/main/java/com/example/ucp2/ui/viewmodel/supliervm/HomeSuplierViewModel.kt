package com.example.ucp2.ui.viewmodel.supliervm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Suplier
import com.example.ucp2.repository.RepositorySuplier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeSuplierViewModel(
    private val repositorySuplier: RepositorySuplier
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiStateSuplier> = repositorySuplier.getAllSuplier()
        .filterNotNull()
        .map {
            HomeUiStateSuplier(
                listSuplier = it.toList(),
                isLoading = false
            )
        }
        .onStart {
            emit(HomeUiStateSuplier(isLoading = true))
            delay(900)
        }
        .catch { exception ->
            emit(
                HomeUiStateSuplier(
                    isLoading = false,
                    isError = true,
                    errorMessage = exception.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiStateSuplier(
                isLoading = true
            )
        )
}

data class HomeUiStateSuplier(
    val listSuplier: List<Suplier> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)