package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityEliminarcuentaBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class eliminarcuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEliminarcuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEliminarcuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.deletebtn.setOnClickListener {
            val password = binding.edpass.text.toString()
            deleteuser (password)
            binding.edpass.setText("")
        }
        binding.cancelbtn.setOnClickListener {
            binding.edpass.setText("")
            startActivity(Intent(this, recomendaciones::class.java))
        }
    }

//Funcion para eliminar cuenta de usuario
    private  fun deleteuser(password : String) {
        val user = auth.currentUser
        if (user != null){
            val email = user.email
            val credential = EmailAuthProvider
                .getCredential(email!!, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        user.delete()
                            .addOnCompleteListener { taskDeleteAcount ->
                                if (taskDeleteAcount.isSuccessful) {
                                    Toast.makeText(this, "Se eliminó tu cuenta.",
                                        Toast.LENGTH_SHORT).show()
                                    signOut()
                                }
                            }
                    } else {
                        Toast.makeText(this, "La contraseña ingresada es incorrecta.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

//funcion para cerrar sesion y volver a registrarse
    private  fun signOut(){
    Firebase.auth.signOut()
    startActivity(Intent(this, MainActivity::class.java))
    }
}
