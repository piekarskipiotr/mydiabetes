package com.apps.bacon.mydiabetes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ProductsAdapter
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.databinding.ActivitySearchBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var allProducts: List<Product>
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

        val searchList = mutableListOf<Product>()

        initRecyclerView()

        productViewModel.getAll().observe(this, {
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
                        "${resources.getString(R.string.enter_product_name_to_find)} \n \n ${
                            resources.getString(
                                R.string.or
                            )
                        } \n \n ${resources.getString(R.string.scan_barcode)}"
                    binding.textMessage.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE

                }
                else -> {
                    binding.textMessage.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.VISIBLE
                    productsAdapter.updateData(searchList)

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
            adapter = productsAdapter

        }
    }

    override fun onProductClick(productID: Int) {
        intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productID)
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
                        val product =
                            productViewModel.getProductByBarcode(it.getStringExtra("BARCODE")!!)

                        if (product == null) {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.search_by_barcode_error_message),
                                Toast.LENGTH_LONG
                            ).show()
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