package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.MealsAdapter
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.MealViewModel

class MealsFragment : Fragment(), MealsAdapter.OnMealClickListener {
    private lateinit var mealsAdapter: MealsAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealViewModel: MealViewModel by viewModels()

        initRecyclerView()

        mealViewModel.getAll().observe(viewLifecycleOwner, {
            mealsAdapter.updateData(it)

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
            adapter = mealsAdapter

        }
    }

    override fun onMealClick(mealId: Int) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra("MEAL_ID", mealId)
        startActivity(intent)
    }
}