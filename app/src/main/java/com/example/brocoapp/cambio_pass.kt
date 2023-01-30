package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityCambioPassBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class cambio_pass : AppCompatActivity() {

    private lateinit var binding: ActivityCambioPassBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambioPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val passwordRegex = Pattern.compile("^" +
                ".{6,}" +                // Al menos 6 caracteres
                "$")
        binding.guardarbtn.setOnClickListener {
            val currentPassword = binding.passact.text.toString()
            val newPassword = binding.edpass.text.toString()
            val repeatPassword = binding.verifypass.text.toString()

            if (newPassword.isEmpty() || !passwordRegex.matcher(newPassword).matches()){
                Toast.makeText(this, "Contraseña débil.",
                    Toast.LENGTH_SHORT).show()
            } else if (newPassword != repeatPassword){
                Toast.makeText(this, "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show()
            } else {
                chagePassword(currentPassword, newPassword)
            }
            binding.passact.setText("")
            binding.edpass.setText("")
            binding.verifypass.setText("")
        }
    }

    private  fun chagePassword(current : String, password : String){
        val user = auth.currentUser
        if (user != null){
            val email = user.email
            val credential = EmailAuthProvider
                .getCredential(email!!, current)
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        user.updatePassword(password)
                            .addOnCompleteListener { taskUpdatePassword ->
                                if (taskUpdatePassword.isSuccessful) {
                                    Toast.makeText(this, "Se cambió la contraseña.",
                                        Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, recomendaciones::class.java)
                                    this.startActivity(intent)
                                }
                            }
                    } else {
                        Toast.makeText(this, "La contraseña actual es incorrecta.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
