package pt.ipsantarem.apptroka

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yuyakaido.android.cardstackview.*
import pt.ipsantarem.apptroka.adapter.CardStackAdapter
import pt.ipsantarem.apptroka.model.UserProfile

class SwipeProfilesActivity : AppCompatActivity(), CardStackListener {

    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val profiles = mutableListOf<UserProfile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_profiles)

        cardStackView = findViewById(R.id.card_stack_view)
        setupCardStackView()
        loadProfilesFromFirestore()
    }

    private fun setupCardStackView() {
        manager = CardStackLayoutManager(this, this)
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL) // permite apenas LEFT/RIGHT
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setStackScale(0.95f)

        adapter = CardStackAdapter(profiles)
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }

    private fun loadProfilesFromFirestore() {
        val currentUserId = auth.currentUser?.uid ?: return

        // Consulta a coleção "utilizadores" para buscar perfis (excluindo o próprio)
        firestore.collection("utilizadores")
            .whereNotEqualTo("uid", currentUserId)
            .get()
            .addOnSuccessListener { result ->
                profiles.clear()
                for (doc in result.documents) {
                    val profile = doc.toObject(UserProfile::class.java)
                    if (profile != null) {
                        profiles.add(profile)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro a carregar perfis: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // =============================================================================================
    // Implementação dos métodos de CardStackListener (para capturar swipe left/right)
    // =============================================================================================

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        // opcional: feedback visual enquanto arrasta
    }

    override fun onCardSwiped(direction: Direction?) {
        // Quando um card é completamente deslizado:
        val position = manager.topPosition - 1   // posição do card que saiu
        if (position < 0 || position >= profiles.size) return

        val swipedProfile = profiles[position]
        if (direction == Direction.Right) {
            // GOSTEI: guardamos no Firestore que currentUser gostou deste swipedProfile.uid
            registerLike(swipedProfile.uid)
        } else if (direction == Direction.Left) {
            // REJEITEI: (podes guardar como “dislike” ou simplesmente ignorar)
            // Por simplicidade, não guardamos dislikes.
        }

        // Se chegarmos ao fim da lista, podes recarregar ou mostrar mensagem
        if (manager.topPosition == profiles.size) {
            Toast.makeText(this, "Acabaste de ver todos os perfis.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCardRewound() {
        // Nunca vamos rebobinar neste fluxo
    }

    override fun onCardCanceled() {
        // Se o arrastar não resultar em swipe (voltou atrás)
    }

    override fun onCardAppeared(view: View?, position: Int) {
        // Quando um novo card aparece em cima
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        // Quando um card desaparece
    }

    // =============================================================================================
    // Registar “like” no Firestore e verificar se há match
    // =============================================================================================

    private fun registerLike(likedUserId: String) {
        val currentUserId = auth.currentUser?.uid ?: return

        // 1) Guardar no sub-collection “likes” do utilizador atual
        val likeData = hashMapOf(
            "likedAt" to System.currentTimeMillis()
        )
        firestore.collection("utilizadores")
            .document(currentUserId)
            .collection("likes")
            .document(likedUserId)
            .set(likeData)
            .addOnSuccessListener {
                // 2) Verificar se o utilizador “likedUserId” também gostou de “currentUserId”
                checkForMatch(currentUserId, likedUserId)
            }
            .addOnFailureListener { e ->
                Log.e("SwipeProfiles", "Falha ao registar like: ${e.message}")
            }
    }

    private fun checkForMatch(currentUserId: String, likedUserId: String) {
        // Verificar se “likedUserId” já guardou um like para o “currentUserId”
        firestore.collection("utilizadores")
            .document(likedUserId)
            .collection("likes")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    // HÁ match bilateral!
                    createMatch(currentUserId, likedUserId)
                }
            }
            .addOnFailureListener { e ->
                Log.e("SwipeProfiles", "Erro ao verificar match: ${e.message}")
            }
    }

    private fun createMatch(userA: String, userB: String) {
        // Podes guardar o match numa coleção “matches” ou em cada perfil.
        // Exemplo simples: em cada utilizador criar campo “matches/{otherUid}”
        val matchData = hashMapOf(
            "matchedAt" to System.currentTimeMillis()
        )

        // Guardar para userA
        firestore.collection("utilizadores")
            .document(userA)
            .collection("matches")
            .document(userB)
            .set(matchData)

        // Guardar para userB
        firestore.collection("utilizadores")
            .document(userB)
            .collection("matches")
            .document(userA)
            .set(matchData)
            .addOnSuccessListener {
                Toast.makeText(this, "É um match! 🎉", Toast.LENGTH_LONG).show()
                // Aqui podes redirecionar para ChatsActivity ou mostrar notificação
            }
            .addOnFailureListener { e ->
                Log.e("SwipeProfiles", "Erro ao criar match: ${e.message}")
            }
    }
}
