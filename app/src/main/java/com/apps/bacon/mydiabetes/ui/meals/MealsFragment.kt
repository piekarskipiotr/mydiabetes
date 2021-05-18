package com.apps.bacon.mydiabetes.ui.meals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.ui.meal.MealActivity
import com.apps.bacon.mydiabetes.ui.stable.meal.StaticMealActivity
import com.apps.bacon.mydiabetes.adapters.PagingMealsAdapter
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MealsFragment : Fragment(), PagingMealsAdapter.OnMealClickListener {
    private lateinit var mealsAdapter: PagingMealsAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealViewModel: MealsViewModel by viewModels()

        initRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            mealViewModel.getPagingListOfMeals().collectLatest {
                mealsAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            mealsAdapter = PagingMealsAdapter(this@MealsFragment)
            adapter = mealsAdapter

        }
    }

    override fun onMealClick(mealId: Int, isEditable: Boolean) {
        if (isEditable) {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("MEAL_ID", mealId)
            startActivity(intent)
        } else {
            val intent = Intent(activity, StaticMealActivity::class.java)
            intent.putExtra("MEAL_ID", mealId)
            startActivity(intent)
        }
    }
}