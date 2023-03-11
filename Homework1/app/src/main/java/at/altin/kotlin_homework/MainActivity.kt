package at.altin.kotlin_homework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

/***
 * @author Altin Kelmendi
 * @version 1.0
 * Kotlin - Homework 1
 */
class MainActivity : AppCompatActivity() {
    companion object {
        const val logTag = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(logTag, R.layout.activity_main.toString())
        Log.e(logTag, "onCreate")

        var result: Int
        val input1 = findViewById<TextInputLayout>(R.id.input1)
        val input2 = findViewById<TextInputLayout>(R.id.input2)
        val calculateButton = findViewById<Button>(R.id.calculate)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        //initialize Values --8--
        input1.editText?.setText("0")
        input2.editText?.setText("0")

        //Calculate
        calculateButton.setOnClickListener {
            result = checkInteger(input1) + checkInteger(input2)
            findViewById<TextView>(R.id.result).text = "$result"
        }
        //Navigate
        findViewById<Button>(R.id.navigate).setOnClickListener {
            val startSecondActivityIntent = Intent(this, SecondActivity::class.java)
            result = checkInteger(input1) + checkInteger(input2)
            startSecondActivityIntent.putExtra("RESULT", result)
            startActivity(startSecondActivityIntent)
        }
        //Seekbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                findViewById<TextView>(R.id.seekBarResult).text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.i(logTag, "Seekbar start")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.i(logTag, "Seekbar stop")
            }
        })
    }

    private fun checkInteger(input1: TextInputLayout): Int {
        return if( input1.editText?.text.toString().toIntOrNull() != null) {
            input1.editText?.text.toString().toInt()
        } else {
            0
        }
    }
}