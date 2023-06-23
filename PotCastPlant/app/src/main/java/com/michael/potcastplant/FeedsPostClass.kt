package com.michael.potcastplant

data class FeedsPostClass(
    var firstName: String?,
    var profileUrl: String,
    var imageUrl: String,
    var description: String,
    var timestamp: com.google.firebase.Timestamp
    )