package br.cericatto.sevencoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_example1.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Exemplo1Activity : AppCompatActivity() {
    companion object {
        private const val TAG = "coroutines"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)

        id_button.setOnClickListener {
            setNewText("Click!")
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = id_text.text.toString() + "\n$input"
        id_text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")
        val result1 = getResult1FromApi() // wait until job is done
        if (result1 == "Result #1") {
            setTextOnMainThread("Got $result1")
            val result2 = getResult2FromApi() // wait until job is done
            if (result2 == "Result #2") {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread.
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread.
        return "Result #2"
    }

    private fun logThread(methodName: String) {
        Log.i(TAG, "debug: $methodName: ${Thread.currentThread().name}")
    }
}