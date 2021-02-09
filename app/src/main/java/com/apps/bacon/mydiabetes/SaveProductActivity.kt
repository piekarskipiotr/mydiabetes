package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.DialogAddImageBinding
import com.apps.bacon.mydiabetes.databinding.DialogDeleteTagBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.apps.bacon.mydiabetes.viewmodel.SaveProductViewModel
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
import kotlinx.android.synthetic.main.activity_save_product.*

private const val REQUEST_CODE_PRODUCT_NAME = 1
private const val REQUEST_CODE_GET_BARCODE = 3
private const val REQUEST_CODE_GET_IMAGE = 4

@AndroidEntryPoint
class SaveProductActivity : AppCompatActivity(), ImageAdapter.OnImageClickListener {
    private val saveProductViewModel: SaveProductViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private var icon: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_product)
        val bundle: Bundle = intent.extras!!
        val measureStatus = bundle.get("MEASURE") as Boolean
        var pieces: Int? = null
        var weight: Double? = null
        val valueStatus = bundle.get("VALUE") as Boolean
        var carbohydrates = 0.0
        var calories = 0.0
        var protein: Double? = null
        var fat: Double? = null
        var proteinFatExchangers = 0.0
        var carbohydrateExchangers = 0.0
        var selectedTagId: Int? = null
        val bottomSheetDialogCameraViewBinding = DialogAddImageBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        saveProductViewModel.getAllTags().observe(this, {
            addChips(this, it)

        })

        if(measureStatus){
            if(bundle.get("PIECES") == bundle.get("CORRECT_PIECES")){
                measureContainer.visibility = View.GONE
                line.visibility = View.GONE

            }else{
                measureSwitch.text = "${bundle.get("PIECES")} / ${bundle.get("CORRECT_PIECES")} "

            }

        }else{
            if(bundle.get("WEIGHT") == bundle.get("CORRECT_WEIGHT")){
                measureContainer.visibility = View.GONE
                line.visibility = View.GONE

            }else{
                measureSwitch.text = "${bundle.get("WEIGHT")} / ${bundle.get("CORRECT_WEIGHT")}"

            }
        }

        measureSwitch.setOnCheckedChangeListener { _, isChecked ->
            val textInfo: String
            if(isChecked){
                if(measureStatus){
                    pieces = bundle.get("CORRECT_PIECES") as Int
                    textInfo = "(dla $pieces sztuk)"
                    measureOfValues.text = textInfo
                    measureOfExchangers.text = textInfo
                }else{
                    weight = bundle.get("CORRECT_WEIGHT") as Double
                    textInfo = "(dla masy $weight g/ml)"
                    measureOfValues.text = textInfo
                    measureOfExchangers.text = textInfo
                }

                carbohydrates = bundle.get("CARBOHYDRATES_SECOND") as Double
                calories = bundle.get("CALORIES_SECOND") as Double
                protein = bundle.get("PROTEIN_SECOND") as Double?
                fat = bundle.get("FAT_SECOND") as Double?

                carbohydrateExchangers = bundle.get("CARBOHYDRATE_EXCHANGERS_SECOND") as Double
                proteinFatExchangers = bundle.get("PROTEIN_FAT_EXCHANGERS_SECOND") as Double

                setTextValues(
                    valueStatus,
                    carbohydrates,
                    calories,
                    protein,
                    fat,
                )
                pieChart(carbohydrateExchangers, proteinFatExchangers)

            }else{

                if(measureStatus){
                    pieces = bundle.get("PIECES") as Int
                    textInfo = "(dla $pieces sztuk)"
                    measureOfValues.text = textInfo
                    measureOfExchangers.text = textInfo
                }else{
                    weight = bundle.get("WEIGHT") as Double
                    textInfo = "(dla masy $weight g/ml)"
                    measureOfValues.text = textInfo
                    measureOfExchangers.text = textInfo
                }

                carbohydrates = bundle.get("CARBOHYDRATES") as Double
                calories = bundle.get("CALORIES") as Double
                protein = bundle.get("PROTEIN") as Double?
                fat = bundle.get("FAT") as Double?
                carbohydrateExchangers = bundle.get("CARBOHYDRATE_EXCHANGERS") as Double
                proteinFatExchangers = bundle.get("PROTEIN_FAT_EXCHANGERS") as Double

                setTextValues(
                    valueStatus,
                    carbohydrates,
                    calories,
                    protein,
                    fat,
                )
                pieChart(carbohydrateExchangers, proteinFatExchangers)

            }
        }.apply { measureSwitch.isChecked = true }

        backButton.setOnClickListener {
            onBackPressed()
        }

        productName.setOnClickListener {
            intent = Intent(this, ChangeProductNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PRODUCT_NAME)
        }

        scanBarcodeButton.setOnClickListener {
            intent = Intent(this, ScannerCameraActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        manualBarcode.setOnClickListener {
            intent = Intent(this, ProductBarcodeActivity::class.java)
            if(manualBarcode.text != null)
                intent.putExtra("BARCODE", false)

            startActivityForResult(intent, REQUEST_CODE_GET_BARCODE)
        }

        takePhotoButton.setOnClickListener {
            bottomSheetDialog.setContentView(bottomSheetDialogCameraViewBinding.root)
            bottomSheetDialog.show()

            bottomSheetDialogCameraViewBinding.cameraButton.setOnClickListener {
                intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_GET_IMAGE)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialogCameraViewBinding.galleryButton.setOnClickListener {
                //TODO: implementation getting image from gallery
                bottomSheetDialog.dismiss()
            }

        }

        tagChipContainer.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == 0){
                selectedTagId = null
                tagChipContainer.clearCheck()
                intent = Intent(this, AddTagActivity::class.java)
                startActivity(intent)

            }else{
                selectedTagId = checkedId
            }

        }

        saveButton.setOnClickListener {

            when {
                saveProductViewModel.checkForProductExist(productName.text.toString()) -> {
                    Toast.makeText(this, "Produkt o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show()
                }
                productName.text.isNullOrEmpty() -> {
                    productName.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))

                }
                else -> {
                    saveProductViewModel.insertProduct(
                        Product(
                            0,
                            productName.text.toString(),
                            weight,
                            pieces,
                            carbohydrates,
                            calories,
                            protein,
                            fat,
                            carbohydrateExchangers,
                            proteinFatExchangers,
                            selectedTagId,
                            manualBarcode.text.toString(),
                            false,
                            icon
                        )
                    )
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }

        }

    }

    private fun setTextValues(
        valueStatus: Boolean,
        carbohydratesValue: Double,
        caloriesValue: Double?,
        proteinValue: Double?,
        fatValue: Double?,
    ){
        carbohydrates.text = carbohydratesValue.toString().trimEnd()
        calories.text = caloriesValue.toString().trimEnd()
        if(valueStatus){
            proteinContainer.visibility = View.GONE
            fatContainer.visibility = View.GONE

        }else{
            protein.text = proteinValue.toString().trimEnd()
            fat.text = fatValue.toString().trimEnd()

        }
    }

    private fun addChips(context: Context, listOfTags: List<Tag>){
        tagChipContainer.removeAllViewsInLayout()
        tagChipContainer.addChip(context, "Dodaj tag", 0)
        for (i in listOfTags.indices){
            tagChipContainer.addChip(context, listOfTags[i].name, listOfTags[i].id)
        }

        for(i in 10 until tagChipContainer.childCount){
            tagChipContainer.getChildAt(i).setOnLongClickListener {
                dialogRemoveTag(it.id)
                true
            }
        }

    }

    private fun ChipGroup.addChip(context: Context, label: String, ID: Int){
        Chip(context).apply {
            id = ID
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }

    private fun dialogRemoveTag(id: Int){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteTagBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tagNameText.text = saveProductViewModel.getTagById(id).name

        dialogBinding.backButton.setOnClickListener {
                alertDialog.dismiss()
            }

        dialogBinding.deleteButton.setOnClickListener {
            saveProductViewModel.deleteTagById(id)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun pieChart(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val pieChart: PieChart = pieChart
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(carbohydrateExchangers.toFloat(), "W. węglowodanowe"))
        data.add(PieEntry(proteinFatExchangers.toFloat(), "W. białkowo-tłuszczowe"))

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

        pieChart.setDrawEntryLabels(false)
        pieChart.isDrawHoleEnabled = false

        pieChart.rotationAngle = 50f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.animate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_PRODUCT_NAME -> {
                if(resultCode == RESULT_OK){
                    data?.let {
                        productName.text = it.getStringExtra("PRODUCT_NAME").toString()
                    }
                }
            }

            REQUEST_CODE_GET_BARCODE -> {
                if(resultCode == RESULT_OK){
                    data?.let {
                        when{
                            it.getBooleanExtra("DELETE_BARCODE", false) -> {
                                manualBarcode.text = null
                            }else -> {
                                val barcode = it.getStringExtra("BARCODE")
                                val productWithBarcode = productViewModel.getProductByBarcode(barcode!!)

                                if(productWithBarcode != null){
                                    Toast.makeText(this, "Istnieje już produkt z takim kodem kreskowym!", Toast.LENGTH_LONG).show()
                                }else{
                                    manualBarcode.text = barcode
                                }
                            }
                        }
                    }
                }
            }

            REQUEST_CODE_GET_IMAGE -> {
                if(resultCode == RESULT_OK){
                    data?.let {
                        icon = it.getStringExtra("IMAGE_URI").toString()
                    }
                }

            }
        }
    }
}