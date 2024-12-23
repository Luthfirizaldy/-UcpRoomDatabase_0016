package com.example.ucp2.ui.viewmodel.supliervm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import com.example.ucp2.repository.RepositorySuplier
import kotlinx.coroutines.launch

// ViewModel untuk Barang
class SuplierViewModel(
    private val repositorySuplier: RepositorySuplier
) : ViewModel() {

    var uiState by mutableStateOf(SuplierUiState())


    // Update state berdasarkan input pengguna
    fun updateState(suplierEvent: SuplierEvent) {
        uiState = uiState.copy(
            suplierEvent = suplierEvent,
        )
    }



    // Validasi input data
    private fun validateFields(): Boolean {
        val event = uiState.suplierEvent
        val errorState = FormErrorStateSuplier(
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            kontak = if (event.kontak.isNotEmpty()) null else "Kontak tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",

            )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    // Fungsi untuk menyimpan data barang
    fun saveData() {
        val currentEvent = uiState.suplierEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositorySuplier.insertSuplier(currentEvent.toSuplierEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        suplierEvent = SuplierEvent(), // Reset data input
                        isEntryValid = FormErrorStateSuplier(), // Reset error state
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid. Periksa kembali data Anda."
            )
        }
    }

    // Reset pesan Snackbar setelah ditampilkan
    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }
}

// UI State untuk Barang
data class SuplierUiState(
    val suplierEvent: SuplierEvent = SuplierEvent(),
    val isEntryValid: FormErrorStateSuplier = FormErrorStateSuplier(),
    val snackBarMessage: String? = null,
)

// Error State untuk validasi form
data class FormErrorStateSuplier(
    val id: String? = null,
    val nama : String? =null,
    val kontak : String? =null,
    val alamat : String? =null
) {
    fun isValid(): Boolean {
        return nama == null && kontak == null && alamat == null
    }
}

// Data class untuk menyimpan input form
data class SuplierEvent(
    val id : String ="",
    val nama : String ="",
    val kontak : String = "",
    val alamat : String = ""
)

// Fungsi untuk konversi BarangEvent menjadi entitas Barang
fun SuplierEvent.toSuplierEntity(): Suplier = Suplier(
    id = System.currentTimeMillis().toString(), // Auto-generate ID
    nama = nama,
    kontak =kontak,
    alamat = alamat
)