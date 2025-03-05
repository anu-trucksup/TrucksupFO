package com.trucksup.field_officer.data.model

import android.os.Parcel
import android.os.Parcelable

class Media() : Parcelable {
    var mediaId: Int? = null
    var url: String? = null
    var greyUrl: String? = null
    var mediaType: String? = null

    constructor(parcel: Parcel) : this() {
        mediaId = parcel.readValue(Int::class.java.classLoader) as? Int
        url = parcel.readString()
        greyUrl = parcel.readString()
        mediaType = parcel.readString()
    }

    fun getImageUrl(): String? {
        if(url != null && url!!.isNotEmpty()) {
            return url ?: ""
        }
        return ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(mediaId)
        parcel.writeString(url)
        parcel.writeString(greyUrl)
        parcel.writeString(mediaType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}