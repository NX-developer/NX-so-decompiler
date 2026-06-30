package com.kyhsgeekcode.disassembler.preference

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kyhsgeekcode.disassembler.disasmtheme.ColorHelper
import com.kyhsgeekcode.disassembler.ui.settings.SettingsScreen
import com.kyhsgeekcode.disassembler.ui.theme.NxTheme
import kotlinx.serialization.ExperimentalSerializationApi


class SettingsActivity : AppCompatActivity() {

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The Compose screen draws its own top bar.
        supportActionBar?.hide()
        // Make sure the disassembly color palettes are loaded before the picker shows.
        ColorHelper.populatePalettes(this)
        setContent {
            NxTheme {
                SettingsScreen(onBack = { finish() })
            }
        }
    }
}
