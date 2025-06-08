package pt.ipsantarem.apptroka

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pt.ipsantarem.apptroka.model.UserProfile
import java.util.*

class CompleteProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var imageProfileUpload: ImageView
    private lateinit var spinnerSkillOffer: Spinner
    private lateinit var spinnerSkillWant: Spinner
    private lateinit var buttonComplete: Button

    private var selectedImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        editTextName = findViewById(R.id.editTextName)
        imageProfileUpload = findViewById(R.id.imageProfileUpload)
        spinnerSkillOffer = findViewById(R.id.spinnerSkillOffer)
        spinnerSkillWant = findViewById(R.id.spinnerSkillWant)
        buttonComplete = findViewById(R.id.buttonCompleteProfile)

        // Configurar spinners com uma lista de habilidades (podes personalizar)
        val skills = listOf("Andar de Bicicleta", "Italiano", "Cozinhar", "Fotografia", "C#",
            "Jogar Futebol", "Meditar", "Tocar Arpa", "Estalar Dedos")
        val adapterSkills = ArrayAdapter(this, android.R.layout.simple_spinner_item, skills)
        adapterSkills.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSkillOffer.adapter = adapterSkills
        spinnerSkillWant.adapter = adapterSkills

        imageProfileUpload.setOnClickListener {
            // Abrir galeria para escolher foto
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        buttonComplete.setOnClickListener {
            saveUserProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            Glide.with(this).load(selectedImageUri).into(imageProfileUpload)
        }
    }

    private fun saveUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        val name = editTextName.text.toString().trim()
        val skillOffer = spinnerSkillOffer.selectedItem as String
        val skillWant = spinnerSkillWant.selectedItem as String

        if (name.isEmpty()) {
            editTextName.error = "Insere o teu nome"
            return
        }

        // 1) Se houver imagem selecionada, carrega-a para o Storage
        if (selectedImageUri != null) {
            val ref = storage.reference.child("profile_images/$uid/${UUID.randomUUID()}")
            ref.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val photoUrl = uri.toString()
                        saveProfileToFirestore(uid, name, photoUrl, skillOffer, skillWant)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Falha no upload da imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Sem imagem, passamos string vazia
            saveProfileToFirestore(uid, name, "", skillOffer, skillWant)
        }
    }

    private fun saveProfileToFirestore(
        uid: String,
        name: String,
        photoUrl: String,
        skillOffer: String,
        skillWant: String
    ) {
        val userProfile = UserProfile(
            uid = uid,
            name = name,
            email = auth.currentUser?.email ?: "",
            photoUrl = photoUrl,
            skillOffer = skillOffer,
            skillWant = skillWant,
            createdAt = System.currentTimeMillis()
        )

        firestore.collection("utilizadores")
            .document(uid)
            .set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show()
                // Encaminha para a lista de perfis (SwipeProfilesActivity)
                startActivity(Intent(this, SwipeProfilesActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro a guardar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
