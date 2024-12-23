package com.example.ucp2.repository

import com.example.ucp2.data.dao.BarangDao
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import kotlinx.coroutines.flow.Flow

interface RepositoryBarang {

    suspend fun insertBarang(barang: Barang)

    suspend fun updateBarang(barang: Barang)

    suspend fun deleteBarang(barang: Barang)

    fun getAllBarang(): Flow<List<Barang>>

    fun getBarangDetail(id: String): Flow<Barang>

    fun getSuplierByNama(nama : String) : Flow<Suplier>
}