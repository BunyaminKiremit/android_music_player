package com.example.musicplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.adapters.MusicAdapter
import com.example.musicplayer.api.RetrofitClient
import com.example.musicplayer.models.MusicCategory
import com.example.musicplayer.models.İtem
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        // Firebase veritabanı referansını al

        uploadDataToFirebase()
        // Verileri Firebase'den al ve RecyclerView ile görüntüle
        fetchDataFromFirebase()





    }
    private fun fetchDataFromFirebase() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val veriReferansi = firebaseDatabase.reference.child("music")

        veriReferansi.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val musicCategories: MutableList<MusicCategory> = mutableListOf()

                for (categorySnapshot in dataSnapshot.child("musicCategories").children) {
                    val baseTitle = categorySnapshot.child("baseTitle").getValue(String::class.java)
                    val items: MutableList<İtem> = mutableListOf()

                    for (itemSnapshot in categorySnapshot.child("items").children) {
                        val musicItem = itemSnapshot.getValue(İtem::class.java)
                        musicItem?.let {
                            items.add(it)
                        }
                    }

                    val musicCategory = MusicCategory(baseTitle, items)
                    musicCategories.add(musicCategory)
                }

                displayDataInRecyclerView(musicCategories)
                Log.d(
                    "Firebase",
                    "Veriler Firebase'den alındı: ${musicCategories.size} adet müzik kategorisi"
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase Hata", databaseError.toString())
            }
        })
    }


    private fun displayDataInRecyclerView(musicCategories: List<MusicCategory>) {
        val musicAdapter = MusicAdapter(musicCategories)
        recyclerView.adapter = musicAdapter
    }

    private fun uploadDataToFirebase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.musicApiService.getMusicData()
                if (response.isSuccessful) {
                    val musicData = response.body()

                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val veriReferansi = firebaseDatabase.reference.child("music")

                    // Müzik verilerini Firebase veritabanına yükleyin
                    veriReferansi.setValue(musicData)
                        .addOnSuccessListener {
                            // Yükleme başarılı olduğunda yapılacak işlemler
                            Log.d("Firebase", "Veriler Firebase'e yüklendi")
                        }
                        .addOnFailureListener { exception ->
                            // Yükleme başarısız olduğunda yapılacak işlemler
                            Log.d("Firebase Hata", exception.toString())
                        }
                } else {
                    // Hata durumunu işleyin
                    Log.d("Retrofit Hata", "API yanıtı başarısız: ${response.code()}")
                }
            } catch (e: Exception) {
                // Hata durumunu işleyin
                Log.d("Hata", e.toString())
            }
        }
    }



}
