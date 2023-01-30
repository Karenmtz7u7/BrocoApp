package com.example.brocoapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brocoapp.databinding.ActivityMain3Binding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*

enum class  ProviderType{
    BASIC
}

class MainActivity3 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain3Binding

    //Variables necesarias para trabajar con el RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Home>
    private lateinit var myAdapter: MyAdapterHome

    //Variable para conectar con Firebase
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain3Binding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

       // binding.appBarMain.salidabtn.setOnClickListener {
            //signOut()
        //}

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //PROGRAMACION DEL RECYCLERVIEW

//Enlazar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true);


//Enlazar con el Main "MyAdapter"
        userArrayList = arrayListOf()
        myAdapter = MyAdapterHome(this,userArrayList)
        recyclerView.adapter = myAdapter

//Ejecutar funcion
        EventChangeListener()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity3, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    //Funcion para conectar a Firebase y mostrar datos en el RecyclerView
    private fun EventChangeListener() {

        db=FirebaseFirestore.getInstance()
        db.collection("QR")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error!=null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type== DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(Home::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }


}