package com.example.brocoapp


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMain7Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.*



@Suppress("DEPRECATION")
class MainActivity7 : AppCompatActivity() {
    //Esta variable permite conectar a la base de datos Firebase
    private val db = FirebaseFirestore.getInstance()
    //Esta variable permite leer los elementos del layout
    private lateinit var binding: ActivityMain7Binding
    //Variables para Actualizar Brocopoints del usuario
    var Acumulados=""
    var Nuevos=""

    //Funcion principal del layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        //Titulo de la ventana
        title="Registrar alimento"

        //Leer los elementos del layout
        binding = ActivityMain7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Ejecutar la funcion el escaner al abrir este layout (La que está al final)
        initScanner();

        //Esto se ejecuta al dar tap en el boton "Cancelar"
        binding.btnCancelar.setOnClickListener {
            //Mensaje en pantalla
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            //Regresar al layout Home
            startActivity(Intent(this, MainActivity3::class.java));

        }
        //Esto se ejecuta al dar tap en el boton "Aceptar"
        binding.btnRegistrar.setOnClickListener {

            //Ejecutar funcion GuardarDatos
            guardarDatos(db)
            //Regresar al layout Home
            startActivity(Intent(this, MainActivity3::class.java));

        }

        //El resto del codigo en la funcion permite obtener Brocopoints del usuario en base de datos Firebase

        //Obtener correo del usuario loggeado
        val user = FirebaseAuth.getInstance().currentUser
        //Conectar a base de datos, entidad "Brocopoints" donde se encuenta el correo
        db.collection("Brocopoints").document(user!!.email.toString())
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    Log.d(TAG, "${document.id} => ${document.data}")
                    binding.txtPuntos.text = document["Brocopoints"].toString()
                    Acumulados=binding.txtPuntos.text.toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "No cuentas con puntos", Toast.LENGTH_SHORT).show()
            }

    }


    //Funcion GuardarDatos para meter el platillo en Firebase
    fun guardarDatos(db: FirebaseFirestore) {

        if (binding.txtNombre.text.isNotBlank() &&
            binding.txtCalorias.text.isNotBlank() &&
            binding.txtSellos.text.isNotBlank() &&
            binding.txtBrocopoints.text.isNotBlank()
        ) {
            val user = FirebaseAuth.getInstance().currentUser
            val date = Date()
            val fechaC = SimpleDateFormat("d 'de' MMMM 'del' yyyy")
            val sFecha: String = fechaC.format(date)

            val dato = hashMapOf(
                "Nombre" to binding.txtNombre.text,
                "Calorías" to binding.txtCalorias.text,
                "Sellos" to binding.txtSellos.text,
                "Brocopoints" to binding.txtBrocopoints.text,
                "Email" to user!!.email,
                "Fecha" to sFecha
            )
            db.collection("Platillo")
                .document()
                .set(dato)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

        }
        //Ejecutar funcion de actualizar Puntos "UpdateBrocopoints"
        UpdateBrocopoints()

        }

    //Funcion para actualizar puntos del usuario mediante las variables Acumulados y Nuevos
    fun UpdateBrocopoints(){
        val user = FirebaseAuth.getInstance().currentUser
        val datos = hashMapOf(
            "Brocopoints" to Acumulados.toInt()+Nuevos.toInt()
        )
        db.collection("Brocopoints").document(user!!.email.toString())
            .set(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "Puntos guardados correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }

    //Funcion para hacer funcionar el Scaner de QR
    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Enfoca un código QR")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }


    //Esta funcion permite obtener la informacion del QR escaneado y mostrar dicha info en los Textbox del layout
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity3::class.java));
            } else {
                var datos = ""
                db.collection("QR").document(result.contents)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val Nombre = document["Nombre"].toString()
                            val Calorias = document["Calorías"].toString()
                            val Sellos = document["Sellos"].toString()
                            val Brocopoints = document["Brocopoints"].toString()
                            datos += "${document.id}: $Nombre, $Calorias, $Sellos, $Brocopoints\n"
                            binding.txtNombre.text =document["Nombre"].toString()
                            binding.txtCalorias.text =document["Calorías"].toString()
                            binding.txtSellos.text = document["Sellos"].toString()
                            binding.txtBrocopoints.text =document["Brocopoints"].toString()
                            //Guardar los nuevos puntos en una variable
                            Nuevos=binding.txtBrocopoints.text.toString()
                        }

                    }.addOnFailureListener {
                            Toast.makeText(this, "QR inválido", Toast.LENGTH_SHORT).show()
                        }



                        }


            }else {
            Toast.makeText(this, "QR inválido", Toast.LENGTH_SHORT).show()
        }

            super.onActivityResult(requestCode, resultCode, data)


        }
    }


