// MainActivity.kt
package com.example.coinrunnergame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameListener {

    private lateinit var gameView: GameView
    private lateinit var scoreTextView: TextView
    private lateinit var gameOverLayout: LinearLayout
    private lateinit var finalScoreTextView: TextView
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)
        scoreTextView = findViewById(R.id.scoreTextView)
        gameOverLayout = findViewById(R.id.gameOverLayout)
        finalScoreTextView = findViewById(R.id.finalScoreTextView)
        restartButton = findViewById(R.id.restartButton)

        gameView.gameListener = this // Define a MainActivity como listener

        restartButton.setOnClickListener {
            gameView.restartGame()
        }
    }

    // Implementação dos métodos da interface GameListener
    override fun onScoreUpdated(newScore: Int) {
        runOnUiThread { // Atualiza a UI na thread principal
            scoreTextView.text = "Score: $newScore"
        }
    }

    override fun onGameOver(gameOver: Boolean, finalScore: Int) {
        runOnUiThread { // Atualiza a UI na thread principal
            if (gameOver) {
                finalScoreTextView.text = "Final Score: $finalScore"
                gameOverLayout.visibility = View.VISIBLE
                scoreTextView.visibility = View.GONE // Esconder score durante Game Over
            } else {
                gameOverLayout.visibility = View.GONE
                scoreTextView.visibility = View.VISIBLE // Mostrar score novamente
            }
        }
    }

    override fun onGameStarted() {
        runOnUiThread {
            scoreTextView.visibility = View.VISIBLE
            gameOverLayout.visibility = View.GONE
            scoreTextView.text = "Score: 0"
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure game restarts if activity is resumed
        if (gameView.holder.surface.isValid) { // Check if surface is valid before starting
            // If the game was already running (e.g., after minimizing and reopening),
            // this might restart it. You might need more sophisticated state management.
            // For now, let's just make sure it's running.
            // gameView.startGame() // This might be called by surfaceCreated already
        }
    }

    override fun onPause() {
        super.onPause()
        // GameView handles stopping its thread in surfaceDestroyed
    }
}