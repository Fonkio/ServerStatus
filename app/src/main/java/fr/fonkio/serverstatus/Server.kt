package fr.fonkio.serverstatus

data class Server (
    var id: Long,
    var ip: String,
    var port: String,
    var nom: String,
    var isOnline: Boolean = false,
    var isDescDeployed: Boolean = false,
    var nbJoueurs: Int = 0,
    var playerList: MutableList<Joueur> = mutableListOf(),
        ) {

    init {

    }

    fun refresh() {

    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Server

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}