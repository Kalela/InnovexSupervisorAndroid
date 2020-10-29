package com.kalela.innovexsupervisor.util

import android.view.View
import android.widget.ProgressBar


/**
 * Toggle if page should show loading progress bar
 */
fun showLoading(show: Boolean, progressBar: ProgressBar) {
    progressBar.apply {
        visibility = if (show) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }
}