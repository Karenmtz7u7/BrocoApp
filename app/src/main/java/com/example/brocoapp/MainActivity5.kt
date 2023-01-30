package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {

    //Variable para Leer los componnentes del respectivo layout
    private lateinit var binding: ActivityMain5Binding
    //Funcion principal del layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Conectar con los componentes del layout mencionado
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.creditosbtn.setOnClickListener {
            //Te lleva a la ventana para ver el historial de los creditos canjeados
            Creditos()
        }
        binding.descuentosbtn.setOnClickListener {
            //Te lleva a la ventana para ver el historial de los descuentos canjeados
            Descuentos()
        }

        //Despues de dar Tap al boton Cancelar se ejecuta lo siguiente
        binding.btnCancelar.setOnClickListener {
            //Regresar a la ventana Home
            startActivity(Intent(this, MainActivity3::class.java));
        }
    }

    //Esta funcion permite direccionar a la ventana de descuentos al dar Tap a una imagen
    fun Descuentos() {
        startActivity(Intent(this, MainActivityhistorialD::class.java));
    }
    
    //Esta funcion permite direccionar a la ventana de creditos al dar Tap a una imagen
    fun Creditos() {
        startActivity(Intent(this, MainActivityhistorialC::class.java));
    }
}
