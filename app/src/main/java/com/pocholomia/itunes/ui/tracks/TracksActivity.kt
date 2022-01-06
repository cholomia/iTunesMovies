package com.pocholomia.itunes.ui.tracks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pocholomia.itunes.databinding.ActivityTracksBinding
import com.pocholomia.itunes.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity for app
 */
@AndroidEntryPoint
class TracksActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityTracksBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}