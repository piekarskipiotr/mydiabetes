package com.apps.bacon.mydiabetes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.Product
import kotlinx.android.synthetic.main.product_item.view.*

class ProductsAdapter constructor(
    private val data: List<Product>,
    private val listener: OnProductClickListener) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextView = view.measure
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onProductClick(data[itemViewType].id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
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