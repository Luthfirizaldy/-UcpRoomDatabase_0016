package com.example.ucp2.repository

import com.example.ucp2.data.dao.SuplierDao
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import com.example.ucp2.repository.RepositorySuplier
import kotlinx.coroutines.flow.Flow

class LocalRepositorySuplier(
    private val suplierDao: SuplierDao
) : RepositorySuplier {

    // Menyimpan data Suplier
    override suspend fun insertSuplier(suplier: Suplier) {
        suplierDao.insertSuplier(suplier)
    }

    // Mendapatkan semua supplier
    override fun getAllSuplier(): Flow<List<Suplier>>{
        return suplierDao.getAllSuplier()
    }

    // Mendapatkan detail supplier berdasarkan ID
    override fun getSuplierById(id: String): Flow<Suplier> {
        return suplierDao.getSuplierById(id)
    }
}