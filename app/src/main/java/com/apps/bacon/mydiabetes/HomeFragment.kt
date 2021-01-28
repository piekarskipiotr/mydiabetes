package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel by activityViewModels<HomeViewModel>()
        initRecyclerView()

        homeViewModel.currentTag.observe(viewLifecycleOwner, { selectedTab ->

            if(homeViewModel.getProductsByTag(selectedTab).hasObservers())
                homeViewModel.getProductsByTag(selectedTab).removeObservers(viewLifecycleOwner)

            if(selectedTab == 0){
                homeViewModel.getAll().observe(viewLifecycleOwner, {

                   productsAdapter.updateData(it)

                })
            }else{
                homeViewModel.getProductsByTag(selectedTab).observe(viewLifecycleOwner, {
                    productsAdapter.updateData(it)
                })
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initRecyclerView(){
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter( this@HomeFragment)
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
        startActivity(intent)
    }
}