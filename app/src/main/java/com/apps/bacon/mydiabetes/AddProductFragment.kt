package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.bacon.mydiabetes.databinding.DialogCalculatedExchangersBinding
import com.apps.bacon.mydiabetes.databinding.FragmentAddProductBinding
import com.apps.bacon.mydiabetes.utilities.Calculations
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.round

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private var measureStatus: Boolean = false
    private var valueStatus: Boolean = false
    private val errorMessage = "Pole nie może być puste!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialogViewBinding: DialogCalculatedExchangersBinding = DialogCalculatedExchangersBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        binding.measureSwitch.setOnCheckedChangeListener { _, isChecked ->
            measureStatus = isChecked
            if(isChecked){
                binding.weightContainer.visibility = View.GONE
                binding.pieceContainer.visibility = View.VISIBLE
            }else{
                binding.weightContainer.visibility = View.VISIBLE
                binding.pieceContainer.visibility = View.GONE
            }
        }
        binding.valuesSwitch.setOnCheckedChangeListener { _, isChecked ->
            valueStatus = isChecked
            if(isChecked){
                binding.proteinTextInputLayout.visibility = View.GONE
                binding.fatTextInputLayout.visibility = View.GONE
                binding.caloriesTextInputLayout.visibility = View.VISIBLE
            }else{
                binding.proteinTextInputLayout.visibility = View.VISIBLE
                binding.fatTextInputLayout.visibility = View.VISIBLE
                binding.caloriesTextInputLayout.visibility = View.GONE
            }
        }
        setOnChangeTextListeners()

        binding.calculateButton.setOnClickListener {
            if(!checkForEmptyFields()){
                bottomSheetDialog.setContentView(bottomSheetDialogViewBinding.root)
                bottomSheetDialog.show()
                val intent = Intent (requireActivity(), SaveProductActivity::class.java)
                val weight: Double
                val correctWeight: Double
                val pieces: Int
                val correctPieces: Int
                val calories: Double
                val protein: Double
                val fat: Double
                val proteinFatExchangers: Double
                val carbohydrate: Double = binding.carbohydrateTextInput.text.toString().toDouble()
                val carbohydrateExchangers: Double = Calculations().carbohydrateExchanges(carbohydrate)
                val carbohydrateExchangersFinal: Double
                val proteinFatExchangersFinal: Double
                val maxSizeOfProgress: Double

                intent.putExtra("CARBOHYDRATES", carbohydrate)
                if(measureStatus){
                    pieces = binding.pieceTextInput.text.toString().toInt()
                    correctPieces = binding.correctPieceTextInput.text.toString().toInt()
                    intent.putExtra("MEASURE", measureStatus)
                    intent.putExtra("PIECES", pieces)
                    intent.putExtra("CORRECT_PIECES", correctPieces)
                    intent.putExtra("CARBOHYDRATES_SECOND", Calculations().carbohydratesByPieces(carbohydrate, pieces, correctPieces))
                    bottomSheetDialogViewBinding.measureText.text = "dla $correctPieces szt. produktu"

                    if(valueStatus){
                        calories = binding.caloriesTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("CALORIES", calories)
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByPieces(calories, pieces, correctPieces))
                        proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                    }else{
                        protein = binding.proteinTextInput.text.toString().toDouble()
                        fat = binding.fatTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("PROTEIN", protein)
                        intent.putExtra("FAT", fat)
                        intent.putExtra("CALORIES", Calculations().caloriesByValues(carbohydrate, protein, fat))
                        intent.putExtra("PROTEIN_SECOND", Calculations().proteinByPieces(protein, pieces, correctPieces))
                        intent.putExtra("FAT_SECOND", Calculations().fatByPieces(fat, pieces, correctPieces))
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByPieces(Calculations().caloriesByValues(carbohydrate, protein, fat), pieces, correctPieces))
                        proteinFatExchangers = Calculations().proteinFatExchangers(protein, fat)

                    }
                    carbohydrateExchangersFinal = Calculations().carbohydrateExchangesByPieces(carbohydrateExchangers, pieces, correctPieces)
                    proteinFatExchangersFinal = Calculations().proteinFatExchangersByPieces(proteinFatExchangers, pieces, correctPieces)

                }else{
                    weight = binding.weightTextInput.text.toString().toDouble()
                    correctWeight = binding.correctWeightTextInput.text.toString().toDouble()
                    intent.putExtra("MEASURE", measureStatus)
                    intent.putExtra("WEIGHT", weight)
                    intent.putExtra("CORRECT_WEIGHT", correctWeight)
                    intent.putExtra("CARBOHYDRATES_SECOND", Calculations().carbohydratesByWeight(carbohydrate, weight, correctWeight))
                    bottomSheetDialogViewBinding.measureText.text = "dla produktu o masie: $correctWeight g/ml"

                    if(valueStatus){
                        calories = binding.caloriesTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("CALORIES", calories)
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByWeight(calories, weight, correctWeight))
                        proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                    }else{
                        protein = binding.proteinTextInput.text.toString().toDouble()
                        fat = binding.fatTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("PROTEIN", protein)
                        intent.putExtra("FAT", fat)
                        intent.putExtra("CALORIES", Calculations().caloriesByValues(carbohydrate, protein, fat))
                        intent.putExtra("PROTEIN_SECOND", Calculations().proteinByWeight(protein, weight, correctWeight))
                        intent.putExtra("FAT_SECOND", Calculations().fatByWeight(fat, weight, correctWeight))
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByWeight(Calculations().caloriesByValues(carbohydrate, protein, fat), weight, correctWeight))
                        proteinFatExchangers = Calculations().proteinFatExchangers(protein, fat)

                    }
                    carbohydrateExchangersFinal = Calculations().carbohydrateExchangesByWeight(carbohydrateExchangers, weight, correctWeight)
                    proteinFatExchangersFinal = Calculations().proteinFatExchangersByWeight(proteinFatExchangers, weight, correctWeight)
                }

                intent.putExtra("CARBOHYDRATE_EXCHANGERS", carbohydrateExchangers)
                intent.putExtra("PROTEIN_FAT_EXCHANGERS", proteinFatExchangers)
                intent.putExtra("CARBOHYDRATE_EXCHANGERS_SECOND", carbohydrateExchangersFinal)
                intent.putExtra("PROTEIN_FAT_EXCHANGERS_SECOND", proteinFatExchangersFinal)

                maxSizeOfProgress = carbohydrateExchangersFinal + proteinFatExchangersFinal
                bottomSheetDialogViewBinding.carbohydrateExchangersBar.progress = round((carbohydrateExchangersFinal / maxSizeOfProgress) * 100).toInt()
                bottomSheetDialogViewBinding.proteinFatExchangersBar.progress = round((proteinFatExchangersFinal / maxSizeOfProgress) * 100).toInt()

                bottomSheetDialogViewBinding.carbohydrateExchangers.text = carbohydrateExchangersFinal.toString()
                bottomSheetDialogViewBinding.proteinFatExchangers.text = proteinFatExchangersFinal.toString()

                bottomSheetDialogViewBinding.calculateButton.setOnClickListener {
                    bottomSheetDialog.dismiss()
                    startActivity(intent)
                }
            }else{
                setError()
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    private fun checkForEmptyFields(): Boolean{
        val checkValue: Boolean
        val checkCal: Boolean
        val checkCarbohydrates: Boolean = binding.carbohydrateTextInput.text.isNullOrEmpty()

        val checkMeasure: Boolean = if(measureStatus){
            listOfPieces().any { it.isNullOrEmpty() }
        }else{
            listOfWeight().any { it.isNullOrEmpty() }
        }

        return if(valueStatus){
            checkCal = binding.caloriesTextInput.text.isNullOrEmpty()
            (checkMeasure || checkCal || checkCarbohydrates)

        }else{
            checkValue = listOfProteinFat().any { it.isNullOrEmpty() }
            (checkMeasure || checkValue || checkCarbohydrates)

        }
    }

    private fun setError(){
        binding.pieceTextInput.text.isNullOrEmpty().apply {
            binding.pieceTextInputLayout.error = errorMessage
        }

        binding.correctPieceTextInput.text.isNullOrEmpty().apply {
            binding.correctPieceTextInputLayout.error = errorMessage
        }

        binding.weightTextInput.text.isNullOrEmpty().apply {
            binding.weightTextInputLayout.error = errorMessage
        }

        binding.correctWeightTextInput.text.isNullOrEmpty().apply {
            binding.correctWeightTextInputLayout.error = errorMessage
        }

        binding.carbohydrateTextInput.text.isNullOrEmpty().apply {
            binding.carbohydrateTextInputLayout.error = errorMessage
        }

        binding.caloriesTextInput.text.isNullOrEmpty().apply {
            binding.caloriesTextInputLayout.error = errorMessage
        }

        binding.proteinTextInput.text.isNullOrEmpty().apply {
            binding.proteinTextInputLayout.error = errorMessage
        }

        binding.fatTextInput.text.isNullOrEmpty().apply {
            binding.fatTextInputLayout.error = errorMessage
        }
    }

    private fun setOnChangeTextListeners(){
        if(measureStatus){
            binding.pieceTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.pieceTextInputLayout.error = errorMessage
                }else{
                    binding.pieceTextInputLayout.error = null
                }
            }

            binding.correctPieceTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.correctPieceTextInputLayout.error = errorMessage
                }else{
                    binding.correctPieceTextInputLayout.error = null
                }
            }
        }else{
            binding.weightTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.weightTextInputLayout.error = errorMessage
                }else{
                    binding.weightTextInputLayout.error = null
                }
            }
            binding.correctWeightTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.correctWeightTextInputLayout.error = errorMessage
                }else{
                    binding.correctWeightTextInputLayout.error = null
                }
            }
        }

        binding.carbohydrateTextInput.onTextChanged {
            if(it.isNullOrEmpty()){
                binding.carbohydrateTextInputLayout.error = errorMessage
            }else{
                binding.carbohydrateTextInputLayout.error = null
            }
        }

        if(valueStatus){
            binding.caloriesTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.caloriesTextInputLayout.error = errorMessage
                }else{
                    binding.caloriesTextInputLayout.error = null
                }
            }
        }else{
            binding.proteinTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.proteinTextInputLayout.error = errorMessage
                }else{
                    binding.proteinTextInputLayout.error = null
                }
            }
            binding.fatTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    binding.fatTextInputLayout.error = errorMessage
                }else{
                    binding.fatTextInputLayout.error = null

                }
            }
        }
    }

    private fun listOfPieces() = listOf(binding.pieceTextInput.text, binding.correctPieceTextInput.text)
    private fun listOfWeight() = listOf(binding.weightTextInput.text, binding.correctWeightTextInput.text)
    private fun listOfProteinFat() = listOf(binding.proteinTextInput.text, binding.fatTextInput.text)

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit){
        this.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}