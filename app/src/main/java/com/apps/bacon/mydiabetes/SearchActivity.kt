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
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

private const val REQUEST_CODE_GET_BARCODE = 2

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), ProductsAdapter.OnProductClickListener {
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var allProducts: List<Product>
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search)

        val searchList = mutableListOf<Product>()

        initRecyclerView()

        productViewModel.getAll().observe(this, {
            allProducts = it
        })

        searchTextInput.onTextChanged {
            searchList.clear()
            for(i in allProducts){
                if(i.name.toLowerCase(Locale.getDefault())
                        .contains(it.toString().toLowerCase(Locale.getDefault()))){
                    searchList.add(i)
                }
            }

            when {
                searchList.isEmpty() -> {
                    textMessage.text = "Brak produktu : ("
                    textMessage.visibility = View.VISIBLE
                    searchRecyclerView.visibility = View.GONE

                }
                searchTextInput.text.isNullOrEmpty() -> {
                    textMessage.text = "Wpisz nazwę produktu aby go odszukać \n \n lub \n \n Zeskanuj kod kreskowy"
                    textMessage.visibility = View.VISIBLE
                    searchRecyclerView.visibility = View.GONE

                }
                else -> {
                    textMessage.visibility = View.GONE
                    searchRecyclerView.visibility = View.VISIBLE
                    productsAdapter.updateData(searchList)

                }
            }

        }

        scanButton.setOnClickListener {
            intent = Intent(this, ScannerCameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initRecyclerView(){
        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
            }
            productsAdapter = ProductsAdapter( this@SearchActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_GET_BARCODE -> {
                if(resultCode == RESULT_OK){
                    data?.let {
                        val product = productViewModel.getProductByBarcode(it.getStringExtra("BARCODE")!!)

                        if(product == null){
                            Toast.makeText(this, "Nie ma takiego produktu z takim kodem kreskowym!", Toast.LENGTH_LONG).show()
                        }else{
                            onProductClick(product.id)
                        }
                    }
                }
            }
        }
    }
}