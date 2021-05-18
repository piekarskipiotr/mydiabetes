package com.apps.bacon.mydiabetes.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.ui.stable.product.StaticProductActivity
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ActivitySearchBinding
import com.apps.bacon.mydiabetes.ui.camera.ScannerCameraActivity
import com.apps.bacon.mydiabetes.ui.product.ProductActivity
import com.apps.bacon.mydiabetes.utilities.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchActivity : BaseActivity(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var allProducts: List<Product>
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("SetTextI18n")
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

        initRecyclerView()

        searchViewModel.getAll().observe(this, {
            allProducts = it
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


            when {
                searchList.isEmpty() -> {
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
                }
            }

        }

        binding.scanButton.setOnClickListener {
            intent = Intent(this, ScannerCameraActivity::class.java)
            getBarcode.launch(intent)
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
            adapter = productsAdapter
        }
    }

    override fun onProductClick(productId: Int, isEditable: Boolean) {
        if (isEditable) {
            intent = Intent(this, ProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", productId)
            startActivity(intent)
        } else {
            intent = Intent(this, StaticProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", productId)
            startActivity(intent)
        }
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

    private val getBarcode =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                activityResult.data?.let {
                    val barcode = it.getStringExtra("BARCODE")!!
                    val product =
                        searchViewModel.getProductByBarcode(barcode)

                    if (product == null) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.search_by_barcode_error_message),
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        onProductClick(product.id, product.isEditable)
                    }
                }
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}