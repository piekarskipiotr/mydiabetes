package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.util.remove
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.databinding.ProductToexportItemBinding
import java.util.ArrayList

class ExportProductsAdapter constructor(
    private val listener: OnExportProductListener
) : RecyclerView.Adapter<ExportProductsAdapter.ViewHolder>(){
    private var data: List<Product> = ArrayList()
    private var dataToExport: MutableList<Product> = ArrayList()
    private var checkedList = SparseBooleanArray()
    private var isFromMealList = SparseBooleanArray()

    inner class ViewHolder(view: ProductToexportItemBinding) : RecyclerView.ViewHolder(view.root), View.OnClickListener{
        val productName: TextView = view.productName
        val measure: TextView = view.measure
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.productIcon
        val toExport: CheckBox = view.exportCheckBox

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onProductClick(data[adapterPosition].id)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExportProductsAdapter.ViewHolder {
        val view = ProductToexportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExportProductsAdapter.ViewHolder, position: Int) {
        if (data[position].icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else
            holder.icon.setImageURI(Uri.parse(data[position].icon))

        holder.productName.text = data[position].name
        if (data[position].weight == null)
            holder.measure.text = data[position].pieces.toString()
        else
            holder.measure.text = data[position].weight.toString()

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()

        if(checkedList.get(data[position].id, false)) {
            holder.toExport.isChecked = true
            holder.toExport.isClickable = !isFromMealList.get(data[position].id, false)
        }

        holder.toExport.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                checkedList.append(data[position].id, true)
                dataToExport.add(data[position])
            }else{
                checkedList.remove(data[position].id, true)
                dataToExport.remove(data[position])
            }
        }
    }

    override fun getItemCount() = data.size

    fun getDataToExport(): List<Product>{
        return dataToExport
    }

    fun addProductsThatAreConnectedWithMeal(listOfProducts: List<Product>){
        for(product in listOfProducts){
            if(dataToExport.find { it.id == product.id } == null){
                dataToExport.add(product)
                checkedList.append(product.id, true)
            }

            isFromMealList.append(product.id, true)
        }
    }

    fun removeProductsThatAreConnectedWithMeal(listOfProducts: List<Product>){
        for(product in listOfProducts){
            dataToExport.remove(product)
            checkedList.remove(product.id, true)
            isFromMealList.remove(product.id, true)
        }
    }

    fun updateData(dataList: List<Product>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnExportProductListener {
        fun onProductClick(productId: Int)
    }

}