package com.example.runerrands.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runerrands.R
import com.xuexiang.xui.widget.imageview.RadiusImageView

class HomeBannerAdapter: RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {
    private lateinit var mData: MutableList<Int>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPic: RadiusImageView = itemView.findViewById(R.id.iv_ban_home)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_banner_home, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivPic.setImageResource(mData[position % mData.size])
    }

    override fun getItemCount() = mData.size

    public fun setData(data: List<Int>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }
}