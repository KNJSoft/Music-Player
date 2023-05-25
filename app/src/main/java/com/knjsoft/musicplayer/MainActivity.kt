package com.knjsoft.musicplayer

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    companion object{
        private const val request=99
    }
    private lateinit var listeview: ListView
    var listson= ArrayList<Son>()
    var sonAdapter: SonAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listeview=findViewById(R.id.listson)
        listson= ArrayList()
        sonAdapter=SonAdapter(this,listson)
        listeview.adapter=sonAdapter
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),request)
            return
        }else{
            getson()
        }
        listeview.onItemClickListener=AdapterView.OnItemClickListener{ adapterview,view,position,id ->
            Intent(this@MainActivity,Lecteur::class.java).also {
                val sons=listson[position]
                it.putExtra("son",sons)
                startActivity(it)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== request){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getson()
            }
        }
    }

    private fun getson() {
        val soncursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null)
        if (soncursor !=null && soncursor.moveToFirst()){
            val indextitre=soncursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val indexauteur=soncursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val indexdata=soncursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val indexmusic=soncursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)
            do {
                val titre=soncursor.getString(indextitre)
                var auteur=soncursor.getString(indexauteur)
                val path=soncursor.getString(indexdata)
                val music=soncursor.getInt(indexmusic)
                if(auteur=="<unknown>"){
                    auteur = "Inconnu"
                }
                if (music==1){
                    listson.add(Son(titre,auteur,path))
                }
            } while (soncursor.moveToNext())
            soncursor.close()
        }
        sonAdapter?.notifyDataSetChanged()
    }
}