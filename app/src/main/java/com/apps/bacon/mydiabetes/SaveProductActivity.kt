package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.iterator
import androidx.lifecycle.ViewModelProvider
import com.apps.bacon.mydiabetes.data.*
import com.apps.bacon.mydiabetes.databinding.ActivitySaveProductBinding
import com.apps.bacon.mydiabetes.databinding.DialogDeleteTagBinding
import com.apps.bacon.mydiabetes.viewmodel.SaveProductModelFactory
import com.apps.bacon.mydiabetes.viewmodel.SaveProductViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.math.round

private const val REQUEST_CODE_PRODUCT_NAME = 1
class SaveProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveProductBinding
    private lateinit var saveProductViewModel: SaveProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        val database = AppDatabase.getInstance(this)
        val repository = SaveProductRepository(database)
        val factory = SaveProductModelFactory(repository)
        saveProductViewModel = ViewModelProvider(this, factory).get(SaveProductViewModel::class.java)

        saveProductViewModel.getAllTags().observe(this, {
            addChips(this, it)

        })

        if(measureStatus){
            if(bundle.get("PIECES") == bundle.get("CORRECT_PIECES")){
                binding.measureContainer.visibility = View.GONE
                binding.line.visibility = View.GONE

            }else{
                binding.measureSwitch.text = "${bundle.get("PIECES")} / ${bundle.get("CORRECT_PIECES")} "

            }

        }else{
            if(bundle.get("WEIGHT") == bundle.get("CORRECT_WEIGHT")){
                binding.measureContainer.visibility = View.GONE
                binding.line.visibility = View.GONE

            }else{
                binding.measureSwitch.text = "${bundle.get("WEIGHT")} / ${bundle.get("CORRECT_WEIGHT")} "

            }
        }

        binding.measureSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                if(measureStatus){
                    pieces = bundle.get("CORRECT_PIECES") as Int
                }else{
                    weight = bundle.get("CORRECT_WEIGHT") as Double
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
                    carbohydrateExchangers,
                    proteinFatExchangers
                )
                setProgressBar(carbohydrateExchangers, proteinFatExchangers)

            }else{
                if(measureStatus){
                    pieces = bundle.get("PIECES") as Int
                }else{
                    weight = bundle.get("WEIGHT") as Double
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
                    carbohydrateExchangers,
                    proteinFatExchangers
                )
                setProgressBar(carbohydrateExchangers, proteinFatExchangers)

            }
        }.apply { binding.measureSwitch.isChecked = true }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.productName.setOnClickListener {
            intent = Intent(this, ChangeProductNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PRODUCT_NAME)
        }

        binding.tagChipContainer.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == 0){
                selectedTagId = null
                binding.tagChipContainer.clearCheck()
                intent = Intent(this, AddTagActivity::class.java)
                startActivity(intent)

            }else{
                selectedTagId = checkedId
            }

        }

        binding.saveButton.setOnClickListener {
            when {
                saveProductViewModel.getProduct(binding.productName.text.toString()).name == binding.productName.text.toString() -> {
                    Toast.makeText(this, "Produkt o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show()
                }
                binding.productName.text.isNullOrEmpty() -> {
                    binding.productName.setHintTextColor(ResourcesCompat.getColor(resources, R.color.red, null))

                }
                else -> {
                    saveProductViewModel.insertProduct(
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
                            selectedTagId
                        )
                    )
                    intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }

        }

    }

    private fun setProgressBar(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val sizeOfBar = carbohydrateExchangers + proteinFatExchangers
        binding.carbohydrateExchangersBar.progress = round((carbohydrateExchangers / sizeOfBar) * 100).toInt()
        binding.proteinFatExchangersBar.progress = round((proteinFatExchangers / sizeOfBar) * 100).toInt()

    }

    private fun setTextValues(
        valueStatus: Boolean,
        carbohydrates: Double,
        calories: Double?,
        protein: Double?,
        fat: Double?,
        carbohydrateExchangers: Double,
        proteinFatExchangers: Double
    ){
        binding.carbohydrates.text = carbohydrates.toString().trimEnd()
        binding.calories.text = calories.toString().trimEnd()
        if(valueStatus){
            binding.proteinContainer.visibility = View.GONE
            binding.fatContainer.visibility = View.GONE

        }else{
            binding.protein.text = protein.toString().trimEnd()
            binding.fat.text = fat.toString().trimEnd()

        }

        binding.carbohydrateExchangers.text = carbohydrateExchangers.toString().trimEnd()
        binding.proteinFatExchangers.text = proteinFatExchangers.toString().trimEnd()
    }

    private fun addChips(context: Context, listOfTags: List<Tag>){
        binding.tagChipContainer.removeAllViewsInLayout()
        binding.tagChipContainer.addChip(context, "Dodaj tag", 0)
        for (i in listOfTags.indices){
            binding.tagChipContainer.addChip(context, listOfTags[i].name, listOfTags[i].id)
        }

        for(i in 10 until binding.tagChipContainer.childCount){
            binding.tagChipContainer.getChildAt(i).setOnLongClickListener {
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

    private fun setProductName(name: String){
        binding.productName.text = name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_PRODUCT_NAME -> {
                if(resultCode == RESULT_OK){
                    setProductName(data!!.getStringExtra("PRODUCT_NAME").toString())

                }
            }

        }
    }
}