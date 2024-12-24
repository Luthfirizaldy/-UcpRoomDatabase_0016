package com.example.ucp2.ui.view.barang

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ucp2.ui.viewmodel.barangvm.DetailUiState
import com.example.ucp2.ui.viewmodel.barangvm.PenyediaBarangViewModel
import com.example.ucp2.ui.viewmodel.barangvm.toBarangEntity

@Composable
fun DetailBarangView(
    modifier: Modifier = Modifier,
    viewModel: DetailBarangViewModel = viewModel(factory = PenyediaBarangViewModel.Factory),
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Detail Barang",
                showBackButton = true,
                onBack = onBack,
                modifier = modifier
            )
        },

        ) { innerPadding ->
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailMatakuliah(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState,

            )
    }
}

@Composable
fun BodyDetailMatakuliah(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState = DetailUiState(),
) {


    when {
        detailUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        detailUiState.isUiEventNotEmpty -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailBarang(
                    barang = detailUiState.detailUiEvent.toBarangEntity(),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }

        detailUiState.isUiEventEmpty -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ItemDetailBarang(
    modifier: Modifier = Modifier,
    barang: Barang
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailMatakuliah(judul = "id", isinya = barang.id)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMatakuliah(judul = "Nama", isinya = barang.nama)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMatakuliah(judul = "Deskripsi", isinya = barang.deskripsi)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMatakuliah(judul = "stok", isinya = barang.stok)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMatakuliah(judul = "supplier", isinya = barang.namaSuplier)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMatakuliah(judul = "harga", isinya = barang.harga)
        }
    }
}

@Composable
fun ComponentDetailMatakuliah(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}