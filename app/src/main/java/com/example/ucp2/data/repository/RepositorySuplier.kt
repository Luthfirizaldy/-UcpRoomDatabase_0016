package com.example.ucp2.repository

import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import kotlinx.coroutines.flow.Flow

interface RepositorySuplier {

    suspend fun insertSuplier(suplier: Suplier)

    fun getAllSuplier(): Flow<List<Suplier>>

    fun getSuplierById(id: String): Flow<Suplier>
}