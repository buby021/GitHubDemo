package com.vidovicbranimir.githubdemo.enums

class versionType {

    enum class VersionType {
        FREE, PAID;

    }

    companion object {
        val type = VersionType.PAID
    }
}