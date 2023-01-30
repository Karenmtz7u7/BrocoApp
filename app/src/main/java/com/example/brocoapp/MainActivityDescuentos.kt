package com.example.brocoapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMainDescuentosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MainActivityDescuentos : AppCompatActivity() {
    //Variable para conectar con firebase
    private val db = FirebaseFirestore.getInstance()

    //Variable para leer componentes del layout
    private lateinit var binding: ActivityMainDescuentosBinding

    //Variable que obtiene el correo del usuario loggeado
    val user = FirebaseAuth.getInstance().currentUser

    //Variables para obtener y actualizar brocopoints del usuario
    var Acumulados=""
    var Gastados=""
    var Cupon=""

    //Funcion principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_descuentos)
        //Obtener componentes del layout
        binding = ActivityMainDescuentosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Ejecutar la funcion y obtener puntos del usuario en pantalla
        ObtenerPuntos()


        //Tap en el boton Aceptar
        binding.btnAceptar.setOnClickListener {
            //Ejecutar funcion guardar
            GuardarDescuento()
                    startActivity(Intent(this, MainActivity3::class.java));
        }
        //Tap en el boton Cancelar
        binding.btnCancelar.setOnClickListener {
            //Regresar a la ventana Home
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity6::class.java));

        }

    }
    //Esta funcion permite obtener los brocopoints del usuario
    fun ObtenerPuntos(){

        //Conectar a base de datos, entidad "Brocopoints" donde se encuenta el correo
        db.collection("Brocopoints").document(user!!.email.toString())
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    binding.txtBrocopoints.text = document["Brocopoints"].toString()
                    Acumulados=binding.txtBrocopoints.text.toString()
                }

                if(Acumulados.toInt()< 15){
                    binding.rb2.isEnabled = false
                    binding.btnAceptar.isEnabled = false
                    Toast.makeText(this, "No cuentas con suficientes Brocopoints", Toast.LENGTH_SHORT).show()
                }else{
                    binding.rb2.isEnabled = true
                }

                if(Acumulados.toInt()<25){
                    binding.rb5.isEnabled = false
                }else{
                    binding.rb5.isEnabled = true
                }


                if(Acumulados.toInt()<50){
                    binding.rb10.isEnabled = false
                }else{
                    binding.rb10.isEnabled = true
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "No cuentas con Brocopoints", Toast.LENGTH_SHORT).show()
            }
    }

    //Funcion para guardar descuentos en base de datos
    fun GuardarDescuento(){

        //El siguiente codigo hace una verificacion de los botones.
        if (binding.rb2.isChecked)
        {
            if(Acumulados>= 15.toString()){
                Cupon="%2 de descuento"
                Gastados= 15.toString()
            }
        }

        if (binding.rb5.isChecked)
        {
            if(Acumulados>= 25.toString()){
                Cupon="%5 de descuento"
                Gastados= 25.toString()
            }
        }

        if (binding.rb10.isChecked)
        {
            if(Acumulados>= 50.toString()){
                Cupon="%10 de descuento"
                Gastados= 50.toString()
            }
        }

        //El siguiente codigo permite guardar los datos en la Entidad Descuentos dentro de Firebase
            val date = Date()
            val fechaC = SimpleDateFormat("d 'de' MMMM 'del' yyyy")
            val sFecha: String = fechaC.format(date)

            val dato = hashMapOf(
                "Cupón" to Cupon,
                "Email" to user!!.email,
                "Fecha" to sFecha
            )
            db.collection("Descuento").document()
                .set(dato)
                .addOnSuccessListener {
                    Toast.makeText(this, "Cupón agregado correctamente", Toast.LENGTH_SHORT).show()
                    UpdateBrocopoints()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
    }

    //Funcion para actualizar puntos del usuario mediante las variables Acumulados y Nuevos
    fun UpdateBrocopoints(){
        val user = FirebaseAuth.getInstance().currentUser
        val datos = hashMapOf(
            "Brocopoints" to  Acumulados.toInt()-Gastados.toInt()
        )
        db.collection("Brocopoints").document(user!!.email.toString())
            .set(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "Brocopoints actualizados", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}