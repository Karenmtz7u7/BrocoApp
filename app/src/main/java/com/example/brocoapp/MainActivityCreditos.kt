package com.example.brocoapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMainCreditosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MainActivityCreditos : AppCompatActivity() {

    //Variable para conectar con firebase
    private val db = FirebaseFirestore.getInstance()

    //Variable para leer componentes del layout
    private lateinit var binding: ActivityMainCreditosBinding

    //Variable que obtiene el correo del usuario loggeado
    val user = FirebaseAuth.getInstance().currentUser

    //Variables para obtener y actualizar brocopoints del usuario
    var Acumulados=""
    var Gastados=""
    var Cupon=""

    //Funcion principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_creditos)

        //Obtener componentes del layout
        binding = ActivityMainCreditosBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Ejecutar la funcion y obtener puntos del usuario en pantalla
        ObtenerPuntos()


        //Tap en el boton Aceptar
        binding.btnAceptar.setOnClickListener {
            //Ejecutar funcion guardar
            GuardarDescuento()
            startActivity(Intent(this, MainActivity6::class.java));

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
                if(Acumulados.toInt()<100){
                    binding.rb1.isEnabled = false
                    binding.btnAceptar.isEnabled = false
                    Toast.makeText(this, "No cuentas con suficientes Brocopoints", Toast.LENGTH_SHORT).show()
                }

                if(Acumulados.toInt()<200){
                    binding.rb2.isEnabled = false
                }

                if(Acumulados.toInt()<300){
                    binding.rb3.isEnabled = false
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "No cuentas con Brocopoints", Toast.LENGTH_SHORT).show()
            }
    }



    //Funcion para guardar creditos en base de datos
    fun GuardarDescuento(){

        //Declarar una variable que indica que cupon fue seleccionado mediante IF y se determinan los puntos a restar
        //Los If anidados verifican que el canje sea posible de acuerdo a los puntos disponibles
        if (binding.rb1.isChecked)
        {
            if(Acumulados>= 100.toString()){
                Cupon="1 crédito"
                Gastados= 100.toString()
            }
        }

        if (binding.rb2.isChecked)
        {
            if(Acumulados>= 200.toString()){
                Cupon="2 créditos"
                Gastados= 200.toString()
            }
        }

        if (binding.rb3.isChecked)
        {
            if(Acumulados>= 300.toString()){
                Cupon="3 créditos"
                Gastados= 300.toString()
            }
        }

        //El siguiente codigo permite guardar los datos en la Entidad Descuentos dentro de Firebase
        val date = Date()
        val fechaC = SimpleDateFormat("d 'de' MMMM 'del' yyyy")
        val sFecha: String = fechaC.format(date)

        val dato = hashMapOf(
            "Cantidad" to Cupon,
            "Email" to user!!.email,
            "Fecha" to sFecha
        )
        db.collection("Credito").document()
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