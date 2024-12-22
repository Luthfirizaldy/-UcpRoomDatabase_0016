
package com.example.ucp2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.ucp2.data.entity.Barang
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarang(barang: Barang)

    @Delete
    suspend fun deleteBarang(barang:Barang)

    @Update
    suspend fun updateBarang(barang: Barang)

    @Transaction
    @Query("SELECT * FROM barang")
    fun  getAllBarang():Flow<List<Barang>>

    @Transaction
    @Query("SELECT * FROM barang WHERE id = :id")
    fun getBarangDetail(id : String) : Flow<Barang>


}
