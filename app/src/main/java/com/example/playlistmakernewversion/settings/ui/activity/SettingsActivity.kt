package com.example.playlistmakernewversion.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.databinding.ActivitySettingsBinding
import com.example.playlistmakernewversion.settings.ui.view_model.SettingViewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingViewModel.getViewModelFactory()
        )[SettingViewModel::class.java]

        binding.buttonArrowBack.setOnClickListener {
            finish()
        }

        binding.sharingTextView.setOnClickListener {
            val link = getString(R.string.sharingMessage)
            viewModel.shareApp(link)
        }

        binding.agreementTextView.setOnClickListener {
            viewModel.termOfUse()
        }

        binding.sendHelpTextView.setOnClickListener {
            viewModel.sentSupport()
        }

        binding.themeSwitch.isChecked = viewModel.default()
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
    }

}