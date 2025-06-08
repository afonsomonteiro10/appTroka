package pt.ipsantarem.apptroka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.ipsantarem.apptroka.adapter.MatchAdapter
import pt.ipsantarem.apptroka.model.UserProfile

class ChatsActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val matchProfiles = mutableListOf<UserProfile>()
    private lateinit var adapter: MatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chats)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        adapter = MatchAdapter(matchProfiles) { profile ->
            // Ao clicar num match, abrir conversação (não implementado aqui)
            Toast.makeText(this, "Abre chat com ${profile.name}", Toast.LENGTH_SHORT).show()
            // Exemplo: startActivity(Intent(this, ChatConversationActivity::class.java).apply {
            //     putExtra("otherUid", profile.uid)
            // })
        }
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        loadMatches()
    }

    private fun loadMatches() {
        val currentUserId = auth.currentUser?.uid ?: return

        // 1) Buscamos todos os documentos em usuarios/{currentUser}/matches
        firestore.collection("utilizadores")
            .document(currentUserId)
            .collection("matches")
            .get()
            .addOnSuccessListener { snapshot ->
                matchProfiles.clear()
                for (doc in snapshot.documents) {
                    val otherUid = doc.id
                    // 2) Para cada matchedUid, buscar o perfil completo
                    firestore.collection("utilizadores")
                        .document(otherUid)
                        .get()
                        .addOnSuccessListener { userDoc ->
                            val profile = userDoc.toObject(UserProfile::class.java)
                            if (profile != null) {
                                matchProfiles.add(profile)
                                adapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatsActivity", "Erro ao buscar perfil do match: ${e.message}")
                        }
                }

                if (snapshot.isEmpty) {
                    Toast.makeText(this, "Ainda não tens matches.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro a carregar matches: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
