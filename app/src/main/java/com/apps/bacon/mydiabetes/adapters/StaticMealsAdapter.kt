package com.apps.bacon.mydiabetes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.StaticMeal
import com.apps.bacon.mydiabetes.databinding.MealItemBinding
import com.bumptech.glide.Glide
import java.util.*

class StaticMealsAdapter constructor(
    private val listener: OnMealClickListener
) : RecyclerView.Adapter<StaticMealsAdapter.ViewHolder>() {
    private var data: List<StaticMeal> = ArrayList()

    inner class ViewHolder(view: MealItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val mealName: TextView = view.mealName
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.mealIcon

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onMealsClick(data[bindingAdapterPosition].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else
            Glide.with(holder.itemView).load(data[position].icon).into(holder.icon)

        holder.mealName.text = data[position].name

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()
    }

    override fun getItemCount() = data.size

    fun updateData(dataList: List<StaticMeal>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnMealClickListener {
        fun onMealsClick(mealId: Int)
    }
}