package com.apps.bacon.mydiabetes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import com.apps.bacon.mydiabetes.databinding.ProductItemBinding
import com.bumptech.glide.Glide

class PagingStaticProductsAdapter constructor(
    private val listener: OnProductClickListener
) : PagedListAdapter<StaticProduct, PagingStaticProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            getItem(bindingAdapterPosition)?.let { listener.onStaticProductClick(it.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagingStaticProductsAdapter.ViewHolder, position: Int) {
        val product: StaticProduct? = getItem(position)
        product?.let {
            if (product.icon == null)
                holder.icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_round_dinner_dining
                    )
                )
            else
                Glide.with(holder.itemView).load(product.icon).into(holder.icon)

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
        fun onStaticProductClick(productId: Int)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StaticProduct>() {
            override fun areItemsTheSame(oldItem: StaticProduct, newItem: StaticProduct) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StaticProduct, newItem: StaticProduct) = oldItem == newItem
        }
    }
}