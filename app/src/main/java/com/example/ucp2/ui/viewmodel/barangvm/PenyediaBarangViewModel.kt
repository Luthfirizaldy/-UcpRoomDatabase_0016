package com.example.ucp2.ui.viewmodel.barangvm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.InventoryApp

object PenyediaBarangViewModel{
    val Factory = viewModelFactory {
        initializer {
            BarangViewModel(
                InventoryApp() .containerApp.repositoryBarang,
                InventoryApp() .containerApp.repositorySuplier

            )

        }

        initializer {
            HomeBarangViewModel(
                InventoryApp() .containerApp.repositoryBarang
            )
        }

        initializer {
            DetailBarangViewModel(
                createSavedStateHandle(),
                InventoryApp() .containerApp.repositoryBarang
            )

        }
        initializer {
            UpdateBarangViewModel(
                createSavedStateHandle(),
                InventoryApp().containerApp.repositoryBarang
            )
        }
    }
}

fun CreationExtras.InventoryApp(): InventoryApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApp)