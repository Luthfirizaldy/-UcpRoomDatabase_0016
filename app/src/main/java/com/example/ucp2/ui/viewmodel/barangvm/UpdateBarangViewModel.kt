package com.example.ucp2.ui.viewmodel.barangvm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.repository.RepositoryBarang
import com.example.ucp2.ui.navigation.DestinasiBarangUpdate
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateBarangViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryBarang: RepositoryBarang
) : ViewModel() {

    var updateUIState by mutableStateOf(BarangUIState())

    private val _kode: String = checkNotNull(savedStateHandle[DestinasiBarangUpdate.ID])

    init {
        viewModelScope.launch {
            updateUIState = repositoryBarang.getBarangDetail(_kode)
                .filterNotNull()
                .first()
                .toUiStateBarang()
        }
    }

    fun updateState (barangEvent: BarangEvent) {
        updateUIState = updateUIState.copy(
            barangEvent = barangEvent,
        )
    }

    fun validateFields() : Boolean {
        val event = updateUIState.barangEvent
        val errorState = FormErrorState(
            id = if (event.id.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            deskripsi = if (event.deskripsi.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
            harga = if (event.harga.isNotEmpty()) null else "Alamat tidak boleh kosong",
            stok = if (event.stok.isNotEmpty()) null else "Kelas tidak boleh kosong",
            namaSuplier = if (event.namaSuplier.isNotEmpty()) null else "Angkatan tidak boleh kosong",
        )

        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateData() {
        val currentEvent = updateUIState.barangEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    // Buat entity Barang tanpa mengubah ID
                    val barangEntity = currentEvent.toBarangEntityPreserveId()

                    // Update data di repository
                    repositoryBarang.updateBarang(barangEntity)

                    // Perbarui UI state setelah update berhasil
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data berhasil diupdate",
                        barangEvent = BarangEvent(),
                        isEntryValid = FormErrorState()
                    )
                    Log.d("UpdateDataHasil", "Data berhasil diupdate: $barangEntity")
                } catch (e: Exception) {
                    // Tampilkan pesan jika terjadi error
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data gagal diupdate"
                    )
                    Log.e("UpdateDataError", "Error updating data", e)
                }
            }
        } else {
            // Tampilkan pesan jika validasi gagal
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data gagal diupdate"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }
}

fun Barang.toUiStateBarang () : BarangUIState = BarangUIState(
    barangEvent = this.toDetailUiEvent(),
)