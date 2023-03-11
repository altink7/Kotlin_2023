package at.altin.kotlin_homework

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/***
 * @author Altin Kelmendi
 * @version 1.0
 * Kotlin - Homework 1
 */
class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val result = intent.extras?.getInt("RESULT") ?: 0
        findViewById<TextView>(R.id.resultNavigate).text = "$result"
        Log.e("SecondActivity", "Calculated Value is $result")
    }

}