package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.MealsAdapter
import com.apps.bacon.mydiabetes.adapters.StaticMealsAdapter
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealsFragment : Fragment(), MealsAdapter.OnMealClickListener,
    StaticMealsAdapter.OnMealClickListener {
    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var staticMealsAdapter: StaticMealsAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealViewModel: MealViewModel by viewModels()

        initRecyclerView()

        mealViewModel.getAll().observe(viewLifecycleOwner, {
            mealsAdapter.updateData(it)

        })

        mealViewModel.getAllStatics().observe(viewLifecycleOwner, {
            staticMealsAdapter.updateData(it)

        })

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
            mealsAdapter = MealsAdapter(this@MealsFragment)
            staticMealsAdapter = StaticMealsAdapter(this@MealsFragment)
            val conAdapter = ConcatAdapter(mealsAdapter, staticMealsAdapter)
            adapter = conAdapter

        }
    }

    override fun onMealClick(mealId: Int) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra("MEAL_ID", mealId)
        startActivity(intent)
    }

    override fun onMealsClick(mealId: Int) {
        val intent = Intent(activity, StaticMealActivity::class.java)
        intent.putExtra("MEAL_ID", mealId)
        startActivity(intent)
    }
}