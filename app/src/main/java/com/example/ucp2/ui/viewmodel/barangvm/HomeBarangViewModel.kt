package com.example.ucp2.ui.viewmodel.barangvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.repository.RepositoryBarang
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeBarangViewModel(
    private val repositoryBarang: RepositoryBarang
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = repositoryBarang.getAllBarang()
        .filterNotNull()
        .map {
            HomeUiState(
                listBarang = it.toList(),
                isLoading = false
            )
        }
        .onStart {
            emit(HomeUiState(isLoading = true))
            delay(900)
        }
        .catch { exception ->
            emit(
                HomeUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = exception.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(
                isLoading = true
            )
        )
    fun deleteBarang(id: String) {
        viewModelScope.launch {
            try {
                repositoryBarang.deleteBarang(id) // Memanggil dari RepositoryBarang
            } catch (e: Exception) {
            }
        }
    }

}

data class HomeUiState(
    val listBarang: List<Barang> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)

data class HomelUiState(
    val detailUiEvent: BarangEvent = BarangEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == BarangEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != BarangEvent()
}