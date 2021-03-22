package com.apps.bacon.mydiabetes.adapters

import android.net.Uri
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
import com.apps.bacon.mydiabetes.data.entities.Meal
import com.apps.bacon.mydiabetes.databinding.MealItemBinding

class PagingMealsAdapter constructor(
    private val listener: OnMealClickListener
) : PagedListAdapter<Meal, PagingMealsAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            getItem(bindingAdapterPosition)?.let { listener.onMealClick(it.id, it.isEditable) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagingMealsAdapter.ViewHolder, position: Int) {
        val meal: Meal? = getItem(position)
        meal?.let {
            if (meal.icon == null)
                holder.icon.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_round_dinner_dining
                    )
                )
            else
                holder.icon.setImageURI(Uri.parse(meal.icon))

            holder.mealName.text = meal.name

            holder.carbohydrateExchangers.text = meal.carbohydrateExchangers.toString()
            holder.proteinFatExchangers.text = meal.proteinFatExchangers.toString()
            holder.calories.text = meal.calories.toString()
        }
    }

    interface OnMealClickListener {
        fun onMealClick(mealId: Int, isEditable: Boolean)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Meal, newItem: Meal) = oldItem == newItem
        }
    }
}