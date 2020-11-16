package br.cericatto.sevencoroutines

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id_ex1_button.setOnClickListener {
            startActivity(Intent(this, Exemplo1Activity::class.java))
        }
        id_ex2_button.setOnClickListener {
            startActivity(Intent(this, Exemplo2Activity::class.java))
        }
    }
}