package com.apps.bacon.mydiabetes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.databinding.ImageItemBinding
import com.bumptech.glide.Glide

class StaticImageAdapter : RecyclerView.Adapter<StaticImageAdapter.ViewHolder>() {
    private var data: List<String> = ArrayList()

    inner class ViewHolder(view: ImageItemBinding) : RecyclerView.ViewHolder(view.root) {
        val image: ImageView = view.productImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(data[position]).into(holder.image)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<String>) {
        data = dataList
        notifyDataSetChanged()
    }
}

