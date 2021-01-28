package com.apps.bacon.mydiabetes

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.apps.bacon.mydiabetes.data.Tag
import com.apps.bacon.mydiabetes.databinding.DialogDeleteTagBinding
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_tag.*

@AndroidEntryPoint
class AddTagActivity : AppCompatActivity() {
    private val tagViewModel: TagViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tag)
        val errorMessage = "Pole nie może być puste"

        if(intent.getBooleanExtra("TAG_MANAGER", false)){
            existingTagsLayout.visibility = View.VISIBLE
            tagViewModel.getAll().observe(this, {
                addChips(this, it)
            })
        }
        addTagButton.setOnClickListener {
            if(tagNameTextInput.text.isNullOrEmpty())
                tagNameTextInputLayout.error = errorMessage
            else{
                tagNameTextInputLayout.error = null
                tagViewModel.insert(Tag(0, tagNameTextInput.text.toString().trim()))
                finish()
            }
        }

        backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun addChips(context: Context, listOfTags: List<Tag>){
        tagChipContainer.removeAllViewsInLayout()
        for (i in listOfTags.indices){
            tagChipContainer.addChip(context, listOfTags[i].name, listOfTags[i].id)
            tagChipContainer[i].setOnClickListener {
                intent.putExtra("TAG_ID", listOfTags[i].id)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        for(i in 10 until tagChipContainer.childCount){
            tagChipContainer.getChildAt(i).setOnLongClickListener {
                dialogRemoveTag(listOfTags[i])
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

    private fun dialogRemoveTag(tag: Tag){
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        val dialogBinding = DialogDeleteTagBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)
        alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tagNameText.text = tag.name

        dialogBinding.backButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.deleteButton.setOnClickListener {
            tagViewModel.delete(tag)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}