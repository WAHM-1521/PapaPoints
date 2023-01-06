package com.tinytotsnbites.papapoints

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet

class CustomRating(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatRatingBar(context, attrs) {

    private var ratingValue: Int
    private val bounds = Rect()
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomRating, 0, 0).apply {
            try {
                ratingValue = getInt(R.styleable.CustomRating_ratingValue, 0)
            } finally {
                recycle()
            }
        }
    }

    private val paint = Paint().apply {
        textSize = 35f
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val x = width / 2f
        val y = height / 2f
        val text = "$ratingValue"
        paint.getTextBounds(text, 0, text.length, bounds)
        canvas?.drawText(text, x - bounds.exactCenterX(), y - bounds.exactCenterY() - 10, paint)

        if (ratingValue < 0) {
            progressDrawable.setTint(Color.RED)
        } else if(ratingValue > 0) {
            progressDrawable.setTint(Color.parseColor("#FFBB86FC"))
        } else {
            progressDrawable.setTint(Color.LTGRAY)
        }
    }

    fun setRatingValueNew(ratingValue: Int) {
        this.ratingValue = ratingValue
        invalidate()
    }
}