package pt.ipsantarem.apptroka

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import android.content.Intent
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Carrega o layout correto
        setContentView(R.layout.homepage)

        // Ajuste para o scroll respeitar as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referência ao container dos dias
        val weekContainer = findViewById<LinearLayout>(R.id.week_days_container)
        weekContainer.removeAllViews()

        // Gera os 7 dias da semana atuais
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        for (i in 0..6) {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val dayView = TextView(this).apply {
                text = day.toString().padStart(2, '0')
                setPadding(12, 4, 12, 4)
                setTypeface(null, Typeface.BOLD)
                textSize = 16f
                gravity = Gravity.CENTER
                setTextColor(resources.getColor(android.R.color.black, null))
            }
            weekContainer.addView(dayView)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Botão para abrir o calendário completo
        val calendarBtn = findViewById<ImageButton>(R.id.open_calendar_btn)
        calendarBtn.setOnClickListener {
            val today = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    Toast.makeText(this, "$dayOfMonth/${month + 1}/$year", Toast.LENGTH_SHORT)
                        .show()
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val chatButton = findViewById<ImageView>(R.id.chatButton)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }
        val exploreButton = findViewById<ImageView>(R.id.ic_search)
        exploreButton.setOnClickListener {
            startActivity(Intent(this, SwipeProfilesActivity::class.java))
        }


    }
}
