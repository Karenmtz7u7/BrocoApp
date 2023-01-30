@file:Suppress("DEPRECATION")

package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brocoapp.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class MainActivity2 : AppCompatActivity() {

    //Variable para leer componentes del layout mencionado
    private lateinit var binding: ActivityMain2Binding

    //Variables necesarias para trabajar con el RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var myAdapter: MyAdapter

    //Variable para conectar con Firebase
    private lateinit var db: FirebaseFirestore

    //Funcion principal del layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //Leer componentes del layout
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Titulo de la ventana
        title = "Historial de comidas registradas"

        //Enlazar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true);


        //Enlazar con el Main "MyAdapter"
        userArrayList = arrayListOf()
        myAdapter = MyAdapter(userArrayList)
        recyclerView.adapter = myAdapter

        //Ejecutar funcion
        EventChangeListener()

        //Si se da Tap en el boton "Regresar" se manda a la pantalla Home
        binding.btnRegresar.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java));
        }
    }

        //Funcion para conectar a Firebase y mostrar datos en el RecyclerView
        private fun EventChangeListener() {
            val user = FirebaseAuth.getInstance().currentUser
            db=FirebaseFirestore.getInstance()
            db.collection("Platillo").whereEqualTo("Email", user!!.email)
                .addSnapshotListener(object: EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error!=null){
                            Log.e("Firestore Error",error.message.toString())
                            return
                    }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type==DocumentChange.Type.ADDED){
                                userArrayList.add(dc.document.toObject(User::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                }
        })
    }
}