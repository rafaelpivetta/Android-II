package com.exercicioiesb.casa.exerciciofinal

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class DetalheImagemNoticiaView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var paint = Paint()
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.img2)
        val matrix = Matrix()

        var escalaX : Float = width.toFloat() / bmp.width.toFloat()
        var escalaY : Float = height.toFloat() / bmp.height.toFloat()

        matrix.postScale(escalaX, escalaY)
        val dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        canvas.drawColor(Color.WHITE)

        canvas.drawBitmap(dstbmp, 0f, 0f, paint)

    }
}