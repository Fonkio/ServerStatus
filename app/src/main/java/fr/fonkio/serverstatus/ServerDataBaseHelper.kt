package fr.gof.promesse.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import fr.fonkio.serverstatus.R

/**
 * Promise data base helper
 *
 * @constructor
 *
 * @param context
 */
class ServerDataBaseHelper(context: Context?) : SQLiteOpenHelper(context, R.string.app_name.toString(), null, R.integer.database_version) {

    val createServer = "CREATE TABLE Server(" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nom VARCHAR(50) NOT NULL," +
            " ip VARCHAR(50) NOT NULL," +
            " port VARCHAR(50) NOT NULL" +
            ");"

    val dropServer = "DROP TABLE IF EXISTS Server;"

    //Creation base de données
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createServer)
    }

    //Suppression des anciennes tables et création de nouvelles
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropServer)
        db?.execSQL(createServer)
    }

}