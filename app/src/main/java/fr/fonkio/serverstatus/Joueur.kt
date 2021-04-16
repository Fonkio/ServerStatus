package fr.fonkio.serverstatus

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

class Joueur(
        val nom: String
){
        var head = "https://cravatar.eu/head/$nom/100.png"

        fun getBitmap() : Bitmap {
                val inputStream = URL(head).openStream()
                return BitmapFactory.decodeStream(inputStream)
        }
}
