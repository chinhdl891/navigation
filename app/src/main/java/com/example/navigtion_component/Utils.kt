package com.example.navigtion_component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class Utils {
    companion object {
        fun enCode(bitmap: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        }

        fun decode(image: String?): Bitmap? {
            val bytes: ByteArray = Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}