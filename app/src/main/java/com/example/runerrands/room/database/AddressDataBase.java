package com.example.runerrands.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.runerrands.room.bean.Address;
import com.example.runerrands.room.dao.AddressDao;


@Database(entities = {Address.class},version = 1,exportSchema = false)
public abstract class AddressDataBase extends RoomDatabase {

    private static AddressDataBase addressDataBase;

    public static synchronized AddressDataBase get(Context context){
        if (addressDataBase == null){
            addressDataBase = Room.databaseBuilder(context.getApplicationContext(),AddressDataBase.class,"address.db")
                    .build();

        }
        return addressDataBase;
    }


    public abstract AddressDao getAddressDao();
}
