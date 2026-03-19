package com.xpeho.xpeapp.ui.components.ideaBox

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object IdeaDateFormatter {
    private val uiFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)

    fun format(date: Date?): String = date?.let { uiFormatter.format(it) } ?: "-"
}
