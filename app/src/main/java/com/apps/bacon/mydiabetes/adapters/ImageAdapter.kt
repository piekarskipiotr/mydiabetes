package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.data.entities.Image
import com.apps.bacon.mydiabetes.databinding.ImageItemBinding

class ImageAdapter constructor(
    private val listener: OnImageClickListener
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var data: List<Image> = ArrayList()

    inner class ViewHolder(view: ImageItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnLongClickListener {
        val image: ImageView = view.productImage

        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            listener.onImageLongClick(data[bindingAdapterPosition])
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageURI(Uri.parse(data[position].image))
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<Image>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnImageClickListener {
        fun onImageLongClick(image: Image)
    }
}

