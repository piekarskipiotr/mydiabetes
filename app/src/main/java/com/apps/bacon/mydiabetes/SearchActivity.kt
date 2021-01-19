package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.AppDatabase
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.data.SearchRepository
import com.apps.bacon.mydiabetes.databinding.ActivitySearchBinding
import com.apps.bacon.mydiabetes.viewmodel.SearchModelFactory
import com.apps.bacon.mydiabetes.viewmodel.SearchViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class SearchActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var allProducts: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchList = mutableListOf<Product>()
        val database = AppDatabase.getInstance(this)
        val repository = SearchRepository(database)
        val factory = SearchModelFactory(repository)
        val searchViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        searchViewModel.getAll().observe(this, {
            allProducts = it
        })

        binding.searchTextInput.onTextChanged {
            searchList.clear()
            for(i in allProducts){
                if(i.name.toLowerCase(Locale.getDefault())
                        .contains(it.toString().toLowerCase(Locale.getDefault()))){
                    searchList.add(i)
                }
            }
            if(searchList.isEmpty()){
                binding.noProductMessageText.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.GONE

            }else{
                binding.noProductMessageText.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                initRecyclerView(searchList)
            }


        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initRecyclerView(data: List<Product>){
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter(data, context, this@SearchActivity)
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
        startActivity(intent)

    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit){
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}