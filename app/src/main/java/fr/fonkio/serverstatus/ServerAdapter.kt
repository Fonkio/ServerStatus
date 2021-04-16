package fr.fonkio.serverstatus

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jakarta.ws.rs.InternalServerErrorException
import java.lang.Exception
import java.lang.StringBuilder

class ServerAdapter(
    var servers: MutableList<ServerInformation>,
    val listener: OnClickListener,
    val context: Activity
) : RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {


    override fun getItemCount() = servers.size


    override fun onBindViewHolder(holder: ServerAdapter.ServerViewHolder, position: Int) {
        holder.server = servers[position]
        holder.textViewServerIp.text = holder.server.ip
        holder.textViewServerName.text = servers[position].name
        holder.textViewServerPort.text = servers[position].port
        val t = Thread()
        t.run {
            val mcApi = MinercraftAPI()
            var online = true
            try {
                holder.server = mcApi.getServerInformation(holder.server.ip, holder.server.port, holder.server.name, holder.server.id, holder.server.descDeployed)
                holder.textViewPlayerCount.text = "${holder.server.players.online}/${holder.server.players.max} joueurs"
                holder.progressBar.max = holder.server.players.max.toInt()
                holder.progressBar.setProgress(holder.server.players.online.toInt(), true)
                var sb = StringBuilder()
                for (extra:Extra in holder.server.description.extra) {
                    sb.append(extra.text)
                }
                println(sb.toString())
                holder.textViewMotd.text = sb.toString()
                holder.textViewVersion.text = holder.server.version.name
                var playerList = mutableListOf<Joueur>()
                for (sample:Sample in holder.server.players.sample) {
                    playerList.add(Joueur(sample.name))
                }
                holder.textViewPlayerList.text = sb.toString()
                holder.rvJoueurs.setHasFixedSize(true)
                val llm = LinearLayoutManager(context)
                llm.orientation = LinearLayoutManager.VERTICAL
                holder.rvJoueurs.layoutManager = llm
                holder.rvJoueurs.adapter = PlayerAdapter(playerList)
                holder.imageFavicon.setImageBitmap(holder.server.getImage())
            } catch (e: InternalServerErrorException) {
                online = false
                e.printStackTrace()
            }
            holder.imageOnOff.setImageDrawable(context.getDrawable(if (online) R.drawable.hightconnection else R.drawable.noconnection))
        }
        t.start()
        var visibility = View.GONE
        if (holder.server.descDeployed) {
            visibility = View.VISIBLE
        }

        holder.textViewPlayerList.visibility = visibility
        holder.textViewVersion.visibility = visibility
        holder.textViewMotd.visibility = visibility
        holder.buttonEdit.visibility = visibility
        holder.rvJoueurs.visibility = visibility
        holder.textViewEdit.visibility = visibility

    }

    override fun onBindViewHolder(holder: ServerAdapter.ServerViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads != null && payloads.isNotEmpty()) {
            var bundle = payloads[0] as Bundle
            var deploy: Boolean? = bundle.getBoolean("deploy")
            if(deploy is Boolean) {
                var visibility: Int
                if (deploy) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
                holder.textViewPlayerList.visibility = visibility
                holder.textViewVersion.visibility = visibility
                holder.textViewMotd.visibility = visibility
                holder.buttonEdit.visibility = visibility
                holder.rvJoueurs.visibility = visibility
                holder.textViewEdit.visibility = visibility
            }
        } else
            onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_server,
            parent,
            false
        )
        return ServerViewHolder(itemView)
    }

    inner class ServerViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener,
        View.OnLongClickListener {
        lateinit var server: ServerInformation
        var textViewServerName: TextView = view.findViewById(R.id.textViewServerName)
        var textViewPlayerCount: TextView = view.findViewById(R.id.textViewPlayerCount)
        var textViewMotd: TextView = view.findViewById(R.id.textViewMotd)
        var textViewVersion: TextView = view.findViewById(R.id.textViewVersion)
        var textViewPlayerList: TextView = view.findViewById(R.id.textViewPlayerList)
        var textViewServerIp: TextView = view.findViewById(R.id.textViewServerIp)
        var textViewServerPort: TextView = view.findViewById(R.id.textViewServerPort)
        var layout: CardView = view.findViewById(R.id.card)
        var imageOnOff: ImageView = view.findViewById(R.id.imageViewOnOff)
        var imageFavicon: ImageView = view.findViewById(R.id.imageFavicon)
        var buttonEdit: ImageView = view.findViewById(R.id.buttonEdit)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var rvJoueurs: RecyclerView = view.findViewById(R.id.rvJoueurs)
        var textViewEdit: TextView = view.findViewById(R.id.textViewEdit)
        init {
            layout.setOnClickListener(this)
            buttonEdit.setOnClickListener(this)
            layout.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v is CardView) {
                    listener.onClickLayout(position, this@ServerAdapter)
                } else {
                    listener.onClickEdit(position, this@ServerAdapter)
                }

            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLongClickLayout(position, this@ServerAdapter)
            }
            return true
        }

    }

    interface OnClickListener {
        fun onClickEdit(position: Int, serverAdapter: ServerAdapter)
        fun onClickLayout(position: Int, serverAdapter: ServerAdapter)
        fun onLongClickLayout(position: Int, serverAdapter: ServerAdapter)
    }



}