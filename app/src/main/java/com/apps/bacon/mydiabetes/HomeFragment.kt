package com.apps.bacon.mydiabetes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.HomeRepository
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.HomeModelFactory
import com.apps.bacon.mydiabetes.viewmodel.HomeViewModel

class HomeFragment : Fragment(), ProductsAdapter.OnProductClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = AppDatabase.getInstance(requireContext())
        val repository = HomeRepository(database)
        val factory = HomeModelFactory(repository)
        val homeViewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)

        homeViewModel.currentTag.observe(viewLifecycleOwner, { selectedTab ->
            if(homeViewModel.getProductsByTag(selectedTab).hasObservers())
                homeViewModel.getProductsByTag(selectedTab).removeObservers(viewLifecycleOwner)

            homeViewModel.getProductsByTag(selectedTab).observe(viewLifecycleOwner, {
                initRecyclerView(it)
            })

        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView(data: List<Product>){
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter(data, context, this@HomeFragment)
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        TODO("Not yet implemented")
    }
}