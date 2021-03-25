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
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ProductToShareItemBinding
import java.util.*

class ShareProductsAdapter constructor(
    private val listener: OnShareProductListener
) : RecyclerView.Adapter<ShareProductsAdapter.ViewHolder>() {
    private var data: List<Product> = ArrayList()
    private var dataToShare: MutableList<Product> = ArrayList()
    private var checkedList = SparseBooleanArray()
    private var isFromMealList = SparseBooleanArray()

    inner class ViewHolder(view: ProductToShareItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextView = view.measure
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.productIcon
        val toShare: CheckBox = view.exportCheckBox

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onProductClick(data[bindingAdapterPosition].id)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShareProductsAdapter.ViewHolder {
        val view =
            ProductToShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareProductsAdapter.ViewHolder, position: Int) {
        val product = data[position]

        if (product.icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else
            holder.icon.setImageURI(Uri.parse(product.icon))

        holder.productName.text = product.name
        if (product.weight == null)
            holder.measure.text = product.pieces.toString()
        else
            holder.measure.text = product.weight.toString()

        holder.carbohydrateExchangers.text = product.carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = product.proteinFatExchangers.toString()
        holder.calories.text = product.calories.toString()

        if (checkedList.get(product.id, false)) {
            holder.toShare.isChecked = true
            holder.toShare.isClickable = !isFromMealList.get(product.id, false)
        }

        holder.toShare.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedList.append(product.id, true)
                dataToShare.add(product)
            } else {
                checkedList.remove(product.id, true)
                dataToShare.remove(product)
            }
        }
    }

    override fun getItemCount() = data.size

    fun getDataToShare(): List<Product> {
        return dataToShare
    }

    fun addProductsThatAreConnectedWithMeal(listOfProducts: List<Product>) {
        for (product in listOfProducts) {
            if (dataToShare.find { it.id == product.id } == null) {
                dataToShare.add(product)
                checkedList.append(product.id, true)
            }

            isFromMealList.append(product.id, true)
        }

        notifyDataSetChanged()
    }

    fun removeProductsThatAreConnectedWithMeal(listOfProducts: List<Product>) {
        for (product in listOfProducts) {
            dataToShare.remove(product)
            checkedList.remove(product.id, true)
            isFromMealList.remove(product.id, true)
        }

        notifyDataSetChanged()
    }

    fun selectAllProducts() {
        for (product in data) {
            if (!checkedList.get(product.id, false)) {
                checkedList.append(product.id, true)
            }
        }

        notifyDataSetChanged()
    }

    fun updateData(dataList: List<Product>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnShareProductListener {
        fun onProductClick(productId: Int)
    }
}