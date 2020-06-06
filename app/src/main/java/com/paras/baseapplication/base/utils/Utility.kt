package com.paras.baseapplication.base.utils

import java.text.SimpleDateFormat

object Utility {

    fun getFormattedDate(dateString: String): String {
        if (dateString.isEmpty()) {
            return ""
        }
        val date = SimpleDateFormat(Constants.INPUT_DATE_FORMAT).parse(dateString)
        return date?.let { SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT).format(it) } ?: ""
    }

}