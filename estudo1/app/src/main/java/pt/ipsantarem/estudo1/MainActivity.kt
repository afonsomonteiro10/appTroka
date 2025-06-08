package pt.ipsantarem.estudo1

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Telephony.Mms.Addr
import android.widget.Button
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener  {

    lateinit var btnAdd: Button
    lateinit var btnSubtract: Button
    lateinit var btnMultiply: Button
    lateinit var btnDivide: Button
    lateinit var etA: EditText
    lateinit var etB: EditText
    lateinit var resultTv : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnAdd = findViewById(R.id.btn_add)
        btnSubtract = findViewById(R.id.btn_subtract)
        btnMultiply = findViewById(R.id.btn_multiply)
        btnDivide = findViewById(R.id.btn_divide)
        etA = findViewById(R.id.et_A)
        etB = findViewById(R.id.et_B)
        resultTv = findViewById(R.id.result_Tv)

        btnAdd.setOnClickListener(this)
        btnSubtract.setOnClickListener(this)
        btnMultiply.setOnClickListener(this)
        btnDivide.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var a = etA.text.toString().toDouble()
        var b = etB.text.toString().toDouble()
        var result = 0.0
        when(v?.id){
            R.id.btn_add->{
                result = a+b
            }
            R.id.btn_subtract->{
                result = a-b
            }
            R.id.btn_multiply->{
                result = a*b
            }
            R.id.btn_divide->{
                result = a/b
            }
        }
        resultTv.text = "Result is $result"
    }
}

