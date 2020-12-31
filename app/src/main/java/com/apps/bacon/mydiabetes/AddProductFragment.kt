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

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calculateButton.setOnClickListener {
            val bottomSheetDialogViewBinding: DialogCalculatedExchangersBinding = DialogCalculatedExchangersBinding.inflate(layoutInflater)

            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            bottomSheetDialog.setContentView(bottomSheetDialogViewBinding.root)
            bottomSheetDialog.show()
            bottomSheetDialogViewBinding.calculateButton.setOnClickListener {
                bottomSheetDialog.dismiss()
                val intent = Intent (activity, SaveProductActivity::class.java)
                activity?.startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }
}