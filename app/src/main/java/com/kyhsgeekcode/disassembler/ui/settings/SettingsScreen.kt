package com.kyhsgeekcode.disassembler.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kyhsgeekcode.disassembler.MainActivity
import com.kyhsgeekcode.disassembler.R
import com.kyhsgeekcode.disassembler.disasmtheme.ColorHelper
import com.kyhsgeekcode.disassembler.preference.PowerUserModeSettings
import com.mikepenz.aboutlibraries.LibsBuilder

private const val ISSUES_URL = "https://github.com/NX-developer/NX-so-decompiler/issues/new"
private const val README_URL = "https://github.com/NX-developer/NX-so-decompiler#readme"
private const val REPO_URL = "https://github.com/NX-developer/NX-so-decompiler"
private const val UPSTREAM_URL = "https://github.com/KYHSGeekCode/Android-Disassembler"
private const val BUGREPORTER_URL = "https://github.com/PramUkesh"

private fun Context.settingsPrefs() =
    getSharedPreferences(MainActivity.SETTINGKEY, Context.MODE_PRIVATE)

private fun Context.openUrl(url: String) {
    runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var showDeveloperInfo by remember { mutableStateOf(false) }

    val title = if (showDeveloperInfo) {
        stringResource(R.string.dev_info_detail)
    } else {
        stringResource(R.string.title_activity_settings)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showDeveloperInfo) showDeveloperInfo = false else onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            if (showDeveloperInfo) {
                DeveloperInfoContent(context)
            } else {
                MainSettingsContent(context) { showDeveloperInfo = true }
            }
        }
    }
}

@Composable
private fun MainSettingsContent(context: Context, onOpenDeveloperInfo: () -> Unit) {
    val prefs = remember { context.settingsPrefs() }

    // --- Disassembly theme ---
    SectionHeader(stringResource(R.string.disasmTheme))
    val paletteNames = remember { ColorHelper.palettes.keys.sorted() }
    var currentPalette by remember {
        mutableStateOf(prefs.getString("PaletteName", "Default") ?: "Default")
    }
    var showPaletteDialog by remember { mutableStateOf(false) }
    SettingRow(
        iconRes = R.drawable.ic_pref_palette,
        title = stringResource(R.string.pref_theme_choose),
        subtitle = currentPalette,
        onClick = { showPaletteDialog = true },
    )
    if (showPaletteDialog) {
        SingleChoiceDialog(
            title = stringResource(R.string.pref_theme_choose),
            options = paletteNames,
            selected = currentPalette,
            onDismiss = { showPaletteDialog = false },
            onSelect = { name ->
                currentPalette = name
                prefs.edit().putString("PaletteName", name).apply()
                ColorHelper.setPalette(name)
                showPaletteDialog = false
            },
        )
    }

    Divider()

    // --- Power-user features (stored in SETTINGKEY so they actually take effect) ---
    SectionHeader(stringResource(R.string.power_user_features))
    var powerUser by remember {
        mutableStateOf(prefs.getBoolean(PowerUserModeSettings.POWER_USER_IMPORT_MODE_KEY, false))
    }
    SwitchRow(
        title = stringResource(R.string.power_user_import_title),
        subtitle = stringResource(R.string.power_user_import_summary),
        checked = powerUser,
        onCheckedChange = {
            powerUser = it
            prefs.edit().putBoolean(PowerUserModeSettings.POWER_USER_IMPORT_MODE_KEY, it).apply()
        },
    )
    if (powerUser) {
        BooleanPref(
            prefs,
            PowerUserModeSettings.POWER_USER_FILESYSTEM_IMPORT_KEY,
            default = true,
            title = stringResource(R.string.power_user_filesystem_import_title),
            subtitle = stringResource(R.string.power_user_filesystem_import_summary),
        )
        BooleanPref(
            prefs,
            PowerUserModeSettings.POWER_USER_APPS_IMPORT_KEY,
            default = true,
            title = stringResource(R.string.power_user_apps_import_title),
            subtitle = stringResource(R.string.power_user_apps_import_summary),
        )
        BooleanPref(
            prefs,
            PowerUserModeSettings.POWER_USER_RESEARCH_TOOLS_IMPORT_KEY,
            default = false,
            title = stringResource(R.string.power_user_research_tools_import_title),
            subtitle = stringResource(R.string.power_user_research_tools_import_summary),
        )
    }

    Divider()

    // --- App info ---
    SectionHeader(stringResource(R.string.app_info))
    SettingRow(
        iconRes = R.drawable.ic_pref_person,
        title = stringResource(R.string.dev_info_detail),
        subtitle = stringResource(R.string.dev_info),
        onClick = onOpenDeveloperInfo,
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_bug,
        title = stringResource(R.string.send_mail),
        subtitle = stringResource(R.string.send_mail_detail),
        onClick = { context.openUrl(ISSUES_URL) },
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_lightbulb,
        title = stringResource(R.string.send_feature_request),
        subtitle = stringResource(R.string.send_feature_request_detail),
        onClick = { context.openUrl(ISSUES_URL) },
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_book,
        title = stringResource(R.string.pref_manual_title),
        subtitle = stringResource(R.string.pref_manual_summary),
        onClick = { context.openUrl(README_URL) },
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_license,
        title = stringResource(R.string.pref_license_title),
        subtitle = stringResource(R.string.pref_license_summary),
        onClick = {
            (context as? Activity)?.let {
                LibsBuilder().withFields(android.R.string::class.java.fields).start(it)
            }
        },
    )
}

@Composable
private fun DeveloperInfoContent(context: Context) {
    SectionHeader(stringResource(R.string.pref_devinfo_project))
    SettingRow(
        iconRes = R.drawable.ic_pref_code,
        title = stringResource(R.string.pref_github_title),
        subtitle = stringResource(R.string.pref_github_summary),
        onClick = { context.openUrl(REPO_URL) },
    )
    Divider()
    SectionHeader(stringResource(R.string.pref_devinfo_people))
    SettingRow(
        iconRes = R.drawable.ic_pref_person,
        title = stringResource(R.string.pref_maintainer_title),
        subtitle = stringResource(R.string.pref_maintainer_summary),
        onClick = { context.openUrl(REPO_URL) },
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_person,
        title = stringResource(R.string.pref_original_author_title),
        subtitle = stringResource(R.string.pref_original_author_summary),
        onClick = { context.openUrl(UPSTREAM_URL) },
    )
    SettingRow(
        iconRes = R.drawable.ic_pref_person,
        title = stringResource(R.string.pref_bugreporter_title),
        subtitle = stringResource(R.string.pref_bugreporter_summary),
        onClick = { context.openUrl(BUGREPORTER_URL) },
    )
}

@Composable
private fun BooleanPref(
    prefs: android.content.SharedPreferences,
    key: String,
    default: Boolean,
    title: String,
    subtitle: String,
) {
    var checked by remember { mutableStateOf(prefs.getBoolean(key, default)) }
    SwitchRow(
        title = title,
        subtitle = subtitle,
        checked = checked,
        onCheckedChange = {
            checked = it
            prefs.edit().putBoolean(key, it).apply()
        },
    )
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun SettingRow(
    @DrawableRes iconRes: Int,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Column(Modifier.padding(start = 16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SingleChoiceDialog(
    title: String,
    options: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                options.forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == selected,
                                onClick = { onSelect(option) },
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = option == selected,
                            onClick = { onSelect(option) },
                        )
                        Text(option, Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.ok)) }
        },
    )
}
