package com.vidovicbranimir.githubdemo.data


import java.text.*
import java.util.*

object Format {
    private var DATETIME: SimpleDateFormat? = null
    private var DATE: SimpleDateFormat? = null
    private var sdf: SimpleDateFormat? = null


    fun aslDateTimeString(date: Date?): String {
        return DATETIME!!.format(date)
    }

    fun asDateString(dateString: String?): String {
        val date = sdf!!.parse(dateString)
        return DATE!!.format(date)
    }


    fun isEmpty(text: String?): Boolean {
        return text == null || text.trim { it <= ' ' }.isEmpty()
    }

    init {
        DATETIME =
            SimpleDateFormat("dd.MM.yyyy'T'HH:mm:ss", Locale.US)
        DATE =
            SimpleDateFormat("dd.MM.yyyy", Locale.US)
        sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}
