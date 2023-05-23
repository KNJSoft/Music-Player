package com.knjsoft.musicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SonAdapter(context: Context, item: ArrayList<Son>) :ArrayAdapter<Son>(context,0,item) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView=LayoutInflater.from(context).inflate(R.layout.item_son,parent,false)
        val titre: TextView=itemView.findViewById(R.id.titre)
        val auteur: TextView=itemView.findViewById(R.id.auteur)
        val son=getItem(position)
        titre.text=son!!.titre
        auteur.text=son.auteur
        return itemView
    }

}
