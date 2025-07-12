// GameObject.kt
package com.example.coinrunnergame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

interface GameObject {
    var x: Int
    var y: Int
    var width: Int
    var height: Int
    var bitmap: Bitmap
    val rect: Rect
        get() = Rect(x, y, x + width, y + height)

    fun update(deltaTime: Double)
    fun draw(canvas: Canvas)
    fun collidesWith(other: GameObject): Boolean {
        return Rect.intersects(this.rect, other.rect)
    }
}