package com.plcoding.doodlekong.ui.drawing

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.plcoding.doodlekong.databinding.ActivityDrawingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToUiStateUpdates()

        binding.colorGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.checkRadioButton(checkedId)
        }
    }

    private fun subscribeToUiStateUpdates() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedColorButtonId.collect { id ->
                binding.colorGroup.check(id)
            }
        }
    }
}