package com.example.ucp2.data.database

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2.data.dao.BarangDao
import com.example.ucp2.data.dao.SuplierDao
import com.example.ucp2.data.entity.Barang
import com.example.ucp2.data.entity.Suplier

@Database(entities = [ Barang::class, Suplier ::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun barangDao() : BarangDao
    abstract fun suplierDao() : SuplierDao

    companion object{
        @Volatile
        private var Instances : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return Instances ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instances = it }
            }
        }
    }
}