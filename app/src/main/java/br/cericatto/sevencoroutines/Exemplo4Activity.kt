package br.cericatto.sevencoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_example1.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class Exemplo4Activity : AppCompatActivity() {
    companion object {
        private const val TAG = "coroutines"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example4)

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

    private suspend fun fakeApiRequest() {
        withContext(IO) {
            val executionTime = measureTimeMillis {
                /*
                val result1 = async {
                    Log.i(TAG, "launching job1: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()
                 */

                // Classic job/launch.
                var result1 = ""
                val job1 = launch {
                    Log.i(TAG, "launching job1: ${Thread.currentThread().name}")
                    result1 = getResult1FromApi()
                }
                job1.join()
                
                val result2 = async {
                    Log.i(TAG, "launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()
                Log.i(TAG, "Got result2: $result2")
            }
            Log.i(TAG, "debug: job1 and job2 are complete. It took $executionTime ms")
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1700) // Does not block thread. Just suspends the coroutine inside the thread.
        return "Result #2"
    }
}