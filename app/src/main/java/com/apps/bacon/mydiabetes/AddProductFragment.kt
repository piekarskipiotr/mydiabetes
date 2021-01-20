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
import com.apps.bacon.mydiabetes.utilities.Calculations
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlin.math.round

class AddProductFragment : Fragment() {
    private var measureStatus: Boolean = false
    private var valueStatus: Boolean = false
    private val errorMessage = "Pole nie może być puste!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialogViewBinding: DialogCalculatedExchangersBinding = DialogCalculatedExchangersBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        measureSwitch.setOnCheckedChangeListener { _, isChecked ->
            measureStatus = isChecked
            if(isChecked){
                weightContainer.visibility = View.GONE
                pieceContainer.visibility = View.VISIBLE
            }else{
                weightContainer.visibility = View.VISIBLE
                pieceContainer.visibility = View.GONE
            }
        }
        valuesSwitch.setOnCheckedChangeListener { _, isChecked ->
            valueStatus = isChecked
            if(isChecked){
                proteinTextInputLayout.visibility = View.GONE
                fatTextInputLayout.visibility = View.GONE
                caloriesTextInputLayout.visibility = View.VISIBLE
            }else{
                proteinTextInputLayout.visibility = View.VISIBLE
                fatTextInputLayout.visibility = View.VISIBLE
                caloriesTextInputLayout.visibility = View.GONE
            }
        }
        setOnChangeTextListeners()

        calculateButton.setOnClickListener {
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
                val carbohydrate: Double = carbohydrateTextInput.text.toString().toDouble()
                val carbohydrateExchangers: Double = Calculations().carbohydrateExchanges(carbohydrate)
                val carbohydrateExchangersFinal: Double
                val proteinFatExchangersFinal: Double
                val maxSizeOfProgress: Double

                intent.putExtra("CARBOHYDRATES", carbohydrate)
                if(measureStatus){
                    pieces = pieceTextInput.text.toString().toInt()
                    correctPieces = correctPieceTextInput.text.toString().toInt()
                    intent.putExtra("MEASURE", measureStatus)
                    intent.putExtra("PIECES", pieces)
                    intent.putExtra("CORRECT_PIECES", correctPieces)
                    intent.putExtra("CARBOHYDRATES_SECOND", Calculations().carbohydratesByPieces(carbohydrate, pieces, correctPieces))
                    bottomSheetDialogViewBinding.measureText.text = "dla $correctPieces szt. produktu"

                    if(valueStatus){
                        calories = caloriesTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("CALORIES", calories)
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByPieces(calories, pieces, correctPieces))
                        proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                    }else{
                        protein = proteinTextInput.text.toString().toDouble()
                        fat = fatTextInput.text.toString().toDouble()
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
                    weight = weightTextInput.text.toString().toDouble()
                    correctWeight = correctWeightTextInput.text.toString().toDouble()
                    intent.putExtra("MEASURE", measureStatus)
                    intent.putExtra("WEIGHT", weight)
                    intent.putExtra("CORRECT_WEIGHT", correctWeight)
                    intent.putExtra("CARBOHYDRATES_SECOND", Calculations().carbohydratesByWeight(carbohydrate, weight, correctWeight))
                    bottomSheetDialogViewBinding.measureText.text = "dla produktu o masie: $correctWeight g/ml"

                    if(valueStatus){
                        calories = caloriesTextInput.text.toString().toDouble()
                        intent.putExtra("VALUE", valueStatus)
                        intent.putExtra("CALORIES", calories)
                        intent.putExtra("CALORIES_SECOND", Calculations().caloriesByWeight(calories, weight, correctWeight))
                        proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                    }else{
                        protein = proteinTextInput.text.toString().toDouble()
                        fat = fatTextInput.text.toString().toDouble()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    private fun checkForEmptyFields(): Boolean{
        val checkValue: Boolean
        val checkCal: Boolean
        val checkCarbohydrates: Boolean = carbohydrateTextInput.text.isNullOrEmpty()

        val checkMeasure: Boolean = if(measureStatus){
            listOfPieces().any { it.isNullOrEmpty() }
        }else{
            listOfWeight().any { it.isNullOrEmpty() }
        }

        return if(valueStatus){
            checkCal = caloriesTextInput.text.isNullOrEmpty()
            (checkMeasure || checkCal || checkCarbohydrates)

        }else{
            checkValue = listOfProteinFat().any { it.isNullOrEmpty() }
            (checkMeasure || checkValue || checkCarbohydrates)

        }
    }

    private fun setError(){
        pieceTextInput.text.isNullOrEmpty().apply {
            pieceTextInputLayout.error = errorMessage
        }

        correctPieceTextInput.text.isNullOrEmpty().apply {
            correctPieceTextInputLayout.error = errorMessage
        }

        weightTextInput.text.isNullOrEmpty().apply {
            weightTextInputLayout.error = errorMessage
        }

        correctWeightTextInput.text.isNullOrEmpty().apply {
            correctWeightTextInputLayout.error = errorMessage
        }

        carbohydrateTextInput.text.isNullOrEmpty().apply {
            carbohydrateTextInputLayout.error = errorMessage
        }

        caloriesTextInput.text.isNullOrEmpty().apply {
            caloriesTextInputLayout.error = errorMessage
        }

        proteinTextInput.text.isNullOrEmpty().apply {
            proteinTextInputLayout.error = errorMessage
        }

        fatTextInput.text.isNullOrEmpty().apply {
            fatTextInputLayout.error = errorMessage
        }
    }

    private fun setOnChangeTextListeners(){
        if(measureStatus){
            pieceTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    pieceTextInputLayout.error = errorMessage
                }else{
                    pieceTextInputLayout.error = null
                }
            }

            correctPieceTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    correctPieceTextInputLayout.error = errorMessage
                }else{
                    correctPieceTextInputLayout.error = null
                }
            }
        }else{
            weightTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    weightTextInputLayout.error = errorMessage
                }else{
                    weightTextInputLayout.error = null
                }
            }
            correctWeightTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    correctWeightTextInputLayout.error = errorMessage
                }else{
                    correctWeightTextInputLayout.error = null
                }
            }
        }

        carbohydrateTextInput.onTextChanged {
            if(it.isNullOrEmpty()){
                carbohydrateTextInputLayout.error = errorMessage
            }else{
                carbohydrateTextInputLayout.error = null
            }
        }

        if(valueStatus){
            caloriesTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    caloriesTextInputLayout.error = errorMessage
                }else{
                    caloriesTextInputLayout.error = null
                }
            }
        }else{
            proteinTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    proteinTextInputLayout.error = errorMessage
                }else{
                    proteinTextInputLayout.error = null
                }
            }
            fatTextInput.onTextChanged {
                if(it.isNullOrEmpty()){
                    fatTextInputLayout.error = errorMessage
                }else{
                    fatTextInputLayout.error = null

                }
            }
        }
    }

    private fun listOfPieces() = listOf(pieceTextInput.text, correctPieceTextInput.text)
    private fun listOfWeight() = listOf(weightTextInput.text, correctWeightTextInput.text)
    private fun listOfProteinFat() = listOf(proteinTextInput.text, fatTextInput.text)

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