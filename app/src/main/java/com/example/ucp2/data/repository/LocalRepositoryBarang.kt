package com.example.ucp2.repository

import com.example.ucp2.data.dao.BarangDao
import com.example.ucp2.data.dao.SuplierDao
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import kotlinx.coroutines.flow.Flow

class LocalRepositoryBarang(
    private val barangDao: BarangDao,
    private val suplierDao: SuplierDao
) : RepositoryBarang {

    override suspend fun insertBarang(barang: Barang) {
        barangDao.insertBarang(barang)
    }

    override suspend fun updateBarang(barang: Barang) {
        barangDao.updateBarang(barang)
    }

    override suspend fun deleteBarang(barang: Barang) {
        barangDao.deleteBarang(barang)
    }

    override fun getAllBarang(): Flow<List<Barang>> {
        return barangDao.getAllBarang()
    }

    override fun getBarangDetail(id: String): Flow<Barang> {
        return barangDao.getBarangDetail(id)
    }

    override fun getSuplierByNama(nama: String): Flow<Suplier> {
        return suplierDao.geSuplierByNama(nama)
    }
}