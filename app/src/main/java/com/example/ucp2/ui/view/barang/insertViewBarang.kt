package com.example.ucp2.ui.view.barang

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Suplier
import com.example.ucp2.ui.customWidget.TopAppBar
import com.example.ucp2.ui.navigation.Alamatnavigasi
import com.example.ucp2.ui.viewmodel.barangvm.BarangEvent
import com.example.ucp2.ui.viewmodel.barangvm.BarangUIState
import com.example.ucp2.ui.viewmodel.barangvm.BarangViewModel
import com.example.ucp2.ui.viewmodel.barangvm.FormErrorState
import com.example.ucp2.ui.viewmodel.barangvm.PenyediaBarangViewModel
import com.example.ucp2.ui.viewmodel.supliervm.HomeSuplierViewModel
import com.example.ucp2.ui.viewmodel.supliervm.HomeUiStateSuplier
import com.example.ucp2.ui.viewmodel.supliervm.PenyediaSuplierViewModel
import kotlinx.coroutines.launch

object DestinasiBarangInsert : Alamatnavigasi {
    override val route = "insert_barang"
}

@Composable
fun InsertBarangView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BarangViewModel = viewModel(factory = PenyediaBarangViewModel.Factory),
    viewModelSup: HomeSuplierViewModel = viewModel(factory = PenyediaSuplierViewModel.Factory),

    ) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val supList by viewModelSup.homeUiState.collectAsState()


    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Barang",
                modifier = Modifier
            )
            InsertBodyBarang(
                uiState = uiState,
                listSuplier = supList,
                onValueChange = { updatedEvent ->
                    viewModel.updateState(updatedEvent)
                },
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveData()
                    }
                    onNavigate()
                }
            )
        }
    }
}

@Composable
fun InsertBodyBarang(
    modifier: Modifier = Modifier,
    onValueChange: (BarangEvent) -> Unit,
    onClick: () -> Unit = {},
    uiState: BarangUIState,
    listSuplier : HomeUiStateSuplier


) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormBarang(
            barangEvent = uiState.barangEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth(),
            listSup = listSuplier.listSuplier


        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Simpan")
        }
    }
}

@Composable
fun FormBarang(
    barangEvent: BarangEvent = BarangEvent(),
    onValueChange: (BarangEvent) -> Unit = {},
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier,
    listSup : List<Suplier>
) {
    val supplierList = listSup.map { it.nama }
    var expanded by remember { mutableStateOf(false) }
    var selectedSupplier by remember { mutableStateOf("") }


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Input Nama Barang
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = barangEvent.nama,
            onValueChange = {
                onValueChange(barangEvent.copy(nama = it))
            },
            label = { Text("Nama Barang") },
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan Nama Barang") }
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )

        // Input Deskripsi Barang
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = barangEvent.deskripsi,
            onValueChange = {
                onValueChange(barangEvent.copy(deskripsi = it))
            },
            label = { Text("Deskripsi") },
            isError = errorState.deskripsi != null,
            placeholder = { Text("Masukkan Deskripsi Barang") }
        )
        Text(
            text = errorState.deskripsi ?: "",
            color = Color.Red
        )

        // Input Harga Barang
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = barangEvent.harga,
            onValueChange = {
                onValueChange(barangEvent.copy(harga = it))
            },
            label = { Text("Harga") },
            isError = errorState.harga != null,
            placeholder = { Text("Masukkan Harga") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.harga ?: "",
            color = Color.Red
        )

        // Input Stok Barang
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = barangEvent.stok,
            onValueChange = {
                onValueChange(barangEvent.copy(stok = it))
            },
            label = { Text("Stok") },
            isError = errorState.stok != null,
            placeholder = { Text("Masukkan Stok Barang") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.stok ?: "",
            color = Color.Red
        )

        // Supplier Dropdown
        Box(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            // Tampilkan field yang dapat diklik untuk membuka dropdown
            Text(
                text = selectedSupplier.ifEmpty { "Pilih Supplier" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { expanded = !expanded }
                    .border(1.dp, Color.Gray) // Tambahkan border agar menyerupai TextField
                    .padding(16.dp)
            )

            // DropdownMenu untuk menampilkan daftar supplier
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                supplierList.forEach { supplier ->
                    DropdownMenuItem(
                        text = { Text(text = supplier) }, // Menampilkan nama supplier
                        onClick = {
                            selectedSupplier = supplier // Memilih supplie
                            onValueChange(barangEvent.copy(namaSuplier = supplier))
                            expanded = false // Menutup dropdown
                        }
                    )
                }
            }
        }
    }


    Spacer(modifier = Modifier.height(16.dp))
}



@Preview(showBackground = true)
@Composable
fun PreviewInsertBarang() {
    InsertBarangView(onBack = {}, onNavigate = {})
}