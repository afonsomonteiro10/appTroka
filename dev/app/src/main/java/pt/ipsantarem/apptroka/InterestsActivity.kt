package com.teuprojeto.troka

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InterestsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val selectedSkills = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_interests)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val buttonIds = listOf(
            R.id.btn_bicicleta, R.id.btn_cozinhar, R.id.btn_ingles,
            R.id.btn_costurar, R.id.btn_dedos, R.id.btn_arpa,
            R.id.btn_outfits, R.id.btn_pintura, R.id.btn_futebol,
            R.id.btn_organizacao, R.id.btn_fotografia, R.id.btn_nadar,
            R.id.btn_meditar
        )

        buttonIds.forEach { id ->
            val btn = findViewById<Button>(id)
            btn.setOnClickListener {
                val habilidade = btn.text.toString()

                if (selectedSkills.contains(habilidade)) {
                    selectedSkills.remove(habilidade)
                    btn.setBackgroundResource(R.drawable.outline_button)
                } else {
                    selectedSkills.add(habilidade)
                    btn.setBackgroundResource(R.drawable.selected_button)
                }
            }
        }

        val btnContinuar = findViewById<Button>(R.id.btn_continuar)
        btnContinuar.setOnClickListener {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                db.collection("users").document(uid)
                    .update("interests", selectedSkills.toList())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Habilidades guardadas!", Toast.LENGTH_SHORT).show()
                        // Podes navegar para outra activity aqui, se quiseres
                        // startActivity(Intent(this, ProximaActivity::class.java))
                        // finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao guardar", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
