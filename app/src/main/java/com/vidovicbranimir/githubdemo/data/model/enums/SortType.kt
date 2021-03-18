package com.vidovicbranimir.githubdemo.data.model.enums

enum class SortType(val type: String) {
    STAR("stars"), FORK("forks"), UPDATED("updated");

    override fun toString(): String {
        return type
    }
}