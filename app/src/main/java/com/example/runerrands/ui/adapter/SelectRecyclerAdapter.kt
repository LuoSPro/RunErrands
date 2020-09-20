package com.example.runerrands.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.runerrands.R
import com.example.runerrands.model.bean.SelectInfo

class SelectRecyclerAdapter: RecyclerView.Adapter<SelectRecyclerAdapter.ViewHolder>() {

    private val mData: MutableList<SelectInfo> = mutableListOf()
    lateinit var mListener: OnItemClickListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvAddress = itemView.findViewById<TextView>(R.id.tv_address_search_select)
        val tvCity = itemView.findViewById<TextView>(R.id.tv_city_search_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_select,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAddress.text = mData[position].adders
        holder.tvCity.text = mData[position].city
        holder.itemView.setOnClickListener {
            mListener.let {
                mListener.onClick(mData[position])
            }
        }
    }

    override fun getItemCount() = mData.size

    fun setData(list: List<SelectInfo>){
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mListener = listener
    }

    interface OnItemClickListener{
        fun onClick(selectInfo: SelectInfo)
    }
}