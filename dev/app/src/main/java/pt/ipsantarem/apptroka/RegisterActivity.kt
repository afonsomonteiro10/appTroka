package pt.ipsantarem.apptroka

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class RegisterActivity {

    class RegisterActivity : AppCompatActivity() {

        private lateinit var emailEditText: EditText
        private lateinit var passwordEditText: EditText
        private lateinit var entrarButton: Button
        private lateinit var loginRedirectButton: Button

        private lateinit var auth: FirebaseAuth
        private lateinit var db: FirebaseFirestore

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.signup)

            // Inicializações
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

            emailEditText = findViewById(R.id.emailEditText)
            passwordEditText = findViewById(R.id.passwordEditText)
            entrarButton = findViewById(R.id.entrarButton)
            loginRedirectButton = findViewById(R.id.loginRedirectButton)

            entrarButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Preenche todos os campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                guardarUtilizadorFirestore(userId, email)
                            }
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }

            loginRedirectButton.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        private fun urlDaFoto(value: Any): Comparable<*>? & Serializable? {
            return TODO("Provide the return value")
        }

        private fun guardarUtilizadorFirestore(userId: String, email: String) {
            // Exemplo de dados a guardar num documento "utilizadores/{uid}"
            val nomeDoUtilizador = null
            val habilidadeQueOferece = null
            val habilidadeQueProcura = null
            val dados = hashMapOf(
                "uid" to userId,
                "email" to email,
                "name" to nomeDoUtilizador,
                "photoUrl" to urlDaFoto(),
                "skillOffer" to habilidadeQueOferece,
                "skillWant" to habilidadeQueProcura,
                "createdAt" to System.currentTimeMillis()
            )

            )

            db.collection("utilizadores").document(userId)
                .set(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao guardar dados", Toast.LENGTH_SHORT).show()
                }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            // Em vez de guardar email e ir ao Main, vamos diretamente completar perfil
                            startActivity(Intent(this, CompleteProfileActivity::class.java))
                            finish()
                        }
                    } else {
                        // ...
                    }
                }
        }
    }

}