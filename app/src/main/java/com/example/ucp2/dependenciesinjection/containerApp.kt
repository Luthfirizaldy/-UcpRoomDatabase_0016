package com.example.ucp2.dependenciesinjection

import android.content.Context
import com.example.ucp2.data.database.AppDatabase
import com.example.ucp2.repository.LocalRepositoryBarang
import com.example.ucp2.repository.LocalRepositorySuplier
import com.example.ucp2.repository.RepositoryBarang
import com.example.ucp2.repository.RepositorySuplier


interface InterfaceContainerApp {

    val repositoryBarang: RepositoryBarang
    val repositorySuplier: RepositorySuplier
}

class ContainerApp(private val context: Context) : InterfaceContainerApp {

    override val repositoryBarang: RepositoryBarang by lazy {
        val database = AppDatabase.getDatabase(context)
        LocalRepositoryBarang(
            barangDao = database.barangDao(),
            suplierDao = database.suplierDao()
        )
    }

    override val repositorySuplier: RepositorySuplier by lazy {
        LocalRepositorySuplier(AppDatabase.getDatabase(context).suplierDao())
    }
}