package com.apps.bacon.mydiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.apps.bacon.mydiabetes.databinding.ActivitySaveProductBinding
import kotlin.math.round

class SaveProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle = intent.extras!!
        val measureStatus = bundle.get("MEASURE") as Boolean
        val valueStatus = bundle.get("VALUE") as Boolean
        var carbohydrates: Double
        var calories: Double?
        var protein: Double?
        var fat: Double?
        var proteinFatExchangers: Double
        var carbohydrateExchangers: Double

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
                carbohydrates = bundle.get("CARBOHYDRATES_SECOND") as Double
                calories = bundle.get("CALORIES_SECOND") as Double?
                protein = bundle.get("PROTEIN_SECOND") as Double?
                fat = bundle.get("FAT_SECOND") as Double?

                carbohydrateExchangers = bundle.get("PROTEIN_FAT_EXCHANGERS_SECOND") as Double
                proteinFatExchangers = bundle.get("CARBOHYDRATE_EXCHANGERS_SECOND") as Double

                setTextValues(valueStatus, carbohydrates, calories, protein, fat, carbohydrateExchangers, proteinFatExchangers)
                setProgressBar(carbohydrateExchangers, proteinFatExchangers)

            }else{
                carbohydrates = bundle.get("CARBOHYDRATES") as Double
                calories = bundle.get("CALORIES") as Double?
                protein = bundle.get("PROTEIN") as Double?
                fat = bundle.get("FAT") as Double?
                carbohydrateExchangers = bundle.get("PROTEIN_FAT_EXCHANGERS") as Double
                proteinFatExchangers = bundle.get("CARBOHYDRATE_EXCHANGERS") as Double

                setTextValues(valueStatus, carbohydrates, calories, protein, fat, carbohydrateExchangers, proteinFatExchangers)
                setProgressBar(carbohydrateExchangers, proteinFatExchangers)

            }
        }.apply { binding.measureSwitch.isChecked = true }

    }

    private fun setProgressBar(carbohydrateExchangers: Double, proteinFatExchangers: Double){
        val sizeOfBar = carbohydrateExchangers + proteinFatExchangers
        binding.carbohydrateExchangersBar.progress = round((carbohydrateExchangers / sizeOfBar) * 100).toInt()
        binding.proteinFatExchangersBar.progress = round((proteinFatExchangers / sizeOfBar) * 100).toInt()

    }

    private fun setTextValues(valueStatus: Boolean, carbohydrates: Double, calories: Double?, protein: Double?, fat: Double?, carbohydrateExchangers: Double, proteinFatExchangers: Double){
        binding.carbohydrates.text = carbohydrates.toString().trimEnd()
        if(valueStatus){
            binding.calories.text = calories.toString().trimEnd()
            binding.protein.text = "-/-"
            binding.fat.text = "-/-"
        }else{
            binding.calories.text = "-/-"
            binding.protein.text = protein.toString().trimEnd()
            binding.fat.text = fat.toString().trimEnd()
        }

        binding.carbohydrateExchangers.text = carbohydrateExchangers.toString().trimEnd()
        binding.proteinFatExchangers.text = proteinFatExchangers.toString().trimEnd()
    }
}