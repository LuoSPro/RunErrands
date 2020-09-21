package com.example.runerrands.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.runerrands.R
import com.example.runerrands.room.bean.Address

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private val mData: MutableList<Address> = mutableListOf()
    private lateinit var onItemClickListener: OnItemClickListener


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address_address)
        val tvName: TextView = itemView.findViewById(R.id.tv_name_address)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_phone_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_address,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAddress.text = mData[position].address
        holder.tvName.text = mData[position].name
        holder.tvPhone.text = mData[position].phone
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(mData[position])
        }
    }

    override fun getItemCount(): Int = mData.size

    fun setData(list: MutableList<Address>){
        this.mData.clear()
        this.mData.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    interface OnItemClickListener{
        fun onClick(address: Address)
    }
}