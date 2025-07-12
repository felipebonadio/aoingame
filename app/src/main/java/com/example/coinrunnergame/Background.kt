// Background.kt
package com.example.coinrunnergame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Background(context: Context, screenWidth: Int, screenHeight: Int) : GameObject {
    override var x: Int = 0
    override var y: Int = 0
    override var width: Int = screenWidth
    override var height: Int = screenHeight
    override var bitmap: Bitmap

    private var speed = 200.0 // Velocidade de rolagem do fundo (pixels/s)

    init {
        // Carregue a imagem do fundo (adicione sua imagem em res/drawable)
        // Certifique-se de que a imagem do fundo seja grande o suficiente para preencher a tela
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.game_background)
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    override fun update(deltaTime: Double) {
        x -= (speed * deltaTime).toInt() // Move para a esquerda
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), null)
    }
}