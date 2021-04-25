package com.remlexworld.branchapp.util

import java.text.SimpleDateFormat

object AppUtils {

    fun formatDate(date : String) : String {

        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm")
        return formatter.format(parser.parse(date))

    }
}