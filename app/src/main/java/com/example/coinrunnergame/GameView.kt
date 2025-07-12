// GameView.kt
package com.example.coinrunnergame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {

    private lateinit var holder: SurfaceHolder
    private var running = false
    private lateinit var gameThread: Thread

    // Game objects
    private var player: Player? = null
    private val obstacles = mutableListOf<Obstacle>()
    private val coins = mutableListOf<Coin>()
    private var background1: Background? = null
    private var background2: Background? = null

    // Game state
    private var score = 0
    private var isGameOver = false
    private var lastObstacleSpawnTime: Long = 0
    private var lastCoinSpawnTime: Long = 0
    private val spawnIntervalObstacle = 1500L
    private val spawnIntervalCoin = 1000L

    // Listener para eventos do jogo (score update, game over)
    var gameListener: GameListener? = null

    init {
        // AQUI: Você já está adicionando o callback para o holder
        // Não precisa inicializar 'holder' aqui, ele será inicializado em surfaceCreated
        // que é chamado pelo Android logo após a SurfaceView ser criada.
        getHolder().addCallback(this) // Use getHolder() aqui
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // CORREÇÃO AQUI: Inicialize 'holder' com o valor passado pelo sistema
        this.holder = holder
        startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle changes like screen rotation (for now, just restart game)
        restartGame()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }

    private fun startGame() {
        if (running) return // Essa checagem é importante
        player = Player(context, width, height) // Essas inicializações podem ser pesadas se os bitmaps forem grandes
        background1 = Background(context, width, height)
        background2 = Background(context, width, height)
        background2?.x = width

        score = 0
        isGameOver = false
        obstacles.clear() // Limpar listas
        coins.clear()   // Limpar listas
        lastObstacleSpawnTime = System.currentTimeMillis()
        lastCoinSpawnTime = System.currentTimeMillis()

        running = true
        gameThread = Thread(this) // Nova thread
        gameThread.start()
        gameListener?.onGameStarted()
    }

    private fun stopGame() {
        running = false // Sinaliza para a thread parar
//        try {
//            gameThread.join() // ESPERA a thread terminar. ISSO PODE BLOQUEAR A MAIN THREAD!
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
    }

    fun restartGame() {
        stopGame() // Apenas sinaliza para a thread parar
        // O delay aqui dá um tempo para a thread antiga morrer naturalmente
        // e também permite que a UI atualize antes de iniciar o novo jogo.
        postDelayed({
            // Verifique se a surface ainda é válida antes de iniciar um novo jogo.
            // Isso evita erros se o usuário fechar o app muito rápido.
            if (holder.surface.isValid) {
                startGame()
                gameListener?.onScoreUpdated(0)
                gameListener?.onGameOver(false, 0)
            }
        }, 200) // Aumentei o delay um pouco para dar mais tempo para a thread antiga parar.
    }

    override fun run() {
        var lastFrameTime = System.nanoTime()
        while (running) {
            val currentTime = System.nanoTime()
            val deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0
            lastFrameTime = currentTime

            if (!isGameOver) {
                update(deltaTime)
            }
            draw()
        }
    }

    private fun update(deltaTime: Double) {
        player?.update(deltaTime)

        background1?.update(deltaTime)
        background2?.update(deltaTime)

        background1?.let {
            if (it.x + it.width < 0) {
                it.x = background2?.x!! + background2?.width!!
            }
        }
        background2?.let {
            if (it.x + it.width < 0) {
                it.x = background1?.x!! + background1?.width!!
            }
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastObstacleSpawnTime > spawnIntervalObstacle) {
            obstacles.add(Obstacle(context, width, height))
            lastObstacleSpawnTime = currentTime
        }

        if (currentTime - lastCoinSpawnTime > spawnIntervalCoin) {
            coins.add(Coin(context, width, height))
            lastCoinSpawnTime = currentTime
        }

        val obstaclesIterator = obstacles.iterator()
        while (obstaclesIterator.hasNext()) {
            val obstacle = obstaclesIterator.next()
            obstacle.update(deltaTime)
            if (player?.collidesWith(obstacle) == true) {
                isGameOver = true
                gameListener?.onGameOver(true, score)
                stopGame()
                return
            }
            if (obstacle.x + obstacle.width < 0) {
                obstaclesIterator.remove()
            }
        }

        val coinsIterator = coins.iterator()
        while (coinsIterator.hasNext()) {
            val coin = coinsIterator.next()
            coin.update(deltaTime)
            if (player?.collidesWith(coin) == true) {
                coinsIterator.remove()
                score += 10
                gameListener?.onScoreUpdated(score)
            }
            if (coin.x + coin.width < 0) {
                coinsIterator.remove()
            }
        }
    }

    private fun draw() {
        // Antes de usar holder, verifique se está inicializado.
        // Como ele é lateinit e inicializado em surfaceCreated, isso deve ser seguro
        // uma vez que surfaceCreated já foi chamado.
        if (!holder.surface.isValid) return

        val canvas = holder.lockCanvas() ?: return
        canvas.drawColor(Color.BLUE)

        background1?.draw(canvas)
        background2?.draw(canvas)

        player?.draw(canvas)
        obstacles.forEach { it.draw(canvas) }
        coins.forEach { it.draw(canvas) }

        holder.unlockCanvasAndPost(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isGameOver) return false

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                player?.jump()
            }
        }
        return true
    }
}

// Interface para comunicar eventos do jogo para a Activity
interface GameListener {
    fun onScoreUpdated(newScore: Int)
    fun onGameOver(gameOver: Boolean, finalScore: Int)
    fun onGameStarted()
}