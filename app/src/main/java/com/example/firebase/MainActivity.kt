package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var editTextName: EditText
    lateinit var ratingBar: RatingBar
    lateinit var buttonSave: Button

    lateinit var listView: ListView
    lateinit var heroList: MutableList<Hero>
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heroList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("heroes")

        listView = findViewById(R.id.listView)
        editTextName = findViewById(R.id.editTextName)
        ratingBar = findViewById(R.id.ratingBar)
        buttonSave = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            saveHero()
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()){
                    heroList.clear()

                    for (h in p0.children){
                       val hero = h.getValue(Hero::class.java)
                        heroList.add(hero!!)
                    }

                    val adapter = HeroAdapter(applicationContext,R.layout.heroes, heroList)
                    listView.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        });
    }

    private fun saveHero(){
        val name = editTextName.text.toString().trim()

        if (name.isEmpty()){
            editTextName.error = "Please enter a name"
            return
        }


        val heroId = ref.push().key

        val hero =  Hero(heroId.toString(), name, ratingBar.numStars)

        ref.child(heroId.toString()).setValue(hero).addOnCompleteListener {
            Toast.makeText(applicationContext, "Hero saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}