package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.databinding.MealItemBinding
import com.bumptech.glide.Glide
import java.util.*

class MealsAdapter constructor(
    private val listener: OnMealClickListener
) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {
    private var data: List<Meal> = ArrayList()

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
            listener.onMealClick(data[bindingAdapterPosition].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = data[position]

        if (meal.icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else{
            if(meal.isEditable)
                holder.icon.setImageURI(Uri.parse(meal.icon))
            else
                Glide.with(holder.itemView).load(meal.icon).into(holder.icon)
        }

        holder.mealName.text = meal.name

        holder.carbohydrateExchangers.text = meal.carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = meal.proteinFatExchangers.toString()
        holder.calories.text = meal.calories.toString()
    }

    override fun getItemCount() = data.size

    fun updateData(dataList: List<Meal>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnMealClickListener {
        fun onMealClick(mealId: Int)
    }
}