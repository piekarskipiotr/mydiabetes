package com.apps.bacon.mydiabetes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.databinding.ProductItemBinding

class ProductsAdapter constructor(
    private val data: List<Product>,
    private val context: Context,
    private val listener: OnProductClickListener) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

   inner class ViewHolder(view: ProductItemBinding) : RecyclerView.ViewHolder(view.root), View.OnClickListener {
        val productName = view.productName
        val measure = view.measure
        val carbohydrateExchangers = view.carbohydrateExchangers
        val proteinFatExchangers = view.proteinFatExchangers
        val calories = view.calories


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onProductClick(data[itemViewType].id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = data[position].name
        if(data[position].weight == null)
            holder.measure.text = data[position].pieces.toString()
        else
            holder.measure.text = data[position].weight.toString()

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()

    }

    override fun getItemCount(): Int = data.size

    interface OnProductClickListener {
        fun onProductClick(productID: Int)
    }
}