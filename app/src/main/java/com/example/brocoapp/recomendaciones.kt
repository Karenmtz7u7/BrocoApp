package com.example.brocoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.brocoapp.databinding.ActivityRecomendacionesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class recomendaciones : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendacionesBinding
    private lateinit var auth: FirebaseAuth
    private val fileResult = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityRecomendacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passmodbtx.setOnClickListener {
            val intent = Intent(this, cambio_pass::class.java)
            this.startActivity(intent)
        }
        binding.deleteusertxt.setOnClickListener {
            val intent = Intent(this, eliminarcuenta::class.java)
            this.startActivity(intent)
        }
        binding.signOutbtn.setOnClickListener {
            cerrarsesion()
            finish()
        }
        binding.guardarbtn.setOnClickListener {
            val nombre = binding.nameusertext.text.toString()
            updateProfile(nombre)
            binding.nameusertext.setText("")
        }
        binding.updatephoto.setOnClickListener {
            fileManager()
        }
        Update()
    }

    //Funcion obtiene el correo del usuario para mostrarse en un txtview
    private fun Update(){
        val user = auth.currentUser
        if(user != null){
            binding.emaildefaulttxt.text = user.email
            binding.namedefaulttxt.text = user.displayName
            binding.nameusertext.setText(user.displayName)
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                
                .placeholder(R.drawable.avatar)
                .into(binding.userimageview)
        }
    }
    //Esta funcion actualiza el nombre de perfil
    private fun updateProfile(nombre: String){
        val user = Firebase.auth.currentUser
        val actualizar = userProfileChangeRequest {
            displayName = nombre
        }

        user!!.updateProfile(actualizar).addOnCompleteListener { it ->
            if(it.isSuccessful){
                binding.nameusertext.setText("")
                Toast.makeText(this, "Datos actualizados correctamente.",
                    Toast.LENGTH_SHORT).show()
                Update()
            }

        }
    }

    //Funcion para cambiar la foto de perfil
    private fun fileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, fileResult)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data

                uri?.let { imageUpload(it) }

            }
        }
    }

    private fun imageUpload(mUri: Uri) {
        val user = auth.currentUser
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Users")
        val fileName: StorageReference = folder.child("img"+user!!.uid)
        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(uri.toString())
                }
                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Foto de perfil actualizada correctamente.",
                                Toast.LENGTH_SHORT).show()
                            Update()
                        }
                    }
            }
        }.addOnFailureListener {
            Log.i("TAG", "Error al actualizar la foto de perfil")
        }
    }

    //Funcion para cerrar la sesion del usuario
    private fun cerrarsesion() {
        Firebase.auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}