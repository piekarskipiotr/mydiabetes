package com.apps.bacon.mydiabetes

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.apps.bacon.mydiabetes.data.entities.Tag
import com.apps.bacon.mydiabetes.databinding.ActivityAddTagBinding
import com.apps.bacon.mydiabetes.databinding.DialogDeleteTagBinding
import com.apps.bacon.mydiabetes.viewmodel.ProductViewModel
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTagActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTagBinding
    private val productViewModel: ProductViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTagBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.getBooleanExtra("TAG_SETTINGS", false))
            binding.headerText.text = resources.getString(R.string.tag_management)

        if (intent.getBooleanExtra("TAG_MANAGER", false)) {
            binding.existingTagsLayout.visibility = View.VISIBLE
            tagViewModel.getAll().observe(this, {
                addChips(this, it)
            })
        }

        val errorEmptyMessage = resources.getString(R.string.empty_field_message_error)
        val errorAlreadyExistsNameMessage =
            resources.getString(R.string.tag_name_exists_error_message)

        binding.addTagButton.setOnClickListener {
            when {
                binding.tagNameTextInput.text.isNullOrEmpty() -> binding.tagNameTextInputLayout.error =
                    errorEmptyMessage
                tagViewModel.checkForTagExist(
                    binding.tagNameTextInput.text.toString().trim()
                        .toLowerCase(Locale.ROOT)
                ) -> binding.tagNameTextInputLayout.error = errorAlreadyExistsNameMessage
                else -> {
                    binding.tagNameTextInputLayout.error = null
                    tagViewModel.insert(Tag(0, binding.tagNameTextInput.text.toString().trim()))
                    if (!intent.getBooleanExtra("TAG_SETTINGS", false)) {
                        intent.putExtra("NEW_TAG", true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        binding.tagNameTextInput.text = null
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addChips(context: Context, listOfTags: List<Tag>) {
        binding.tagChipContainer.removeAllViewsInLayout()
        for (i in listOfTags.indices) {
            binding.tagChipContainer.addChip(context, listOfTags[i].name, listOfTags[i].id)
            if (!intent.getBooleanExtra("TAG_SETTINGS", false)) {
                binding.tagChipContainer[i].setOnClickListener {
                    intent.putExtra("TAG_ID", listOfTags[i].id)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

        }
        for (i in 9 until binding.tagChipContainer.childCount) {
            binding.tagChipContainer.getChildAt(i).setOnLongClickListener {
                dialogRemoveTag(listOfTags[i])
                true
            }
        }
    }

    private fun ChipGroup.addChip(context: Context, label: String, ID: Int) {
        Chip(context, null, R.attr.CustomChip).apply {
            id = ID
            text = label
            isClickable = true
            if (!intent.getBooleanExtra("TAG_SETTINGS", false))
                isCheckable = true
            isCheckedIconVisible = false
            isFocusable = true
            addView(this)
        }
    }

    private fun dialogRemoveTag(tag: Tag) {
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
            removeTagFromProducts(tag.id)
            tagViewModel.delete(tag)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun removeTagFromProducts(tagId: Int) {
        productViewModel.getAllByTag(tagId).observe(this, {
            for (product in it) {
                product.tag = null
                productViewModel.update(product)
            }
        })
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
        this.finish()
    }
}