package com.example.ucp2.ui.viewmodel.barangvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.repository.RepositoryBarang
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// ViewModel untuk Barang
class BarangViewModel(
    private val repositoryBarang: RepositoryBarang
) : ViewModel() {

    // State untuk UI
    private val _uiState = MutableStateFlow(BarangUIState())
    val uiState: StateFlow<BarangUIState> = _uiState

    // Update state berdasarkan input pengguna
    fun updateState(barangEvent: BarangEvent) {
        _uiState.value = _uiState.value.copy(
            barangEvent = barangEvent,
        )

        // Ambil nama suplier berdasarkan id_suplier
        getSuplierName(barangEvent.id_suplier)
    }

    // Fungsi untuk mengambil nama suplier berdasarkan id_suplier
    private fun getSuplierName(idSuplier: String) {
        viewModelScope.launch {
            repositoryBarang.getSuplierByNama(idSuplier).collect { suplierName ->
                _uiState.value = _uiState.value.copy(
                    suplierName = suplierName.toString()
                )
            }
        }
    }

    // Validasi input data
    private fun validateFields(): Boolean {
        val event = _uiState.value.barangEvent
        val errorState = FormErrorState(
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            deskripsi = if (event.deskripsi.isNotEmpty()) null else "Deskripsi tidak boleh kosong",
            harga = if (event.harga.isNotEmpty()) null else "Harga tidak boleh kosong",
            stok = if (event.stok.isNotEmpty()) null else "Stok tidak boleh kosong",
            idSuplier = if (event.id_suplier.isNotEmpty()) null else "ID Suplier tidak boleh kosong"
        )
        _uiState.value = _uiState.value.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    // Fungsi untuk menyimpan data barang
    fun saveData() {
        val currentEvent = _uiState.value.barangEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryBarang.insertBarang(currentEvent.toBarangEntity())
                    _uiState.value = _uiState.value.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        barangEvent = BarangEvent(), // Reset data input
                        isEntryValid = FormErrorState(), // Reset error state
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                snackBarMessage = "Input tidak valid. Periksa kembali data Anda."
            )
        }
    }

    // Reset pesan Snackbar setelah ditampilkan
    fun resetSnackBarMessage() {
        _uiState.value = _uiState.value.copy(snackBarMessage = null)
    }
}

// UI State untuk Barang
data class BarangUIState(
    val barangEvent: BarangEvent = BarangEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null,
    val suplierName: String = "" // Menyimpan nama suplier
)

// Error State untuk validasi form
data class FormErrorState(
    val id: String? = null,
    val nama: String? = null,
    val deskripsi: String? = null,
    val harga: String? = null,
    val stok: String? = null,
    val idSuplier: String? = null
) {
    fun isValid(): Boolean {
        return nama == null && deskripsi == null && harga == null &&
                stok == null && idSuplier == null
    }
}

// Data class untuk menyimpan input form
data class BarangEvent(
    val id : String ="",
    val nama: String = "",
    val deskripsi: String = "",
    val harga: String = "",
    val stok: String = "",
    val id_suplier: String = ""
)

// Fungsi untuk konversi BarangEvent menjadi entitas Barang
fun BarangEvent.toBarangEntity(): Barang = Barang(
    id = System.currentTimeMillis().toString(), // Auto-generate ID
    nama = nama,
    deskripsi = deskripsi,
    harga = harga,
    stok = stok,
    id_suplier = id_suplier
)