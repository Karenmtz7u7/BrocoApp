package com.example.brocoapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.brocoapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.loginBtn.setOnClickListener {
            setup()

        }
        binding.crearcuentabtn.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java));

            binding.edpass.setText("")
            binding.edmail.setText("")
        }
        binding.restablecertxt.setOnClickListener {
            startActivity(Intent(this, MainActivity9::class.java));
            binding.edpass.setText("")

            //deja vacios los campos despues de dar Tap en ellos
        }
        verify()
    }

    private fun verify(){
        binding.edmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.edmail.text.toString()).matches()){
                    binding.loginBtn.isEnabled = true
                }else{
                    binding.loginBtn.isEnabled = false
                    binding.edmail.error = "Correo Inválido"
                }
            }
            override fun afterTextChanged(s: Editable?) {}

        })

    }

    private fun setup() {

        //Revisar que los campos no esten vacios
        if (binding.edpass.text!!.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.edmail.text.toString(),
                binding.edpass.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    ShowHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    scaner(it.result?.user?.email?:"",ProviderType.BASIC)

                }
                else {
                    ShowAlert()
                }
            }
        }
    }




    private fun ShowAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Correo o contraseña incorrectos.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    private fun ShowHome(email:String, provider:ProviderType){
        val HomeIntent=Intent(this,MainActivity3::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(HomeIntent)
    }
    private fun scaner(email:String,provider:ProviderType){
        val HomeIntent=Intent(this,MainActivity7::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }

    }

}


