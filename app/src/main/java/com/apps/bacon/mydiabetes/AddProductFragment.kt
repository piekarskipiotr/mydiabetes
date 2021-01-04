package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.bacon.mydiabetes.databinding.DialogCalculatedExchangersBinding
import com.apps.bacon.mydiabetes.databinding.FragmentAddProductBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.round

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private var measureStatus: Boolean = false
    private var valueStatus: Boolean = false

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

        binding.calculateButton.setOnClickListener {
            bottomSheetDialog.setContentView(bottomSheetDialogViewBinding.root)
            bottomSheetDialog.show()
            val weight: Double
            val correctWeight: Double
            val pieces: Int
            val correctPieces: Int
            val calories: Double
            val protein: Double
            val fat: Double
            val proteinFatExchangers: Double
            val carbohydrate: Double = binding.carbohydrateTextInput.text.toString().toDouble()
            val carbohydrateExchangers: Double= Calculations().carbohydrateExchanges(carbohydrate)
            val carbohydrateExchangersFinal: Double
            val proteinFatExchangersFinal: Double
            val maxSizeOfProgress: Double

            if(measureStatus){
                pieces = binding.pieceTextInput.text.toString().toInt()
                correctPieces = binding.correctPieceTextInput.text.toString().toInt()
                bottomSheetDialogViewBinding.measureText.text = "dla $correctPieces szt. produktu"
                if(valueStatus){
                    calories = binding.caloriesTextInput.text.toString().toDouble()
                    proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                }else{
                    protein = binding.proteinTextInput.text.toString().toDouble()
                    fat = binding.fatTextInput.text.toString().toDouble()
                    proteinFatExchangers = Calculations().proteinFatExchangers(protein, fat)

                }
                carbohydrateExchangersFinal = Calculations().carbohydrateExchangesByPieces(carbohydrateExchangers, pieces, correctPieces)
                proteinFatExchangersFinal = Calculations().proteinFatExchangersByPieces(proteinFatExchangers, pieces, correctPieces)
            }else{
                weight = binding.weightTextInput.text.toString().toDouble()
                correctWeight = binding.correctWeightTextInput.text.toString().toDouble()
                bottomSheetDialogViewBinding.measureText.text = "dla produktu o masie: $correctWeight g/ml"
                if(valueStatus){
                    calories = binding.caloriesTextInput.text.toString().toDouble()
                    proteinFatExchangers = Calculations().proteinFatExchangersByCal(calories, carbohydrate)

                }else{
                    protein = binding.proteinTextInput.text.toString().toDouble()
                    fat = binding.fatTextInput.text.toString().toDouble()
                    proteinFatExchangers = Calculations().proteinFatExchangers(protein, fat)

                }
                carbohydrateExchangersFinal = Calculations().carbohydrateExchangesByWeight(carbohydrateExchangers, weight, correctWeight)
                proteinFatExchangersFinal = Calculations().proteinFatExchangersByWeight(proteinFatExchangers, weight, correctWeight)
            }

            maxSizeOfProgress = carbohydrateExchangersFinal + proteinFatExchangersFinal
            bottomSheetDialogViewBinding.carbohydrateExchangersBar.progress = round((carbohydrateExchangersFinal / maxSizeOfProgress) * 100).toInt()
            bottomSheetDialogViewBinding.proteinFatExchangersBar.progress = round((proteinFatExchangersFinal / maxSizeOfProgress) * 100).toInt()

            bottomSheetDialogViewBinding.carbohydrateExchangers.text = carbohydrateExchangersFinal.toString()
            bottomSheetDialogViewBinding.proteinFatExchangers.text = proteinFatExchangersFinal.toString()

            bottomSheetDialogViewBinding.calculateButton.setOnClickListener {
                bottomSheetDialog.dismiss()
                val intent = Intent (activity, SaveProductActivity::class.java)
                intent.putExtra("CARBOHYDRATE_EXCHANGERS", carbohydrateExchangersFinal)
                intent.putExtra("PROTEIN_FAT_EXCHANGERS", proteinFatExchangersFinal)
                activity?.startActivity(intent)
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

}