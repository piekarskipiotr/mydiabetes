package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ProductItemBinding
import com.bumptech.glide.Glide

class PagingProductsAdapter constructor(
    private val listener: OnProductClickListener
) : PagingDataAdapter<Product, PagingProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            getItem(bindingAdapterPosition)?.let { listener.onProductClick(it.id, it.isEditable) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagingProductsAdapter.ViewHolder, position: Int) {
        val product: Product? = getItem(position)
        product?.let {
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
    }

    interface OnProductClickListener {
        fun onProductClick(productId: Int, isEditable: Boolean)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Product, newItem: Product
            ) = oldItem == newItem
        }
    }
}