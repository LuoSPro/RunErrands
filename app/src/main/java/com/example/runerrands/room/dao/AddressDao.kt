package com.example.runerrands.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.runerrands.room.bean.Address

@Dao
interface AddressDao {

    @Insert
    fun addAddress(vararg address: Address)

    @Delete
    fun deleteAddress(vararg address: Address)

    @Query("SELECT * FROM Address ORDER BY ID DESC")
    fun getAllAddress(): LiveData<List<Address>>
}