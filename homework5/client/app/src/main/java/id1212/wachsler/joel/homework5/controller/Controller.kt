package id1212.wachsler.joel.homework5.controller

import id1212.wachsler.joel.homework5.net.ServerConnection
import java.io.IOException
import java.util.concurrent.CompletableFuture

object Controller {

  /**
   * Tries to connect to the server on a separate thread.
   * If something goes wrong the <code>error</code> callback will be called with the error
   * else the <code>success</code> callback will be called.
   */
  fun connect(error: (msg: String) -> Unit, success: () -> Unit) {
    CompletableFuture.runAsync({
      try {
        ServerConnection.connect()
        success()
      } catch (e: IOException) {
        error(e.message!!)
      }
    })
  }

  /**
   * @see ServerConnection#guess
   */
  fun guess(guess: String) {
    CompletableFuture.runAsync({
      ServerConnection.guess(guess)
    })
  }
}
