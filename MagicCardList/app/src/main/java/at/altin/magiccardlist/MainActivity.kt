package at.altin.magiccardlist

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

/**
 * @author Altin
 * @version 1.0
 * @since 2023-03-12
 */

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var resultTextView: TextView
    private var page:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "onCreate: ")

        loadData()
    }

    private fun loadData() {
        //init
        val loadDataButton = findViewById<Button>(R.id.button)
        resultTextView = findViewById(R.id.data)
        //set movement method for scrolling
        resultTextView.movementMethod = ScrollingMovementMethod()

        //Event listener
        loadDataButton.setOnClickListener {
            //onclick -loading data
            loadDataButton.text = getString(R.string.loading)
            loadDataButton.isEnabled = false
            resultTextView.text = null


            lifecycleScope.launch(Dispatchers.IO) {
            var intermediateResult = ""
                try {
                    intermediateResult = getContentFromWeb()
                } catch (io: IOException) {
                    withContext(Dispatchers.Main) {
                        val toast = Toast.makeText(
                            applicationContext,
                            "An error occurred, please try again later",
                            Toast.LENGTH_LONG
                        )
                        Log.e(TAG, "Error during loading data: $io")
                        toast.show()
                    }
                }
                if (intermediateResult.isNotBlank()) {
                    val result = async(Dispatchers.Default) {
                        parseJsonAddToCardList(intermediateResult)
                    }
                    withContext(Dispatchers.Main) {
                        resultTextView.text = sortedListToString(result.await())
                        loadDataButton.text = getString(R.string.load)
                        loadDataButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun getContentFromWeb(): String {
        page++
        val pageSize = Random.nextInt()

        if(URL("https://api.magicthegathering.io/v1/cards?page=$page").toString().isBlank()){
            page=1
        }
        findViewById<TextView>(R.id.page).text = buildString { append("Page: "); append(page) }
        val url = URL("https://api.magicthegathering.io/v1/cards?page=$page&pageSize=$pageSize")
        val connection = url.openConnection() as HttpURLConnection

        try {
            val resultAsString = connection.run {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                String(inputStream.readBytes())
            }
            Log.i(TAG, "getContentFromWeb: $resultAsString")
            return resultAsString
        } finally {
            connection.disconnect()
            Log.i(TAG, "getContentFromWeb: disconnected")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e(TAG, "onSaveInstanceState: ")
        outState.putString("key", resultTextView.text.toString())
        outState.putString("page", page.toString())
        Log.i(TAG, "saved: ${resultTextView.text}"+page.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.e(TAG, "onRestoreInstanceState: ")
        resultTextView.text = savedInstanceState.getString("key", "No data")
        page = savedInstanceState.getString("page", "1")!!.toInt()
        findViewById<TextView>(R.id.page).text = buildString { append("Page: "); append(page) }
        Log.i(TAG, "restored: ${resultTextView.text}"+page.toString())
    }
}
