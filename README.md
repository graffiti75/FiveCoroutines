# SevenCoroutines

# Concepts Covered

This application contains 5 examples of use of Coroutines in Android, using language Kotlin:
- CoroutineScope and WithContext
- Timeout
- InvokeOnCompletion
- Async and Await
- Coroutine Exception Handler

# Additional Info

In this section we will cover some important Coroutines concepts that can lead to confusion: _launch_, _async_ and _supervisorScope_.

## launch

Launches a new coroutine in the background and returns a reference to it as a Job object. The launch coroutine builder does a fire and forget, it does not return any result to the caller asides the job instance which is just a handle to the background operation. It inherits the context and job from the scope where it was called but these can be overridden.

```
fun CoroutineScope.launch(
   context: CoroutineContext = EmptyCoroutineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit
): Job
```

## async

Async coroutine builder is similar to launch in its structure but returns a Deferred<T> instead of a Job. A Deferred<T> is a light-weight non blocking future that represents a promise to deliver a result later. You will need to call the suspending function await on the deferred object to get the eventual result.

```
fun CoroutineScope.async(
   context: CoroutineContext = EmptyCorotuineContext,
   start: CoroutineStart = CoroutineStart.DEFAULT,
   block: suspend CoroutineScope.() -> Unit
): Deferred<T>
```

The parameters supplied to async are pretty much the same as launch. The returned Deferred<T> extends Job so it can be used in the same way as a Job . Async coroutine builder can be used to run several independent operations in parallel. The sample below demonstrates this:

```
fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        val first = async { firstNumber() }
        val second = async { secondNumber() }
        val third = async { thirdNumber() }
        val result = first.await() + second.await() + third.await()
    }
    println(time) //prints 7 seconds
}
suspend fun firstNumber(): Int {
    delay(3_000) // 3 seconds delay
    return 5
}
suspend fun secondNumber(): Int {
    delay(5_000) // 5 seconds delay
    return 8
}
suspend fun thirdNumber(): Int {
    delay(7_000) // 7 seconds delay
    return 10
}
// the above code prints out 7 seconds, which is the time it took 
// the longest running function to return
```

## supervisorScope

A failure of a child does not cause this scope to fail and does not affect its other children, so a custom policy for handling failures of its children can be implemented. See SupervisorJob for details. A failure of the scope itself (exception thrown in the block or cancellation) fails the scope with all its children, but does not cancel the parent job. 

```
fun test6() {
    val scope = CoroutineScope(Job() + Dispatchers.IO)
    scope.launch {
        supervisorScope {
            repeat(2) { i ->
                try {
                    val result = async { doWork(i) }.await()
                    log("work result: ${result.toString()}")
                } catch (t: Throwable) {
                    log("caught exception: $t")
                }
            }
        }
    }
}
private suspend fun doWork(iteration: Int): Int {
    delay(100)
    log("working on $iteration")
    val i = iteration
    return if (i == 0) throw WorkException("work failed") else 0
}
private fun log(msg: String) {
    Log.i("TestUseCase", msg);
}
private class WorkException(msg: String): RuntimeException(msg) {}
```
