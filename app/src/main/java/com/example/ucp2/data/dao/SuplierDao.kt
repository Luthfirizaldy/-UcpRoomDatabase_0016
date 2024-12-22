package com.example.ucp2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SuplierDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuplier(suplier: Suplier)

    @Query("SELECT * FROM suplier")
    fun getAllSuplier():Flow<List<Suplier>>

    @Query("SELECT * FROM suplier WHERE id = :id")
    fun getSuplierById(id : String) : Flow<Suplier>

    @Transaction
    @Query("SELECT * FROM suplier WHERE nama = :nama")
    fun  geSuplierByNama(nama : String) : Flow<Suplier>
}