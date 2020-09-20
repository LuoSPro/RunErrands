package com.example.runerrands.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.runerrands.room.bean.Address
import com.example.runerrands.room.dao.AddressDao

@Database(entities = [Address::class],version = 1,exportSchema = false)
abstract class AddressDatabase: RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE: AddressDatabase? = null
        fun getInstance(context: Context)
                = INSTANCE ?: synchronized(this){
            INSTANCE ?: buildDatabase(context).also{INSTANCE = it}
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AddressDatabase::class.java, "address.db")
                .build()
    }

    abstract fun getAddressDao(): AddressDao
}