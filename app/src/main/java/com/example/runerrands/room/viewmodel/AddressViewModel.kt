package com.example.runerrands.room.viewmodel

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.runerrands.room.bean.Address
import com.example.runerrands.room.dao.AddressDao
import com.example.runerrands.room.database.AddressDataBase

class AddressViewModel(context: Context) {

    //private val addressDao = AddressDatabase.getInstance(context).getAddressDao()
    private val addressDao = AddressDataBase.get(context).addressDao

    fun addAddress(vararg address: Address){
        InsertAddress(addressDao).execute(*address)
    }

    fun deleteAddress(vararg address: Address){
        DeleteAddress(addressDao).execute(*address)
    }

    var addressLive: LiveData<List<Address>> = addressDao.getAllAddress()

    //kotlin中，内部类默认是静态的，使用inner修饰后，变成非静态内部类
    class InsertAddress(addressDao: AddressDao): AsyncTask<Address, Void, Void>() {
        private val addressDao: AddressDao = addressDao
        override fun doInBackground(vararg address: Address): Void? {
            addressDao.addAddress(*address)
            return null
        }

    }

    class DeleteAddress(addressDao: AddressDao): AsyncTask<Address, Void, Void>() {
        private val addressDao: AddressDao = addressDao
        override fun doInBackground(vararg address: Address): Void? {
            addressDao.deleteAddress(*address)
            return null
        }

    }

}