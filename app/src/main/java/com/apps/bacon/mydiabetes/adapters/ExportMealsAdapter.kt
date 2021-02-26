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
import com.apps.bacon.mydiabetes.data.Meal
import com.apps.bacon.mydiabetes.databinding.MealToexportItemBinding
import java.util.ArrayList

class ExportMealsAdapter constructor(
    private val listener: OnExportMealListener
) : RecyclerView.Adapter<ExportMealsAdapter.ViewHolder>(){
    private var data: List<Meal> = ArrayList()
    private var dataToExport: MutableList<Meal> = ArrayList()
    private var checkedList = SparseBooleanArray()

    inner class ViewHolder(view: MealToexportItemBinding) : RecyclerView.ViewHolder(view.root), View.OnClickListener{
        val mealName: TextView = view.mealName
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.mealIcon
        val toExport: CheckBox = view.exportCheckBox

        init {
            itemView.setOnClickListener(this)
            toExport.setOnClickListener {
                listener.onMealCheckBoxClick(data[adapterPosition].id, toExport.isChecked)
            }
        }

        override fun onClick(v: View?) {
            listener.onMealClick(data[adapterPosition].id)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExportMealsAdapter.ViewHolder {
        val view = MealToexportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExportMealsAdapter.ViewHolder, position: Int) {
        if (data[position].icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else
            holder.icon.setImageURI(Uri.parse(data[position].icon))

        holder.mealName.text = data[position].name

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()

        if(checkedList.get(data[position].id, false)) {
            holder.toExport.isChecked = true
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

    fun getDataToExport(): List<Meal>{
        return dataToExport
    }

    fun updateData(dataList: List<Meal>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnExportMealListener {
        fun onMealClick(mealId: Int)
        fun onMealCheckBoxClick(mealId: Int, isChecked: Boolean)
    }

}