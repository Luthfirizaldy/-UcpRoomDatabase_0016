package com.example.ucp2.ui.view.barang

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.ui.customWidget.TopAppBar
import com.example.ucp2.ui.viewmodel.barangvm.DetailBarangViewModel
import com.example.ucp2.ui.viewmodel.barangvm.HomeBarangViewModel
import com.example.ucp2.ui.viewmodel.barangvm.HomeUiState
import com.example.ucp2.ui.viewmodel.barangvm.PenyediaBarangViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeBarang(
    viewModel: HomeBarangViewModel = viewModel(factory = PenyediaBarangViewModel.Factory),
    onAddBarang: () -> Unit = {},
    onBack: () -> Unit,
    onDetailClick: (String) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    var barangToDeleteId by remember { mutableStateOf<String?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Daftar Barang",
                showBackButton = true,
                onBack = onBack,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBarang,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Barang"
                )
            }
        }
    ) { innerPadding ->
        val homeUiState by viewModel.homeUiState.collectAsState()

        BodyHomeBarangView(
            homeUiState = homeUiState,
            onClick = {
                onDetailClick(it)
                println(it)
            },
            onDelete = { id ->
                barangToDeleteId = id
                showDialog = true
            },
            onEditClick = {
                onEditClick(it)
                Log.d("data du edit","Data yang akan di edit : $it")
            },

            modifier = Modifier.padding(innerPadding)
        )
        if (showDialog) {
            ConfirmDeleteDialog(
                onDismiss = { showDialog = false }, // Menutup dialog jika tombol "Tidak" ditekan
                onConfirm = {
                    barangToDeleteId?.let {
                        viewModel.deleteBarang(it) // Panggil fungsi deleteBarang dari ViewModel
                    }
                    showDialog = false // Tutup dialog setelah konfirmasi
                }
            )
        }
    }
}

@Composable
fun BodyHomeBarangView(
    homeUiState: HomeUiState,
    onClick: (String) -> Unit = {},
    onDelete: (String) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,

    ) {

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    when {
        homeUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        homeUiState.isError -> {
            LaunchedEffect(homeUiState.errorMessage) {
                homeUiState.errorMessage?.let { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        homeUiState.listBarang.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data Barang.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            ListBarang(
                listBarang = homeUiState.listBarang,
                onClick = {
                    onClick(it)
                    println(it)

                },
                onDelete = onDelete,
                onEditClick = {
                    onEditClick(it)
                    println(it)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
fun ListBarang(
    listBarang: List<Barang>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    onDelete: (String) -> Unit,
    onEditClick: (String) -> Unit ={}
) {
    LazyColumn(modifier = modifier) {
        items(
            items = listBarang,
            itemContent = { brg ->
                CardBarang(
                    brg = brg,
                    onClick = { onClick(brg.id) },
                    onDelete = {onDelete(brg.id)},
                    onEditClick = {onEditClick(brg.id)}
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CardBarang(
    brg: Barang,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDelete: (String) -> Unit = {} ,
    onEditClick : (String) -> Unit = {}
) {
    val cardColor = when {
        brg.stok.toInt() == 0 -> Color.Gray
        brg.stok.toInt() in 1..10 -> Color.Red
        brg.stok.toInt() > 10 -> Color.Green
        else -> Color.Transparent
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box( // Menggunakan Box untuk menempatkan ikon hapus di tengah vertikal
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(cardColor)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = brg.nama,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,


                        )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = brg.namaSuplier,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,

                        )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Face, contentDescription = "")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = brg.stok,
                        fontWeight = FontWeight.Bold,

                        )
                }
            }

            // Menambahkan Box untuk ikon hapus, agar posisinya tetap di tengah secara vertikal
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Menempatkan di sebelah kanan tengah
                    .padding(8.dp), // Tambahkan padding sesuai kebutuhan
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Jarak antar ikon
            ) {
                IconButton(onClick =  {onEditClick(brg.id)
                }) {
                    Log.d("Edit","Edit ID{${brg.id}}")
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                }

                IconButton(onClick =  {onDelete(brg.id)
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Hapus")
                }


            }



        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Apakah Anda yakin ingin menghapus barang ini?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}