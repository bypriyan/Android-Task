package com.socialseller.dummyapplication.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val profileImageUrl: String = ""
) {
    constructor() : this("", "", "", "")
}
