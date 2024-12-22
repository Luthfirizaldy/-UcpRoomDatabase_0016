package com.example.ucp2.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity  (tableName = "barang",
    foreignKeys = [
        ForeignKey(
            entity = Suplier::class,
            parentColumns = ["id"],
            childColumns = ["id_suplier"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_suplier"])]
)
data class Barang(
    @PrimaryKey
    val id : String,
    val nama : String,
    val deskripsi : String,
    val harga : String,
    val stok : String,
    val id_suplier : String
)