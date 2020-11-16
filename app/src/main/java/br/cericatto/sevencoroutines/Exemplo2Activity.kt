package br.cericatto.sevencoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_example1.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class Exemplo2Activity : AppCompatActivity() {
    companion object {
        private const val TAG = "coroutines"
    }

    private val JOB_TIMEOUT = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example2)

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
        withContext(IO) {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi() // wait until job is done
                setTextOnMainThread("Got $result1")
                val result2 = getResult2FromApi() // wait until job is done
                setTextOnMainThread("Got $result2")
            } // waiting for job to complete

            if (job == null) {
                val cancelMessage = "Cancelling job ... Job took longer than $JOB_TIMEOUT ms"
                Log.i(TAG, "debug: $cancelMessage")
                setTextOnMainThread(cancelMessage)
            }
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread.
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread.
        return "Result #2"
    }
}