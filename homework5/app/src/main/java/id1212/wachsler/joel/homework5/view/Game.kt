package id1212.wachsler.joel.homework5.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import id1212.wachsler.joel.homework5.R

class Game : AppCompatActivity() {
  private var guessInput: EditText? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_game)

    guessInput = findViewById<EditText>(R.id.guess_input)
  }

  fun guess(v: View) {
    val msg = guessInput!!.text
    println("The guess to send is: $msg")
  }
}
