// Obstacle.kt
package com.example.coinrunnergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.random.Random

class Obstacle(context: Context, screenWidth: Int, screenHeight: Int) : GameObject {
    override var x: Int = screenWidth // Começa fora da tela, à direita
    override var y: Int
    override var width: Int = 100
    override var height: Int = Random.nextInt(150, 300) // Altura variável
    override var bitmap: Bitmap

    private var speed = 700.0 // Velocidade de movimento (pixels/s)

    init {
        // Posição Y no chão, considerando a altura do obstáculo
        y = screenHeight - height - 100 // 100 pixels acima da base, como o player

        // Carregue a imagem do obstáculo (adicione sua imagem em res/drawable)
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.obstacle_sprite)
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    override fun update(deltaTime: Double) {
        x -= (speed * deltaTime).toInt() // Move para a esquerda
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), null)
    }
}