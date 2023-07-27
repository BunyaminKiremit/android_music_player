package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.adapters.FavoritesAdapter
import com.example.musicplayer.models.FavoriteMusic
import com.google.firebase.database.*

class FavoritesListActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesList: MutableList<FavoriteMusic>

    private lateinit var database: FirebaseDatabase
    private lateinit var favoritesRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_list)

        database = FirebaseDatabase.getInstance()
        favoritesRef = database.reference.child("favoriler")

        favoritesList = mutableListOf()
        favoritesAdapter = FavoritesAdapter(favoritesList)

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        favoritesRecyclerView.adapter = favoritesAdapter

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                favoritesList.clear()

                for (snapshot in dataSnapshot.children) {
                    val music = snapshot.getValue(FavoriteMusic::class.java)
                    music?.let {
                        favoritesList.add(it)
                    }
                }

                favoritesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Hata durumunda işlemler yapılabilir
            }
        }

        favoritesRef.addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesRef.removeEventListener(valueEventListener)
    }
}