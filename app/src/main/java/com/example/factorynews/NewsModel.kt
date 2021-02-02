package com.example.factorynews

import android.os.Parcel
import android.os.Parcelable


data class News (
    val articles: List<Article>
)

data class Article(
        val author: String?,
        val title: String?,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: Any? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}