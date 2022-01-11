package pl.mkotynia.musicmate.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid: String = "",
    val userName: String = "",
    val email: String = "",
    val status: String = "",
    val instrument: String = "",
    val level: String = "",
    val rehearsalSpace: Boolean = false,
    val lat: Double = 0.0,
    val lon: Double = 0.0): Parcelable