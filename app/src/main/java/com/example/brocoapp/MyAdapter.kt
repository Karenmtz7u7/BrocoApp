package com.example.brocoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.brocoapp.MyAdapter.MyViewHolder
import java.util.*
import kotlin.collections.ArrayList

//ESTE MAIN SIRVE PARA CONFIGURAR EL RECYCLERVIEW DEL LAYOUT NUMERO 2

class MyAdapter(private val userList:ArrayList<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
        return MyViewHolder(itemView)
    }

    //fUNCION QUE PERMITE LEER VARIABLES DECLARADAS EN EL MAIN USER
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user:User =userList[position]
        holder.Nombre.text=user.Nombre
        holder.Calorias.text=user.Calorias
        holder.Sellos.text=user.Sellos
        holder.Brocopoints.text=user.Brocopoints
        holder.Fecha.text=user.Fecha
    }

    //FUNCION QUE RETORNA LA VERIABLE CREADA EN LA FUNCION DE ARRIBA
    override fun getItemCount(): Int {
        return userList.size
    }

    //FUNCION QUE PERMITE ASIGNAR UN ELEMENTO DEL LAYOUT ITEM VIEW A LAS VARIABLES ANTERIORES
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val Nombre: TextView= itemView.findViewById(R.id.txtnombre)
            val Calorias: TextView = itemView.findViewById(R.id.txtcalorias)
            val Sellos: TextView = itemView.findViewById(R.id.txtsellos)
            var Brocopoints: TextView = itemView.findViewById(R.id.txtbrocopoints)
            var Fecha: TextView = itemView.findViewById(R.id.txtfecha)
    }
}