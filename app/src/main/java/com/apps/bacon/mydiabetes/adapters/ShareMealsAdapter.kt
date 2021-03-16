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
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.databinding.MealToShareItemBinding
import java.util.*

class ShareMealsAdapter constructor(
    private val listener: OnShareMealListener
) : RecyclerView.Adapter<ShareMealsAdapter.ViewHolder>() {
    private var data: List<Meal> = ArrayList()
    private var dataToShare: MutableList<Meal> = ArrayList()
    private var checkedList = SparseBooleanArray()

    inner class ViewHolder(view: MealToShareItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val mealName: TextView = view.mealName
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.mealIcon
        val toShare: CheckBox = view.exportCheckBox

        init {
            itemView.setOnClickListener(this)
            toShare.setOnClickListener {
                listener.onMealCheckBoxClick(data[bindingAdapterPosition].id, toShare.isChecked)
            }
        }

        override fun onClick(v: View?) {
            listener.onMealClick(data[bindingAdapterPosition].id)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShareMealsAdapter.ViewHolder {
        val view =
            MealToShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareMealsAdapter.ViewHolder, position: Int) {
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

        if (checkedList.get(data[position].id, false)) {
            holder.toShare.isChecked = true
        }

        holder.toShare.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedList.append(data[position].id, true)
                dataToShare.add(data[position])
            } else {
                checkedList.remove(data[position].id, true)
                dataToShare.remove(data[position])
            }
        }
    }

    override fun getItemCount() = data.size

    fun getDataToShare(): List<Meal> {
        return dataToShare
    }

    fun selectAllMeals() {
        for(meal in data){
            checkedList.append(meal.id, true)
            listener.onMealCheckBoxClick(meal.id, true)
        }
        notifyDataSetChanged()
    }

    fun updateData(dataList: List<Meal>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnShareMealListener {
        fun onMealClick(mealId: Int)
        fun onMealCheckBoxClick(mealId: Int, isChecked: Boolean)
    }
}