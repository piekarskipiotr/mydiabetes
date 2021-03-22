package com.apps.bacon.mydiabetes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.mydiabetes.adapters.StaticImageAdapter
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.databinding.ActivityStaticProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogReportBinding
import com.apps.bacon.mydiabetes.viewmodel.ImageViewModel
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StaticProductActivity : AppCompatActivity() {
    private lateinit var staticProduct: Product
    private val productViewModel: ProductViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()
    private lateinit var imagesAdapterStatic: StaticImageAdapter
    private lateinit var binding: ActivityStaticProductBinding

    @Inject
    lateinit var storageReference: StorageReference

    @Inject
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaticProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        staticProduct = productViewModel.getProduct(productId)

        initRecyclerView()
        setProductInfo()

        val imageViewModel: ImageViewModel by viewModels()
        imageViewModel.getURL(storageReference, "product", staticProduct.name)?.observe(this, {
            imagesAdapterStatic.updateData(it)
        })

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.reportButton.setOnClickListener {
            dialogReport()
        }

        if(staticProduct.inFoodPlate){
            binding.addButton.isClickable = false
            binding.addButton.alpha = 0.8f
        }else{
            binding.addButton.setOnClickListener {
                productViewModel.update(staticProduct.apply {
                    inFoodPlate = true
                })
                binding.addButton.isClickable = false
                binding.addButton.alpha = 0.8f
                Toast.makeText(this, resources.getString(R.string.product_added), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.photosRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            imagesAdapterStatic = StaticImageAdapter()
            adapter = imagesAdapterStatic
        }
    }

    private fun dialogReport() {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogReportBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.nameErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.valuesErrorCheckBox.isChecked = false
                dialogBinding.tagErrorCheckBox.isChecked = false
                dialogBinding.photosErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.valuesErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.nameErrorCheckBox.isChecked = false
                dialogBinding.tagErrorCheckBox.isChecked = false
                dialogBinding.photosErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.tagErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.nameErrorCheckBox.isChecked = false
                dialogBinding.valuesErrorCheckBox.isChecked = false
                dialogBinding.photosErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.photosErrorCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dialogBinding.nameErrorCheckBox.isChecked = false
                dialogBinding.valuesErrorCheckBox.isChecked = false
                dialogBinding.tagErrorCheckBox.isChecked = false

            }
        }

        dialogBinding.reportButton.setOnClickListener {
            val errorMassage = when {
                dialogBinding.nameErrorCheckBox.isChecked -> dialogBinding.nameErrorCheckBox.text.toString()
                dialogBinding.valuesErrorCheckBox.isChecked -> dialogBinding.valuesErrorCheckBox.text.toString()
                dialogBinding.tagErrorCheckBox.isChecked -> dialogBinding.tagErrorCheckBox.text.toString()
                dialogBinding.photosErrorCheckBox.isChecked -> dialogBinding.photosErrorCheckBox.text.toString()
                else -> null
            }

            if (errorMassage != null) {
                val productReference = database.child("Product Errors")
                productReference.child(staticProduct.name).setValue(errorMassage)

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.report_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setProductInfo() {
        binding.productName.text = staticProduct.name
        val measureText: String = if (staticProduct.weight != null)
            "(${resources.getString(R.string.for_smth)} ${staticProduct.weight} g/ml)"
        else
            "(${resources.getString(R.string.for_smth)} ${staticProduct.pieces} ${
                resources.getString(
                    R.string.pieces_shortcut
                )
            })"

        binding.measureOfProductValues.text = measureText
        binding.measureOfProductExchangers.text = measureText

        binding.carbohydrates.text = staticProduct.carbohydrates.toString()
        binding.calories.text = staticProduct.calories.toString()
        if (staticProduct.protein != null) {
            binding.protein.text = staticProduct.protein.toString()
            binding.fat.text = staticProduct.fat.toString()
        } else {
            binding.proteinContainer.visibility = View.GONE
            binding.fatContainer.visibility = View.GONE
        }

        pieChart(staticProduct.carbohydrateExchangers, staticProduct.proteinFatExchangers)

        if (staticProduct.tag == null)
            addChip(resources.getString(R.string.set_the_tag), 0)
        else {
            val tag = tagViewModel.getTagById(staticProduct.tag!!)
            addChip(tag.name, tag.id)
        }

        if (staticProduct.barcode == null) {
            binding.barcodeCard.visibility = View.GONE
        } else {
            binding.barcode.text = staticProduct.barcode
        }
    }

    private fun addChip(label: String, ID: Int) {
        binding.tagChipContainer.removeAllViewsInLayout()
        binding.tagChipContainer.addChip(label, ID)
    }

    private fun ChipGroup.addChip(label: String, ID: Int) {
        Chip(this@StaticProductActivity, null, R.attr.CustomChip).apply {
            id = ID
            text = label
            isClickable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }

    private fun pieChart(carbohydrateExchangers: Double, proteinFatExchangers: Double) {
        val pieChart: PieChart = binding.pieChart
        val data = ArrayList<PieEntry>()
        if (carbohydrateExchangers != 0.0)
            data.add(
                PieEntry(
                    carbohydrateExchangers.toFloat(),
                    resources.getString(R.string.pie_label_carbohydrate)
                )
            )
        if (proteinFatExchangers != 0.0)
            data.add(
                PieEntry(
                    proteinFatExchangers.toFloat(),
                    resources.getString(R.string.pie_label_protein_fat)
                )
            )

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

}