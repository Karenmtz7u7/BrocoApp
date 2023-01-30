package com.example.brocoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//ESTE MAIN SIRVE PARA CONFIGURAR EL RECYCLERVIEW DEL LAYOUT Descuentos

class MyAdapterDescuentos(private val userList:ArrayList<Descuento>): RecyclerView.Adapter<MyAdapterDescuentos.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterDescuentos.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_rowdesc,parent,false)
        return MyViewHolder(itemView)
    }

    //fUNCION QUE PERMITE LEER VARIABLES DECLARADAS EN EL MAIN USER
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user:Descuento =userList[position]
        holder.Cupon.text=user.Cupon
        holder.Fecha.text=user.Fecha
    }

    //FUNCION QUE RETORNA LA VERIABLE CREADA EN LA FUNCION DE ARRIBA
    override fun getItemCount(): Int {
        return userList.size
    }

    //FUNCION QUE PERMITE ASIGNAR UN ELEMENTO DEL LAYOUT ITEM VIEW A LAS VARIABLES ANTERIORES
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Cupon: TextView = itemView.findViewById(R.id.txtCupon)
        var Fecha: TextView = itemView.findViewById(R.id.txtFecha)
    }
}