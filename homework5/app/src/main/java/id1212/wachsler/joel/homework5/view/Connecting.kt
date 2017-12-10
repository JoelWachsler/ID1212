package id1212.wachsler.joel.homework5.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import id1212.wachsler.joel.homework5.R
import kotlin.concurrent.thread

class Connecting : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_connecting)

    // Connect on a separate thread.
    thread {
      connect()
      startActivity(Intent(this, Game::class.java))
    }
  }

  /**
   * Connects to the server.
   */
  fun connect() {
    println("Started sleeping...")
    Thread.sleep(5000)
    println("Sleep over...")
  }
}
