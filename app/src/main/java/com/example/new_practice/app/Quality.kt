package com.example.new_practice.app

import android.os.Parcel
import java.io.Serializable

class Quality : Serializable {
    var height: Int
    var width: Int
    var index: Int
    var isCurrent = 0

    constructor(inValue: Parcel) {
        height = inValue.readInt()
        width = inValue.readInt()
        index = inValue.readInt()
        isCurrent = inValue.readInt()
    }

    constructor(height: Int, width: Int, index: Int) {
        this.height = height
        this.width = width
        this.index = index
    }

//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(parcel: Parcel, i: Int) {
//        parcel.writeStringArray(
//            arrayOf(
//                index.toString() + "",
//                width.toString() + "",
//                height.toString() + ""
//            )
//        )
//    }

//    companion object CREATOR : Parcelable.Creator<Quality> {
//        override fun createFromParcel(parcel: Parcel): Quality {
//            return Quality(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Quality?> {
//            return arrayOfNulls(size)
//        }
//    }
}