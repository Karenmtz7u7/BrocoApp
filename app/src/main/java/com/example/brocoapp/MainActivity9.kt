package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMain9Binding
import com.google.firebase.auth.FirebaseAuth

class MainActivity9 : AppCompatActivity() {

    private lateinit var binding: ActivityMain9Binding
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain9Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java));
            binding.edmail.setText("")
        }
        binding.aceptarbtn.setOnClickListener {
            var username = binding.edmail
            forgotPassword(username)
            startActivity(Intent(this, MainActivity::class.java));
            binding.edmail.setText("")
        }
        verify()
    }
    private fun verify(){
        binding.edmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.edmail.text.toString()).matches()){
                    binding.aceptarbtn.isEnabled = true
                }else{
                    binding.aceptarbtn.isEnabled = false
                    binding.edmail.error = "Correo Inválido"
                }
            }
            override fun afterTextChanged(s: Editable?) {}

        })

    }

    private fun forgotPassword(username: EditText) {

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se envió un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show()
                }
            }
    }
}