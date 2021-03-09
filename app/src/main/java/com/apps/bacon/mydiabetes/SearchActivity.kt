package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.adapters.StaticProductsAdapter
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import com.apps.bacon.mydiabetes.databinding.ActivitySearchBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener,
    StaticProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var staticProductsAdapter: StaticProductsAdapter
    private lateinit var allProducts: List<Product>
    private lateinit var allStaticProducts: List<StaticProduct>
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textMessage.text =
            "${resources.getString(R.string.enter_product_name_to_find)} \n ${resources.getString(R.string.or)} \n ${
                resources.getString(
                    R.string.scan_barcode
                )
            }"

        val searchList = mutableListOf<Product>()
        val staticSearchList = mutableListOf<StaticProduct>()

        initRecyclerView()

        productViewModel.getAll().observe(this, {
            allProducts = it
        })

        productViewModel.getAllStatics().observe(this, {
            allStaticProducts = it
        })

        binding.searchTextInput.onTextChanged {
            searchList.clear()
            for (i in allProducts) {
                if (i.name.toLowerCase(Locale.getDefault())
                        .contains(it.toString().toLowerCase(Locale.getDefault()))
                ) {
                    searchList.add(i)
                }
            }

            for (i in allStaticProducts) {
                if (i.name.toLowerCase(Locale.getDefault())
                        .contains(it.toString().toLowerCase(Locale.getDefault()))
                ) {
                    staticSearchList.add(i)
                }
            }

            when {
                searchList.isEmpty() and staticSearchList.isEmpty() -> {
                    binding.textMessage.text = resources.getString(R.string.lack_product)
                    binding.textMessage.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE

                }
                binding.searchTextInput.text.isNullOrEmpty() -> {
                    binding.textMessage.text =
                        "${resources.getString(R.string.enter_product_name_to_find)} \n ${
                            resources.getString(
                                R.string.or
                            )
                        } \n ${resources.getString(R.string.scan_barcode)}"
                    binding.textMessage.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE

                }
                else -> {
                    binding.textMessage.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.VISIBLE

                    if (searchList.isNotEmpty()) {
                        productsAdapter.updateData(searchList)
                    }

                    if (staticSearchList.isNotEmpty()) {
                        staticProductsAdapter.updateData(staticSearchList)
                    }
                }
            }

        }

        binding.scanButton.setOnClickListener {
            intent = Intent(this, ScannerCameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initRecyclerView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
            }
            productsAdapter = ProductsAdapter(this@SearchActivity)
            staticProductsAdapter = StaticProductsAdapter(this@SearchActivity)
            val conAdapter = ConcatAdapter(productsAdapter, staticProductsAdapter)
            adapter = conAdapter

        }
    }

    override fun onProductClick(productId: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    override fun onStaticProductClick(productId: Int) {
        intent = Intent(this, StaticProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GET_BARCODE -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val barcode = it.getStringExtra("BARCODE")!!
                        val product =
                            productViewModel.getProductByBarcode(barcode)

                        if (product == null) {
                            val staticProduct =
                                productViewModel.getStaticProductByBarcode(barcode)
                            if (staticProduct == null) {
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.search_by_barcode_error_message),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                onStaticProductClick(staticProduct.id)
                            }

                        } else {
                            onProductClick(product.id)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GET_BARCODE = 2
    }


}