package br.cericatto.sevencoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class Exemplo5Activity : AppCompatActivity() {
    companion object {
        private const val TAG = "coroutines"
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.i(TAG, "Exception thrown in one of the children: $exception")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example4)

        main()
    }

    private fun main() {
        val parentJob = CoroutineScope(IO).launch(handler) {
            // --------------- JOB A ---------------
            val jobA = launch {
                val resultA = getResult(1)
                Log.i(TAG, "resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    Log.i(TAG, "Error getting resultA: $throwable")
                }
            }

            // --------------- JOB B ---------------
            val jobB = launch {
                val resultB = getResult(2)
                Log.i(TAG, "resultB: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    Log.i(TAG, "Error getting resultB: $throwable")
                }
            }

            // --------------- JOB C ---------------
            val jobC = launch {
                val resultC = getResult(3)
                Log.i(TAG, "resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    Log.i(TAG, "Error getting resultC: $throwable")
                }
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                Log.i(TAG, "Parent job failed: $throwable")
            } else {
                Log.i(TAG, "Parent job SUCCESS!")
            }
        }
    }

    suspend fun getResult(number: Int): Int {
        return withContext(Main) {
            delay(number * 500L)
            if (number == 2) {
                throw Exception("Error getting result for number: $number")
            }
            number * 2
        }
    }
}