// Player.kt - CORRIGIDO
package com.example.coinrunnergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Player(context: Context, screenWidth: Int, private val screenHeight: Int) : GameObject { // screenHeight como propriedade
    override var x: Int = screenWidth / 8
    override var y: Int = screenHeight / 2 // Posição inicial no meio
    override var width: Int = 150
    override var height: Int = 150
    override var bitmap: Bitmap

    private var gravity = 2000.0 // Gravidade (pixels/s^2)
    private var velocityY = 0.0 // Velocidade vertical (pixels/s)
    private var jumpStrength = -1000.0 // Força do pulo (negativo para subir)
    private var isJumping = false // Adicione esta variável

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player_sprite)
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        y = screenHeight - height - 100 // 100 pixels acima da base
    }

    override fun update(deltaTime: Double) {
        velocityY += gravity * deltaTime
        y += (velocityY * deltaTime).toInt()

        if (y + height > screenHeight) { // Usar screenHeight passado
            y = screenHeight - height // Coloca no chão
            velocityY = 0.0 // Zera velocidade
            isJumping = false // Reseta o estado de pulo
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), null)
    }

    fun jump() {
        if (!isJumping) { // Impede pulos múltiplos no ar
            velocityY = jumpStrength
            isJumping = true
        }
    }
}