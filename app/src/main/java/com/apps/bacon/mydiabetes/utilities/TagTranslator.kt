package com.apps.bacon.mydiabetes.utilities

import android.content.Context
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.viewmodel.TagViewModel

class TagTranslator {

    fun translate(tagViewModel: TagViewModel, context: Context) {
        val listOfTags = listOf(
            context.resources.getString(R.string.meat),
            context.resources.getString(R.string.fish),
            context.resources.getString(R.string.protein),
            context.resources.getString(R.string.bread),
            context.resources.getString(R.string.vegetables_and_fruits),
            context.resources.getString(R.string.sweets_and_snacks),
            context.resources.getString(R.string.drinks),
            context.resources.getString(R.string.nuts),
            context.resources.getString(R.string.others)
        )

        for (i in 1..9) {
            val tag = tagViewModel.getTagById(i)
            tag.name = listOfTags[i.dec()]
            tagViewModel.update(tag)
        }
    }
}