package com.example.ucp2.ui.viewmodel.supliervm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.InventoryApp
import com.example.ucp2.ui.viewmodel.barangvm.InventoryApp

object PenyediaSuplierViewModel{
    val Factory = viewModelFactory {
        initializer {
            SuplierViewModel(
                InventoryApp() .containerApp.repositorySuplier

            )

        }

        initializer {
            HomeSuplierViewModel(
                InventoryApp() .containerApp.repositorySuplier
            )
        }
        initializer {
            DetailSuplierViewModel(
                createSavedStateHandle(),
                InventoryApp() .containerApp.repositorySuplier
            )

        }


    }
}

fun CreationExtras.InventoryApp(): InventoryApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventoryApp)