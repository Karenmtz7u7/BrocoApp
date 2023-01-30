package com.example.brocoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//ESTE MAIN SIRVE PARA CONFIGURAR EL RECYCLERVIEW DEL LAYOUT NUMERO 2

class MyAdapterHome(val context: Context, private val userList:ArrayList<Home>): RecyclerView.Adapter<MyAdapterHome.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterHome.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_rowhome,parent,false)
        return MyViewHolder(itemView)
    }

    //fUNCION QUE PERMITE LEER VARIABLES DECLARADAS EN EL MAIN USER
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user:Home =userList[position]
        holder.Nombre.text=user.Nombre
        holder.Calorias.text=user.Calorias
        holder.Sellos.text=user.Sellos
        holder.Brocopoints.text=user.Brocopoints
        Glide.with(context).load(user.Foto).into(holder.Foto)
    }

    //FUNCION QUE RETORNA LA VERIABLE CREADA EN LA FUNCION DE ARRIBA
    override fun getItemCount(): Int {
        return userList.size
    }

    //FUNCION QUE PERMITE ASIGNAR UN ELEMENTO DEL LAYOUT ITEM VIEW A LAS VARIABLES ANTERIORES
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Nombre: TextView = itemView.findViewById(R.id.txtnombre)
        val Calorias: TextView = itemView.findViewById(R.id.txtcalorias)
        val Sellos: TextView = itemView.findViewById(R.id.txtsellos)
        var Brocopoints: TextView = itemView.findViewById(R.id.txtbrocopoints)
        var Foto: ImageView = itemView.findViewById(R.id.iconImage)
    }
}