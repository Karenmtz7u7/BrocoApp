package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrarbtn.setOnClickListener {
            setup()
        }
        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            binding.edpassru.setText("")
            binding.verifypass.setText("")
            binding.edmailru.setText("")
        }

        verify()
    }
    // Verifica que el correo electronico que se introduza sea un Correo electronico
private fun verify(){
        binding.edmailru.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.edmailru.text.toString()).matches()){
                    binding.registrarbtn.isEnabled = true
                }else{
                    binding.registrarbtn.isEnabled = false
                    binding.edmailru.error = "Correo Inválido"
                }
            }
            override fun afterTextChanged(s: Editable?) {}

        })

}
private fun setup() {
    val pass = binding.edpassru.text.toString()
    val verify = binding.verifypass.text.toString()

    if(pass != verify) {
        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        binding.edpassru.setText("")
        binding.verifypass.setText("")
    } else {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.edmailru.text.toString(),
            binding.edpassru.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                ShowHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                CuentaBrocopoints()
                finish()
            } else {
                ShowAlert()
            }
        }
    }

}

    //Esta funcion permite registrar al usuario en la entidad "Brocopoints" para llevar un registro de los puntos
    fun CuentaBrocopoints(){
        val user = FirebaseAuth.getInstance().currentUser

        val dato = hashMapOf(
            "Brocopoints" to 1
        )
        db.collection("Brocopoints").document(user!!.email.toString())
            .set(dato)
                .addOnSuccessListener {
                    Toast.makeText(this, "Cuentas con 1 brocopoint por registrarte", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
    }

//Configurar la pantalla

private fun ShowAlert(){
    val builder= AlertDialog.Builder(this)
    builder.setTitle("Error")
    builder.setMessage("El correo electrónico ya está registrado en BrocoApp.")
    builder.setPositiveButton("Aceptar", null)
    val dialog: AlertDialog =builder.create()
    dialog.show()
}
private fun ShowHome(email:String, provider:ProviderType){
    val HomeIntent=Intent(this,MainActivity::class.java).apply{
        putExtra("email",email)
        putExtra("provider",provider.name)
    }
    startActivity(HomeIntent)

}
}
