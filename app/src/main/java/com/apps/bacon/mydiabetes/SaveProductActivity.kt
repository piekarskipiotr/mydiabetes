package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.apps.bacon.mydiabetes.data.entities.Product
import com.apps.bacon.mydiabetes.data.entities.Tag
import com.apps.bacon.mydiabetes.databinding.ActivitySaveProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogDeleteTagBinding
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveProductActivity : BaseActivity() {
    private val tagViewModel: TagViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var binding: ActivitySaveProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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

        tagViewModel.getAll().observe(this, {
            addChips(this, it)

        })

        if (measureStatus) {
            if (bundle.get("PIECES") == bundle.get("CORRECT_PIECES")) {
                binding.measureContainer.visibility = View.GONE
                binding.line.visibility = View.GONE

            } else {
                binding.measureSwitch.text =
                    "${bundle.get("PIECES")} / ${bundle.get("CORRECT_PIECES")} "

            }

        } else {
            if (bundle.get("WEIGHT") == bundle.get("CORRECT_WEIGHT")) {
                binding.measureContainer.visibility = View.GONE
                binding.line.visibility = View.GONE

            } else {
                binding.measureSwitch.text =
                    "${bundle.get("WEIGHT")} / ${bundle.get("CORRECT_WEIGHT")}"

            }
        }

        binding.measureSwitch.setOnCheckedChangeListener { _, isChecked ->
            val textInfo: String
            if (isChecked) {
                if (measureStatus) {
                    pieces = bundle.get("CORRECT_PIECES") as Int
                    textInfo =
                        "(${resources.getString(R.string.for_smth)} $pieces ${resources.getString(R.string.pieces_shortcut)})"
                    binding.measureOfValues.text = textInfo
                    binding.measureOfExchangers.text = textInfo
                } else {
                    weight = bundle.get("CORRECT_WEIGHT") as Double
                    textInfo = "(${resources.getString(R.string.for_smth)} $weight g/ml)"
                    binding.measureOfValues.text = textInfo
                    binding.measureOfExchangers.text = textInfo
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

            } else {

                if (measureStatus) {
                    pieces = bundle.get("PIECES") as Int
                    textInfo =
                        "(${resources.getString(R.string.for_smth)} $pieces ${resources.getString(R.string.pieces_shortcut)})"
                    binding.measureOfValues.text = textInfo
                    binding.measureOfExchangers.text = textInfo
                } else {
                    weight = bundle.get("WEIGHT") as Double
                    textInfo = "(${resources.getString(R.string.for_smth)} $weight g/ml)"
                    binding.measureOfValues.text = textInfo
                    binding.measureOfExchangers.text = textInfo
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
        }.apply { binding.measureSwitch.isChecked = true }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.productName.setOnClickListener {
            intent = Intent(this, ChangeProductNameActivity::class.java)
            val currentName = binding.productName.text.toString()
            if (currentName.isNotEmpty())
                intent.putExtra("CURRENT_NAME", currentName)
            getProductName.launch(intent)
        }

        binding.scanBarcodeButton.setOnClickListener {
            intent = Intent(this, ScannerCameraActivity::class.java)
            getBarcode.launch(intent)
        }

        binding.manualBarcode.setOnClickListener {
            intent = Intent(this, ProductBarcodeActivity::class.java)
            if (binding.manualBarcode.text != null)
                intent.putExtra("BARCODE", false)

            getBarcode.launch(intent)
        }

        binding.tagChipContainer.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == 0) {
                selectedTagId = null
                binding.tagChipContainer.clearCheck()
                intent = Intent(this, AddTagActivity::class.java)
                startActivity(intent)

            } else {
                selectedTagId = checkedId
            }

        }

        binding.saveButton.setOnClickListener {

            when {
                binding.productName.text.isNullOrEmpty() -> {
                    binding.productName.setHintTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.red,
                            null
                        )
                    )

                }
                else -> {
                    val barcode = if (binding.manualBarcode.text.isNullOrEmpty())
                        null
                    else
                        binding.manualBarcode.text.toString()

                    productViewModel.insert(
                        Product(
                            0,
                            binding.productName.text.toString(),
                            weight,
                            pieces,
                            carbohydrates,
                            calories,
                            protein,
                            fat,
                            carbohydrateExchangers,
                            proteinFatExchangers,
                            selectedTagId,
                            barcode,
                            false,
                            null,
                            true
                        )
                    )
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
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
    ) {
        binding.carbohydrates.text = carbohydratesValue.toString().trimEnd()
        binding.calories.text = caloriesValue.toString().trimEnd()
        if (valueStatus) {
            binding.proteinContainer.visibility = View.GONE
            binding.fatContainer.visibility = View.GONE

        } else {
            binding.protein.text = proteinValue.toString().trimEnd()
            binding.fat.text = fatValue.toString().trimEnd()

        }
    }

    private fun addChips(context: Context, listOfTags: List<Tag>) {
        binding.tagChipContainer.removeAllViewsInLayout()
        binding.tagChipContainer.addChip(context, resources.getString(R.string.add_tag), 0)
        for (i in listOfTags.indices) {
            binding.tagChipContainer.addChip(context, listOfTags[i].name, listOfTags[i].id)
        }

        for (i in 10 until binding.tagChipContainer.childCount) {
            binding.tagChipContainer.getChildAt(i).setOnLongClickListener {
                dialogRemoveTag(it.id)
                true
            }
        }

    }

    private fun ChipGroup.addChip(context: Context, label: String, ID: Int) {
        Chip(context, null, R.attr.CustomChip).apply {
            id = ID
            text = label
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }

    private fun dialogRemoveTag(id: Int) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteTagBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tagNameText.text = tagViewModel.getTagById(id).name

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            tagViewModel.deleteById(id)
            alertDialog.dismiss()
        }
        alertDialog.show()
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

    private val getProductName =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                activityResult.data?.let {
                    binding.productName.hint = null
                    binding.productName.text = it.getStringExtra("PRODUCT_NAME").toString()
                }
            }
        }

    private val getBarcode =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                activityResult.data?.let {
                    if (it.getBooleanExtra("DELETE_BARCODE", false))
                        binding.manualBarcode.text = null
                    else {
                        val barcode = it.getStringExtra("BARCODE")
                        binding.manualBarcode.text = barcode
                    }
                }
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}