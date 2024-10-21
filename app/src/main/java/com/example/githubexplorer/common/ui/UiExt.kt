package com.example.githubexplorer.common.ui

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Context.launchChromeTab(url: String) {
    CustomTabsIntent.Builder().build()
        .launchUrl(this, Uri.parse(url))
}