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
import com.apps.bacon.mydiabetes.data.entities.StaticMeal
import com.apps.bacon.mydiabetes.databinding.MealItemBinding
import com.bumptech.glide.Glide

class PagingStaticMealsAdapter constructor(
    private val listener: OnMealClickListener
) : PagedListAdapter<StaticMeal, PagingStaticMealsAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            getItem(bindingAdapterPosition)?.let { listener.onStaticMealClick(it.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagingStaticMealsAdapter.ViewHolder, position: Int) {
        val meal: StaticMeal? = getItem(position)
        meal?.let {
            if (meal.icon == null)
                holder.icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_round_dinner_dining
                    )
                )
            else
                Glide.with(holder.itemView).load(meal.icon).into(holder.icon)

            holder.mealName.text = meal.name

            holder.carbohydrateExchangers.text = meal.carbohydrateExchangers.toString()
            holder.proteinFatExchangers.text = meal.proteinFatExchangers.toString()
            holder.calories.text = meal.calories.toString()
        }
    }

    interface OnMealClickListener {
        fun onStaticMealClick(mealId: Int)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StaticMeal>() {
            override fun areItemsTheSame(oldItem: StaticMeal, newItem: StaticMeal) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StaticMeal, newItem: StaticMeal) = oldItem == newItem
        }
    }
}