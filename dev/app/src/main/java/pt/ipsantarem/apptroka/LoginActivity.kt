package pt.ipsantarem.apptroka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Certifica-te que corresponde ao teu layout

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // opcional: impede voltar ao login com o botão "voltar"
        }
    }
}
