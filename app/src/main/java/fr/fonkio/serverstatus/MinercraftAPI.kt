package fr.fonkio.serverstatus

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import jakarta.json.JsonObject
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.WebTarget
import jakarta.ws.rs.core.MediaType
import org.glassfish.hk2.utilities.ServiceLocatorUtilities.bind


class MinercraftAPI {
    private val MINECRAFT_API = "https://minecraft-api.com/api/"
    private val INFORMATION_PATH = "ping/"
    private val client: Client = ClientBuilder.newClient()
    private val target: WebTarget = client.target(MINECRAFT_API)

    fun getServerInformation(ip: String, port: String, name: String, id: Long, descDeployed: Boolean): ServerInformation {
        val serverInfo = target.path(INFORMATION_PATH)
                .path("$ip/")
                .path("$port/")
                .path("json")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(ServerInformation::class.java)
        serverInfo.port = port
        serverInfo.ip = ip
        serverInfo.name = name
        serverInfo.id = id
        serverInfo.descDeployed = descDeployed
        return serverInfo
    }

    fun getServerStatus(ip: String, port: String): Boolean {
        return target.path(INFORMATION_PATH)
                .path("status/")
                .path("$ip/")
                .path("$port/")
                .path("json")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(JsonObject::class.java).getString("status") == "En ligne"
    }
}

class ServerStatus {
    lateinit var status: String
}

class ServerInformation {

    //
    //var bmp: Bitmap =
    lateinit var description: Description
    lateinit var players: Players
    lateinit var version: Version
    var favicon: String? = null
    var ip = ""
    var port = ""
    var name = ""
    var id = -1L
    var descDeployed = false

    fun getImage(): Bitmap? {
        var base64Image: String? = favicon?.split(",")?.get(1) ?: null
        var imageBytes: ByteArray? = if (base64Image != null) javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image) else null
        return if (imageBytes != null) {
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes?.size)
        } else {
            null
        }
    }
}

class Players {
    lateinit var max: Integer
    lateinit var online: Integer
    var sample: MutableList<Sample> = mutableListOf()
}

class Version {
    lateinit var name: String
    lateinit var protocol: Integer
}

class Sample {
    lateinit var id: String
    lateinit var name: String
}

class Description {
    lateinit var extra: MutableList<Extra>
    lateinit var text: String
}

class Extra {
    lateinit var color: String
    lateinit var text: String
}
