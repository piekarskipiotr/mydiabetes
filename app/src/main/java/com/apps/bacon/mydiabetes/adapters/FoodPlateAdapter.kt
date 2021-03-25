package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ProductItemFoodPlateBinding
import com.apps.bacon.mydiabetes.utilities.Calculations
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FoodPlateAdapter constructor(
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<FoodPlateAdapter.ViewHolder>() {
    private var data: List<Product> = ArrayList()

    inner class ViewHolder(view: ProductItemFoodPlateBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextInputEditText = view.measureTextInput
        val measureLayout: TextInputLayout = view.measureTextInputLayout
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
        val view =
            ProductItemFoodPlateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val product = data[position]

        if (product.icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else {
            if (product.isEditable)
                holder.icon.setImageURI(Uri.parse(product.icon))
            else
                Glide.with(holder.itemView).load(product.icon).into(holder.icon)
        }

        holder.productName.text = product.name
        if (product.weight == null) {
            holder.measure.setText(product.pieces.toString())
            holder.measureLayout.suffixText = context.resources.getString(R.string.pieces_shortcut)
            holder.measure.inputType =
                InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_CLASS_NUMBER
        } else {
            holder.measure.setText(product.weight.toString())
            holder.measureLayout.suffixText = "g/ml"
            holder.measure.inputType =
                InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        }

        holder.carbohydrateExchangers.text = product.carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = product.proteinFatExchangers.toString()
        holder.calories.text = product.calories.toString()

        holder.measure.onTextChanged {
            val value: String = when {
                it.isNullOrEmpty() -> "0"
                it[0] == '.' -> "0$it"
                else -> it.toString()
            }

            if (product.weight == null) {
                holder.carbohydrateExchangers.text = Calculations()
                    .carbohydrateExchangesByPieces(
                        product.carbohydrateExchangers,
                        product.pieces!!,
                        value.toInt()
                    )
                    .toString()
                holder.proteinFatExchangers.text = Calculations()
                    .proteinFatExchangersByPieces(
                        product.proteinFatExchangers, product.pieces!!, value.toInt()
                    )
                    .toString()
                holder.calories.text = Calculations()
                    .caloriesByPieces(
                        product.calories!!, product.pieces!!, value.toInt()
                    )
                    .toString()
            } else {
                holder.carbohydrateExchangers.text = Calculations()
                    .carbohydrateExchangesByWeight(
                        product.carbohydrateExchangers,
                        product.weight!!,
                        value.toDouble()
                    )
                    .toString()
                holder.proteinFatExchangers.text = Calculations()
                    .proteinFatExchangersByWeight(
                        product.proteinFatExchangers,
                        product.weight!!,
                        value.toDouble()
                    )
                    .toString()
                holder.calories.text = Calculations()
                    .caloriesByWeight(
                        product.calories!!, product.weight!!, value.toDouble()
                    )
                    .toString()
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<Product>) {
        data = dataList
        notifyDataSetChanged()
    }

    fun getData(): List<Product> {
        return data
    }

    fun getCarbohydrateExchangers(position: Int): Double {
        return data[position].carbohydrateExchangers
    }

    fun getProteinFat(position: Int): Double {
        return data[position].proteinFatExchangers
    }

    fun getCalories(position: Int): Double? {
        return data[position].calories
    }

    fun getProduct(position: Int): Product {
        return data[position]
    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit) {
        var dotHasBeenSet = false
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrEmpty()) {
                    dotHasBeenSet = if (p0.length == 3 && !dotHasBeenSet) {
                        p0.append(".")
                        true
                    } else {
                        false
                    }
                }
            }
        })
    }

    interface OnProductClickListener {
        fun onProductClick(productId: Int, isEditable: Boolean)
    }
}