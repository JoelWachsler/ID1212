package id1212.wachsler.joel.homework5.client.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.TextView
import id1212.wachsler.joel.homework5.client.R
import id1212.wachsler.joel.homework5.client.controller.Controller
import id1212.wachsler.joel.homework5.common.GameState

/**
 * Game action view.
 */
class Game : AppCompatActivity() {
  private var guessInput: EditText? = null
  private var stateOutput: TextView? = null
  private var triesOutput: TextView? = null
  private var scoreOutput: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_game)

    // Get the elements.
    guessInput = findViewById(R.id.guess_input)
    stateOutput = findViewById<EditText>(R.id.state_output)
    triesOutput = findViewById<EditText>(R.id.tries_output)
    scoreOutput = findViewById<EditText>(R.id.score_output)

    // Listen for server messages
    registerListener()

    // Set default game state values
    updateGameState(GameState(0, 0, 0, ""))
  }

  private fun registerListener() {
    Controller.registerListener(error = { msg ->
      runOnUiThread({
        AlertDialog.Builder(this)
          .setTitle("Something went wrong!")
          .setMessage(msg)
          .setPositiveButton("Ok", { _: DialogInterface, _: Int ->
            // If something goes wrong we're going back to the connection screen.
            startActivity(Intent(this, Connecting::class.java))
          })
          .create()
          .show()
      })
    }, gameStateUpdate = {
      updateGameState(it)
    })
  }

  @SuppressLint("SetTextI18n")
  private fun updateGameState(gameState: GameState) {
    guessInput?.setText("")
    stateOutput?.text = "State: ${gameState.state}"
    triesOutput?.text = "Tries: ${gameState.tries}/${gameState.totalTries}"
    scoreOutput?.text = "Score: ${gameState.score}"
  }

  fun guess(v: View) {
    val msg = guessInput!!.text
    Controller.guess(msg.toString())
  }
}
