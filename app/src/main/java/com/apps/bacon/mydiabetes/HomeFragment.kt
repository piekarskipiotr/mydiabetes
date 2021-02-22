package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.databinding.FragmentHomeBinding
import com.apps.bacon.mydiabetes.viewmodel.HomeViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel by activityViewModels<HomeViewModel>()
        val productViewModel: ProductViewModel by viewModels()
        initRecyclerView()

        homeViewModel.currentTag.observe(viewLifecycleOwner, { selectedTab ->

            if (productViewModel.getAllByTag(selectedTab).hasObservers())
                productViewModel.getAllByTag(selectedTab).removeObservers(viewLifecycleOwner)

            if (selectedTab == 0) {
                productViewModel.getAll().observe(viewLifecycleOwner, {
                    productsAdapter.updateData(it)

                })
            } else {
                productViewModel.getAllByTag(selectedTab).observe(viewLifecycleOwner, {
                    productsAdapter.updateData(it)
                })
            }
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
            productsAdapter = ProductsAdapter(this@HomeFragment)
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productId: Int) {
        val intent = Intent(activity, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }
}