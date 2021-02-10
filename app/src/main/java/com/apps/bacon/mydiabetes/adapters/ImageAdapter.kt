package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.Image
import kotlinx.android.synthetic.main.image_item.view.*

class ImageAdapter constructor(
    private val listener: OnImageClickListener
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){
    private var data: List<Image> = ArrayList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener{
        val image: ImageView = view.productImage

        init {
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            listener.onImageLongClick(data[adapterPosition])
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageURI(Uri.parse(data[position].image))
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<Image>){
        data = dataList
        notifyDataSetChanged()
    }

    interface OnImageClickListener {
        fun onImageLongClick(image: Image)

    }

}

