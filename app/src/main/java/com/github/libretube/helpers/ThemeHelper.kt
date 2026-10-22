package com.github.libretube.helpers

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import com.github.libretube.R
import com.github.libretube.constants.PreferenceKeys
import com.github.libretube.ui.adapters.IconsSheetAdapter
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors

object ThemeHelper {
    /**
     * Set the theme, including accent color and night mode
     */
    fun updateTheme(activity: AppCompatActivity) {
        // AyuTube uses one fixed theme (black, off-white, logo red); accent prefs are ignored
        activity.setTheme(R.style.Theme_Ayu)
    }

    /**
     * Update the accent color of the app and apply dynamic colors if needed
     */
    private fun getTheme(accentColor: String): Int {
        return when (accentColor) {
            // set the accent color, use the pure black/white theme if enabled
            "my" -> R.style.BaseTheme
            "red" -> R.style.Theme_Red
            "blue" -> R.style.Theme_Blue
            "yellow" -> R.style.Theme_Yellow
            "green" -> R.style.Theme_Green
            "purple" -> R.style.Theme_Purple
            "monochrome" -> R.style.Theme_Monochrome
            "violet" -> R.style.Theme_Violet
            else -> throw IllegalArgumentException()
        }
    }

    fun applyDialogActivityTheme(activity: Activity) {
        activity.theme.applyStyle(R.style.DialogActivity, true)
    }

    /**
     * set the theme mode (light, dark, auto)
     */
    @Suppress("UNUSED_PARAMETER")
    fun getThemeMode(themeMode: String): Int {
        // AyuTube is dark-only
        return AppCompatDelegate.MODE_NIGHT_YES
    }

    /**
     * change the app icon
     */
    fun changeIcon(context: Context, newLogoActivityAlias: String) {
        // Disable Old Icon(s)
        for (appIcon in IconsSheetAdapter.availableIcons) {
            val activityClass = context.packageName.removeSuffix(".debug") + "." + appIcon.activityAlias

            // remove old icons
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context.packageName, activityClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        // set the class name for the activity alias
        val newLogoActivityClass = context.packageName.removeSuffix(".debug") + "." + newLogoActivityAlias
        // Enable New Icon
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context.packageName, newLogoActivityClass),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    /**
     * Get a color by a color resource attr
     */
    fun getThemeColor(context: Context, colorCode: Int) =
        MaterialColors.getColor(context, colorCode, Color.TRANSPARENT)

    /**
     * Get the styled app name: "ayu" in the text color, "tube" in the logo red, both in the
     * wordmark font (res/font/wordmark.ttf)
     */
    fun getStyledAppName(context: Context): Spanned {
        val colorPrimary = getThemeColor(context, androidx.appcompat.R.attr.colorPrimaryDark)
        val name = SpannableStringBuilder("ayutube")
        ResourcesCompat.getFont(context, R.font.wordmark)?.let {
            name.setSpan(FontSpan(it), 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        name.setSpan(ForegroundColorSpan(colorPrimary), 3, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return name
    }

    fun isDarkMode(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}

/**
 * Applies a custom typeface to a span of text (TypefaceSpan(Typeface) needs API 28)
 */
private class FontSpan(private val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}
