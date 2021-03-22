package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ProductItemBinding
import com.bumptech.glide.Glide
import java.util.*

class ProductsAdapter constructor(
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    private var data: List<Product> = ArrayList()

    inner class ViewHolder(view: ProductItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextView = view.measure
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.productIcon

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onProductClick(
                data[bindingAdapterPosition].id,
                data[bindingAdapterPosition].isEditable
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = data[position]

        if (product.icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else{
            if(product.isEditable)
                holder.icon.setImageURI(Uri.parse(product.icon))
            else
                Glide.with(holder.itemView).load(product.icon).into(holder.icon)
        }

        holder.productName.text = product.name
        if (product.weight == null)
            holder.measure.text = product.pieces.toString()
        else
            holder.measure.text = product.weight.toString()

        holder.carbohydrateExchangers.text = product.carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = product.proteinFatExchangers.toString()
        holder.calories.text = product.calories.toString()

    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<Product>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnProductClickListener {
        fun onProductClick(productId: Int, isEditable: Boolean)
    }
}