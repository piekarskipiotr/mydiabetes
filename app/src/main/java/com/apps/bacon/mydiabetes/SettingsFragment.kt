package com.apps.bacon.mydiabetes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreference = requireActivity().getSharedPreferences(
            "PREFERENCE_NAME",
            Context.MODE_PRIVATE
        )

        tagManagerSettings.setOnClickListener {
            val intent = Intent(requireActivity(), AddTagActivity::class.java)
            intent.putExtra("TAG_SETTINGS", true)
            intent.putExtra("TAG_MANAGER", true)
            startActivity(intent)

        }

        themeChangerSettings.setOnClickListener {
            if(getDefaultNightMode() == MODE_NIGHT_NO){
                with(sharedPreference.edit()) {
                    putInt("THEME", MODE_NIGHT_YES)
                    apply()
                }
            }else{
                with(sharedPreference.edit()) {
                    putInt("THEME", MODE_NIGHT_NO)
                    apply()
                }
            }
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finishAffinity()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}