package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var text = ""
    var customButColor = Color.parseColor("#07C2AA")
    var path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
        textAlign = Paint.Align.CENTER
    }


    // Todo use Delegates
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    // TODO use ValueAnimator
    private var progress = 0
    var valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000).apply {
        addUpdateListener {
            progress = it.animatedValue as Int
            invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                progress = 0
                path.reset()
            }
        })
        interpolator = LinearInterpolator()
        repeatCount = 0
        repeatMode = ValueAnimator.RESTART
    }


    init {
        // get error when use withStyledAttributes tell me to use lower java level 8
        // so i go to the doc [https://developer.android.com/develop/ui/views/layout/custom-views/create-view#applyattr]
        // and find this code
//        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
//            text = getString(R.styleable.LoadingButton_text).toString()
//            customButColor = getColor(R.styleable.LoadingButton_color, Color.parseColor("#07C2AA"))
//        }
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0).apply {


                text = getString(R.styleable.LoadingButton_text).toString()
                customButColor = getColor(R.styleable.LoadingButton_color, Color.parseColor("#07C2AA"))

        }
    }


    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(customButColor)
        paint.color = Color.BLUE
        paint.textSize = 60f
        canvas?.drawRect(
            0f,
            0f,
            progress.toFloat() * (widthSize.toFloat() / 365f),
            heightSize.toFloat(),
            paint
        )
        paint.color = Color.YELLOW

        path.addArc(
            widthSize.toFloat() / 2 + 170,
            heightSize.toFloat() / 2 - 20,
            widthSize.toFloat() / 2 + 50f + 170,
            heightSize.toFloat() / 2 + 50f - 20,
            0f,
            progress.toFloat()
        );
        canvas?.drawPath(path, paint)
//        canvas?.drawCircle(widthSize.toFloat() / 2 + 170, heightSize.toFloat() / 2-20,20f,paint)
        paint.color = Color.WHITE
        canvas?.drawText(text, widthSize.toFloat() / 2, heightSize.toFloat() / 2, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}