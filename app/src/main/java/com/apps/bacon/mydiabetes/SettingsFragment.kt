package com.apps.bacon.mydiabetes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.bacon.mydiabetes.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tagManagerSettings.setOnClickListener {
            val intent = Intent(requireActivity(), AddTagActivity::class.java)
            intent.putExtra("TAG_SETTINGS", true)
            intent.putExtra("TAG_MANAGER", true)
            startActivity(intent)

        }

        binding.themeChangerSettings.setOnClickListener {
            val intent = Intent(requireActivity(), ThemeSettings::class.java)
            startActivity(intent)

        }

        binding.languageSettings.setOnClickListener {
            val intent = Intent(requireActivity(), LanguageSettings::class.java)
            startActivity(intent)

        }

        binding.exportMany.setOnClickListener {
            val intent = Intent(requireActivity(), ExportActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}