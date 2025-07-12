// Coin.kt
package com.example.coinrunnergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.random.Random

class Coin(context: Context, screenWidth: Int, screenHeight: Int) : GameObject {
    override var x: Int = screenWidth // Começa fora da tela, à direita
    override var y: Int
    override var width: Int = 70
    override var height: Int = 70
    override var bitmap: Bitmap

    private var speed = 700.0 // Mesma velocidade dos obstáculos para alinhar

    init {
        // Posição Y aleatória para as moedas (pode estar no ar ou no chão)
        val minHeight = screenHeight / 4 // Mínimo 1/4 da tela de cima
        val maxHeight = screenHeight - 200 // Máximo acima do chão
        y = Random.nextInt(minHeight, maxHeight)

        // Carregue a imagem da moeda (adicione sua imagem em res/drawable)
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin_sprite)
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    override fun update(deltaTime: Double) {
        x -= (speed * deltaTime).toInt() // Move para a esquerda
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), null)
    }
}