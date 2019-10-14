package com.androidcourse.shoppinglistkotlin.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "whowon")
    var whowon: String,

    @ColumnInfo(name = "yourplay")
    var yourplay: String,

    @ColumnInfo(name = "computerplay")
    var computerplay: String,

    @ColumnInfo(name = "date")
    var date: String

) : Parcelable