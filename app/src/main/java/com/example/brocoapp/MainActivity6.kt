package com.example.brocoapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMain6Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity6 : AppCompatActivity() {

    //Variable para Leer los componnentes del respectivo layout
    private lateinit var binding: ActivityMain6Binding

    //Esta variable permite conectar a la base de datos Firebase
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        //Conectar con los componentes del layout mencionado
        binding = ActivityMain6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ObtenerPuntos()
        binding.creditosbtn.setOnClickListener {

            //La funcion te lleva a la ventana de canjear creditos
            Creditos()
        }
        binding.descuentosbtn.setOnClickListener {

            //La funcion te lleva a la ventana de canjear descuentos
            Descuentos()
        }
        //Despues de dar Tap al boton Cancelar se ejecuta lo siguiente
        binding.btnRegresar.setOnClickListener {

            //Regresar a la ventana Home
            startActivity(Intent(this, MainActivity3::class.java));
        }
}
fun ObtenerPuntos(){

    //Obtener correo del usuario loggeado
    val user = FirebaseAuth.getInstance().currentUser
    //Conectar a base de datos, entidad "Brocopoints" donde se encuenta el correo
    db.collection("Brocopoints").document(user!!.email.toString())
        .get()
        .addOnSuccessListener { document ->
            if(document.exists()){
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                binding.brocotxt.text = document["Brocopoints"].toString()
            }
        }
}


    //Esta funcion permite direccionar a la ventana de canjede descuentos al dar Tap a una imagen
    fun Descuentos() {
        startActivity(Intent(this, MainActivityDescuentos::class.java));
    }
    //Esta funcion permite direccionar a la ventana de canje de creditos al dar Tap a una imagen
    fun Creditos() {
        startActivity(Intent(this, MainActivityCreditos::class.java));
    }
}