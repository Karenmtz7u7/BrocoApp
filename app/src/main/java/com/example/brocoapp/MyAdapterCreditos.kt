package com.example.brocoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//ESTE MAIN SIRVE PARA CONFIGURAR EL RECYCLERVIEW DEL LAYOUT Creditos

class MyAdapterCreditos(private val userList:ArrayList<Credito>): RecyclerView.Adapter<MyAdapterCreditos.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterCreditos.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_rowcred,parent,false)
        return MyViewHolder(itemView)
    }

    //fUNCION QUE PERMITE LEER VARIABLES DECLARADAS EN EL MAIN USER
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user:Credito =userList[position]
        holder.Cantidad.text=user.Cantidad
        holder.Fecha.text=user.Fecha
    }

    //FUNCION QUE RETORNA LA VERIABLE CREADA EN LA FUNCION DE ARRIBA
    override fun getItemCount(): Int {
        return userList.size
    }

    //FUNCION QUE PERMITE ASIGNAR UN ELEMENTO DEL LAYOUT ITEM VIEW A LAS VARIABLES ANTERIORES
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Cantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        var Fecha: TextView = itemView.findViewById(R.id.txtFecha)
    }
}