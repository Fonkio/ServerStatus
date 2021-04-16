package fr.fonkio.serverstatus

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import fr.gof.promesse.database.ServerDataBase


class MainActivity : AppCompatActivity(), ServerAdapter.OnClickListener {

    val database = ServerDataBase(this)
    lateinit var adapter: ServerAdapter
    lateinit var rvServer: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setSupportActionBar(findViewById(R.id.toolbar))
        adapter = ServerAdapter(database.getAllServer(), this, this)
        rvServer = findViewById(R.id.rvServer)
        rvServer.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvServer.layoutManager = llm
        rvServer.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            dialog()
        }
        refresh()
    }

    private fun dialog(server: ServerInformation? = null) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Ajouter un serveur")
        // Set up the input
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_addserver, null);
        builder.setView(customLayout)
        val ip: TextInputEditText = customLayout.findViewById(R.id.editTextIp)
        val nom: TextInputEditText = customLayout.findViewById(R.id.editTextNom)
        val port: TextInputEditText = customLayout.findViewById(R.id.editTextPort)
        if (server!=null) {
            ip.setText(server.ip)
            nom.setText(server.name)
            port.setText(server.port)
        }
        builder.setPositiveButton(if (server == null) "Ajouter" else "Modifier", DialogInterface.OnClickListener { dialog, which ->
            if (server == null) {
                val serverToAdd = ServerInformation()
                serverToAdd.name = nom.text.toString()
                serverToAdd.port = port.text.toString()
                serverToAdd.ip = ip.text.toString()
                database.addServer(serverToAdd)
                adapter.servers = database.getAllServer()
                adapter.notifyItemInserted(adapter.servers.size)
                adapter.notifyItemRangeChanged(0, adapter.servers.size)
            } else {
                server.ip = ip.text.toString()
                server.port = port.text.toString()
                server.name = nom.text.toString()

                database.updateServer(server)
                adapter.servers = database.getAllServer()
                adapter.notifyItemChanged(adapter.servers.indexOf(server))
            }
            refresh()
        })
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun refresh() {
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onRefresh(view: View) {
        refresh()
    }

    override fun onClickEdit(position: Int, serverAdapter: ServerAdapter) {
        dialog(adapter.servers[position])
    }

    override fun onClickLayout(position: Int, serverAdapter: ServerAdapter) {
        adapter.servers[position].descDeployed = !adapter.servers[position].descDeployed
        var bundle = Bundle()
        bundle.putBoolean("deploy", adapter.servers[position].descDeployed)
        adapter.notifyItemChanged(position, bundle)
    }

    override fun onLongClickLayout(position: Int, serverAdapter: ServerAdapter) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Supprimer le serveur ?")
        builder.setPositiveButton("Supprimer") { _, _ ->
            database.deleteServer(adapter.servers[position])
            adapter.servers.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()


    }

//    inner class refresh() : AsyncTask<Void, Void, String>() {
//        override fun doInBackground(vararg params: Void?): String? {
////            for (server in adapter.servers) {
////                try {
////                    var socket = Socket()
////                    socket.connect(InetSocketAddress(server.ip, 80), 1000);
////                    server.isOnline = true
////                } catch (e: Exception) {
////                    server.isOnline = false // Either timeout or unreachable or failed DNS lookup.
////                }
////            }
////            return "success"
//
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            // ...
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            adapter.notifyDataSetChanged()
//            // ...
//        }
//    }
}