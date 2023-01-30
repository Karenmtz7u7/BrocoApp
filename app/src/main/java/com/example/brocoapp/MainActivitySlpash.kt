package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity

//ESTA PAGINA CREA EL SPLASH DE LA APP

class MainActivitySlpash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_slpash)


        //Ejecuta la funcion agregada al final
        startTimer()
    }

    //Funcion para el splash
    fun startTimer(){
        //Asignar tiempo de duracion del splash
        object: CountDownTimer(1000,1000){
            override fun onTick(millisUntilFinished: Long) {

            }
            //Ejecuta la pantalla de loggeo despues del tiempo asignado
            override fun onFinish() {
                val intent= Intent(applicationContext,MainActivity::class.java).apply{}
                startActivity(intent)
                finish()
            }

        }.start()
    }
}