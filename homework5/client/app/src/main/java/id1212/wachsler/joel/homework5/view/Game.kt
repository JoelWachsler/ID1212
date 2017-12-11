package id1212.wachsler.joel.homework5.view

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import id1212.wachsler.joel.homework5.R

/**
 * Game action view.
 */
class Game : AppCompatActivity() {
  private var guessInput: EditText? = null
  private var stateOutput: TextView? = null
  private var triesOutput: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_game)

    // Get the elements.
    guessInput = findViewById(R.id.guess_input)
    stateOutput = findViewById<EditText>(R.id.state_output)
    triesOutput = findViewById<EditText>(R.id.tries_output)

    // Init values
    newGame()
  }

  @SuppressLint("SetTextI18n")
  private fun newGame() {
    guessInput!!.setText("")
    stateOutput!!.text = "State: _ _ _ _ _ "
    triesOutput!!.text = "Tries: 0/8"
  }

  fun guess(v: View) {
    val msg = guessInput!!.text
    println("The guess to send is: $msg")
  }
}
