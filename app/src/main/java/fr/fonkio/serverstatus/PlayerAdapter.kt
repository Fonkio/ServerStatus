package fr.fonkio.serverstatus

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class PlayerAdapter(
    var joueurs: MutableList<Joueur>
) : RecyclerView.Adapter<PlayerAdapter.JoueurViewHolder>() {


    override fun getItemCount() = joueurs.size


    override fun onBindViewHolder(holder: PlayerAdapter.JoueurViewHolder, position: Int) {
        holder.player = joueurs[position]
        holder.textViewPseudo.text = holder.player.nom
        val t = Thread()
        t.run {
            holder.imageViewPlayerHead.setImageBitmap(holder.player.getBitmap())
            //holder.imageViewPlayerHead.setImageURI(Uri.parse(holder.player.head))
        }
        t.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoueurViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_player,
            parent,
            false
        )
        return JoueurViewHolder(itemView)
    }

    inner class JoueurViewHolder(view: View): RecyclerView.ViewHolder(view) {
        lateinit var player: Joueur
        var textViewPseudo: TextView = view.findViewById(R.id.textViewPseudo)
        var imageViewPlayerHead: ImageView = view.findViewById(R.id.imageViewPlayerHead)
    }
}
