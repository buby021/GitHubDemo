package com.vidovicbranimir.githubdemo.data.model.enums

enum class SortOrder(val direction: String) {
    ASCENDING("asc"), DESCENDING("desc");

    fun getOpposite(): SortOrder {
        if (this == ASCENDING) return DESCENDING
        else return ASCENDING
    }


}