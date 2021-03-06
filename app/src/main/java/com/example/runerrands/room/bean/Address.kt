package com.example.runerrands.room.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(var type: Int, var address: String, val name: String, val phone: String,
                   val sex: String, val latitude: Double, val longitude: Double) {

    @PrimaryKey(autoGenerate = true)
    private var id: Int = 0

    //必须得有setter和getter
    fun setId(id: Int){
        this.id = id
    }

    fun getId(): Int{
        return id
    }
}