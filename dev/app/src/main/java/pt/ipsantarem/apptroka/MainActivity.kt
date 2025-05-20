package pt.ipsantarem.apptroka

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DatePickerDialog
import android.graphics.Typeface
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val weekContainer = findViewById<LinearLayout>(R.id.week_days_container)
        weekContainer.removeAllViews()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        for (i in 0..6) {
            val dayView = TextView(this).apply {
                val day = calendar.get(Calendar.DAY_OF_MONTH)
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

        val calendarBtn = findViewById<ImageButton>(R.id.open_calendar_btn)
        calendarBtn.setOnClickListener {
            val today = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    Toast.makeText(this, "$dayOfMonth/${month + 1}/$year", Toast.LENGTH_SHORT).show()
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ).show()
        }




    }
}