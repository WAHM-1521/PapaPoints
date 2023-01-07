package com.tinytotsnbites.papapoints

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat

const val TEXT_VERTICAL_OFFSET = 10

class CustomRating(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatRatingBar(context, attrs) {

    private var ratingValue: Int
    private val bounds = Rect()

    init {
        context.theme.obtainStyledAttributes(attrs,
            R.styleable.CustomRating, 0, 0).apply {
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
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val x = width / 2f
        val y = height / 2f
        val text = "$ratingValue"
        paint.getTextBounds(text, 0, text.length, bounds)
        canvas?.drawText(text, x - bounds.exactCenterX(), y - bounds.exactCenterY() - TEXT_VERTICAL_OFFSET, paint)

        setProgressDrawableTint()
    }

    private fun setProgressDrawableTint() {
        if (ratingValue < 0) {
            progressDrawable.setTint(ContextCompat.getColor(context, R.color.customRed))
        } else if (ratingValue > 0) {
            progressDrawable.setTint(ContextCompat.getColor(context, R.color.holoOrangeDark))
        } else {
            progressDrawable.setTint(Color.LTGRAY)
        }
    }

    fun setRatingValueNew(ratingValue: Int) {
        this.ratingValue = ratingValue
        invalidate()
    }
}