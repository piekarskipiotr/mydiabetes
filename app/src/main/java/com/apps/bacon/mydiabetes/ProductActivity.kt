package com.apps.bacon.mydiabetes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.ImageAdapter
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.DialogAddImageBinding
import com.apps.bacon.mydiabetes.databinding.DialogDeleteProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogManagerImageBinding
import com.apps.bacon.mydiabetes.databinding.DialogManagerTagBinding
import com.apps.bacon.mydiabetes.viewmodel.ImageViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.backButton
import kotlinx.android.synthetic.main.activity_product.deleteButton
import kotlinx.android.synthetic.main.dialog_delete_product.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProductActivity : AppCompatActivity(), ImageAdapter.OnImageClickListener {
    private lateinit var product: Product
    private val productViewModel: ProductViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()
    private val imageViewModel: ImageViewModel by viewModels()
    private lateinit var imagesAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val bottomSheetDialogCameraViewBinding = DialogAddImageBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        product = productViewModel.getProduct(productId)
        initRecyclerView()
        setProductInfo()

        imageViewModel.getImageByProductId(product.id).observe(this, {
            imagesAdapter.updateData(it)
        })

        productName.setOnClickListener {
            intent = Intent(this, ChangeProductNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PRODUCT_NAME)
        }

        scanBarcodeButton.setOnClickListener {
            intent = Intent(this, CameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        manualBarcode.setOnClickListener {
            intent = Intent(this, ProductBarcodeActivity::class.java)
            if(product.barcode != null)
                intent.putExtra("BARCODE", false)

            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        takePhotoButton.setOnClickListener{
            bottomSheetDialog.setContentView(bottomSheetDialogCameraViewBinding.root)
            bottomSheetDialog.show()

            bottomSheetDialogCameraViewBinding.cameraButton.setOnClickListener {
                intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_GET_IMAGE)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialogCameraViewBinding.galleryButton.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK)
                gallery.type = "image/*"

                startActivityForResult(gallery, REQUEST_CODE_GET_IMAGE_FROM_GALLERY)
                bottomSheetDialog.dismiss()
            }
        }


        addButton.setOnClickListener {
            productViewModel.update(product.apply {
                inFoodPlate = true
            })

            Toast.makeText(this, resources.getString(R.string.product_added), Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        deleteButton.setOnClickListener {
            dialogDeleteProduct()
        }
    }

    private fun initRecyclerView(){
        photosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            imagesAdapter = ImageAdapter(this@ProductActivity)
            adapter = imagesAdapter
        }
    }

    private fun setProductInfo(){
        productName.text = product.name
        val measureText: String = if(product.weight != null)
            "(${resources.getString(R.string.for_smth)} ${product.weight} g/ml)"
        else
            "(${resources.getString(R.string.for_smth)} ${product.pieces} ${resources.getString(R.string.pieces_shortcut)})"

        measureOfProductValues.text = measureText
        measureOfProductExchangers.text = measureText

        carbohydrates.text = product.carbohydrates.toString()
        calories.text = product.calories.toString()
        if(product.protein != null){
            protein.text = product.protein.toString()
            fat.text = product.fat.toString()
        }else{
            proteinContainer.visibility = View.GONE
            fatContainer.visibility = View.GONE
        }

        pieChart(product.carbohydrateExchangers, product.proteinFatExchangers)

        if(product.tag == null)
            addChip(resources.getString(R.string.set_the_tag), 0)
        else{
            val tag = tagViewModel.getTagById(product.tag!!)
            addChip(tag.name, tag.id)
        }

        if(product.barcode == null){
            manualBarcode.text = resources.getString(R.string.barcode_manually)
        }else{
            manualBarcode.text = product.barcode
        }
    }

    private fun addChip(label: String, ID: Int){
        tagChipContainer.removeAllViewsInLayout()
        tagChipContainer.addChip(label, ID)
        tagChipContainer[0].setOnClickListener {
            if(product.tag == null){
                intent = Intent(this, AddTagActivity::class.java)
                intent.putExtra("TAG_MANAGER", true)
                startActivityForResult(intent, REQUEST_CODE_GET_TAG)
            }else
                dialogManagerTag(label)

        }

    }

    private fun ChipGroup.addChip(label: String, ID: Int){
        Chip(this@ProductActivity, null, R.attr.CustomChip).apply {
            id = ID
            text = label
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }

    private fun dialogDeleteProduct(){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteProductBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.productNameText.text = product.name

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            productViewModel.delete(product)
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    private fun dialogManagerTag(tagName: String){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogManagerTagBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tagNameText.text = tagName

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            product.tag = null
            productViewModel.update(product)
            setProductInfo()
            alertDialog.dismiss()
        }

        dialogBinding.changeButton.setOnClickListener {
            intent = Intent(this, AddTagActivity::class.java)
            intent.putExtra("TAG_MANAGER", true)
            startActivityForResult(intent, REQUEST_CODE_GET_TAG)
            alertDialog.dismiss()

        }
        alertDialog.show()
    }

    private fun dialogImageManager(image: Image){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogManagerImageBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.imagePreview.setImageURI(Uri.parse(image.image))

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            product.icon = null
            productViewModel.update(product)
            imageViewModel.delete(image)

            alertDialog.dismiss()
        }

        dialogBinding.setAsIconButton.setOnClickListener {
            product.icon = image.image
            productViewModel.update(product)
            alertDialog.dismiss()

        }


        alertDialog.show()
    }

    private fun pieChart(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val pieChart: PieChart = pieChart
        val data = ArrayList<PieEntry>()
        if(carbohydrateExchangers != 0.0)
            data.add(PieEntry(carbohydrateExchangers.toFloat(), resources.getString(R.string.pie_label_carbohydrate)))
        if(proteinFatExchangers != 0.0)
            data.add(PieEntry(proteinFatExchangers.toFloat(), resources.getString(R.string.pie_label_protein_fat)))

        val dataSet = PieDataSet(data, "")
        dataSet.setColors(
            ContextCompat.getColor(this, R.color.strong_yellow),
            ContextCompat.getColor(this, R.color.blue_purple)
        )

        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.black)
        dataSet.valueTextSize = 16f

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(DefaultValueFormatter(1))

        pieChart.data = pieData
        pieChart.description.isEnabled = false

        pieChart.legend.textColor = ContextCompat.getColor(this, R.color.independent)

        pieChart.setDrawEntryLabels(false)
        pieChart.isDrawHoleEnabled = false

        pieChart.rotationAngle = 50f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.animate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_GET_TAG -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        when {
                            it.getBooleanExtra("NEW_TAG", false) -> {
                                product.tag = tagViewModel.getLastId()
                            }
                            else -> {
                                product.tag = it.getIntExtra("TAG_ID", -1)

                            }
                        }
                        productViewModel.update(product)
                        setProductInfo()
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    product.tag = null
                    productViewModel.update(product)
                    setProductInfo()
                }
            }

            REQUEST_CODE_GET_BARCODE -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        when {
                            it.getBooleanExtra("DELETE_BARCODE", false) -> {
                                product.barcode = null
                                productViewModel.update(product)
                                setProductInfo()

                            }
                            else -> {
                                val barcode = it.getStringExtra("BARCODE")
                                val productWithBarcode = productViewModel.getProductByBarcode(
                                    barcode!!
                                )

                                if (productWithBarcode != null) {
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.barcode_exists_error_message),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    product.barcode = barcode
                                    productViewModel.update(product)
                                    setProductInfo()

                                }
                            }
                        }
                    }
                }
            }

            REQUEST_CODE_PRODUCT_NAME -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        product.name = it.getStringExtra("PRODUCT_NAME").toString()
                        productViewModel.update(product)
                        setProductInfo()
                    }
                }
            }

            REQUEST_CODE_GET_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val imageUri = it.getStringExtra("IMAGE_URI").toString()
                        imageViewModel.insert(
                            Image(0, product.id, imageUri)
                        )
                    }
                }
            }

            REQUEST_CODE_GET_IMAGE_FROM_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val photoFile = File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}.jpg"
                        )
                        photoFile.createNewFile()
                        val out = FileOutputStream(photoFile)
                        out.write(
                            getBytes(this.contentResolver.openInputStream(it.data!!)!!)
                        )
                        out.close()

                        imageViewModel.insert(
                            Image(0, product.id, Uri.fromFile(photoFile).toString())
                        )
                    }
                }
            }
        }
    }

    private fun getBytes(inputStream: InputStream): ByteArray{
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        while(true){
            val len = inputStream.read(buffer)
            if(len != -1)
                byteBuffer.write(buffer, 0, len)
            else
                break
        }
        return byteBuffer.toByteArray()
    }

    override fun onImageLongClick(image: Image) {
        dialogImageManager(image)
    }

    companion object{
        private const val REQUEST_CODE_GET_TAG = 1
        private const val REQUEST_CODE_GET_BARCODE = 2
        private const val REQUEST_CODE_PRODUCT_NAME = 3
        private const val REQUEST_CODE_GET_IMAGE = 4
        private const val REQUEST_CODE_GET_IMAGE_FROM_GALLERY = 5
    }
}