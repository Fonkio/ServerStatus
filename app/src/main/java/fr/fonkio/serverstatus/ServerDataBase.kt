package fr.gof.promesse.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import fr.fonkio.serverstatus.ServerInformation

class ServerDataBase (context : Context){

    val database = ServerDataBaseHelper(context)

    fun addServer(server: ServerInformation) {
        val dbwritable: SQLiteDatabase = this.database.writableDatabase
        val values = ContentValues()
        values.put("ip", server.ip)
        values.put("port", server.port)
        values.put("nom", server.name)
        dbwritable.insert("Server", null, values)
        dbwritable.close()
    }

    fun updateServer(server : ServerInformation){
        val dbwritable: SQLiteDatabase = this.database.writableDatabase
        val values = ContentValues()
        values.put("ip", server.ip)
        values.put("port", server.port)
        values.put("nom", server.name)
        dbwritable.update("Server", values,"id = '${server.id}'", null)
        dbwritable.close()
    }

    fun deleteServer(server: ServerInformation) {
        //Ouverture
        val dbwritable: SQLiteDatabase = this.database.writableDatabase
        //Suppression du server
        dbwritable.delete("Server","id = ${server.id}", null)
        //Fermeture
        dbwritable.close()
    }

    fun getServer(curs: Cursor): MutableList<ServerInformation>{
        var serverList = mutableListOf<ServerInformation>()
        try {
            while (curs.moveToNext()) {
                val id = curs.getLong(curs.getColumnIndexOrThrow("id"))
                val nom = curs.getString(curs.getColumnIndexOrThrow("nom"))
                val ip = curs.getString(curs.getColumnIndexOrThrow("ip"))
                val port = curs.getString(curs.getColumnIndexOrThrow("port"))
                val servInfoToAdd = ServerInformation()
                servInfoToAdd.ip = ip
                servInfoToAdd.port = port
                servInfoToAdd.name = nom
                servInfoToAdd.id = id
                serverList.add(servInfoToAdd)
            }
        } finally {
            curs.close()
        }

        return serverList
    }

    fun getAllServer() : MutableList<ServerInformation> {
        val dbreadable : SQLiteDatabase = this.database.readableDatabase
        //Execution requête
        val col = arrayOf("id", "ip", "port", "nom")

        val curs: Cursor = dbreadable.query("Server", col, null, null, null, null, null)
        return getServer(curs)
    }
}