package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brocoapp.databinding.ActivityMainActivityhistorialCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MainActivityhistorialC : AppCompatActivity() {

    //Variable para leer componentes del layout mencionado
    private lateinit var binding: ActivityMainActivityhistorialCBinding

    //Variables necesarias para trabajar con el RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Credito>
    private lateinit var myAdapter: MyAdapterCreditos

    //Variable para conectar con Firebase
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityhistorial_c)

        //Leer componentes del layout
        binding = ActivityMainActivityhistorialCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Titulo de la ventana
        title = "Historial de créditos"

        //Enlazar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true);


        //Enlazar con el Main "MyAdapter"
        userArrayList = arrayListOf()
        myAdapter = MyAdapterCreditos(userArrayList)
        recyclerView.adapter = myAdapter

        //Ejecutar funcion
        EventChangeListener()

        //Si se da Tap en el boton "Regresar" se manda a la pantalla Home
        binding.btnRegresar2.setOnClickListener {
            startActivity(Intent(this, MainActivity5::class.java));
        }
    }

    //Funcion para conectar a Firebase y mostrar datos en el RecyclerView
    private fun EventChangeListener() {
        val user = FirebaseAuth.getInstance().currentUser
        db=FirebaseFirestore.getInstance()
        db.collection("Credito").whereEqualTo("Email", user!!.email)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error!=null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type== DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(Credito::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}