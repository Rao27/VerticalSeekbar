package com.rao27.verticalseekbar.ui_elements

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar

class VerticalSeekbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : androidx.appcompat.widget.AppCompatSeekBar(context,attrs,defStyle) {

    private var onChangeListener: SeekBar.OnSeekBarChangeListener? = null

    private var lastProgress = 0

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(height, width, oldHeight, oldWidth)
    }

    @Synchronized override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        progress = 50
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progress = 0
    }
    override fun onDraw(c: Canvas) {
        // To display the seekbar vertically
        c.rotate(-90f)
        c.translate((-height).toFloat(), 0f)
        super.onDraw(c)
    }

    override fun setOnSeekBarChangeListener(onChangeListener: SeekBar.OnSeekBarChangeListener) {
        this.onChangeListener = onChangeListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onChangeListener?.onStartTrackingTouch(this)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_MOVE -> {
                super.onTouchEvent(event)
                var progress = max - (max * event.y / height).toInt()

                // Ensure progress stays within boundaries
                if (progress < 0) {
                    progress = 0
                }
                if (progress > max) {
                    progress = max
                }
                setProgress(progress)  // Draw progress
                if (progress != lastProgress) {
                    // Only enact listener if the progress has actually changed
                    lastProgress = progress
                    onChangeListener?.onProgressChanged(this, progress, true)
                }

                onSizeChanged(width, height, 0, 0)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_UP -> {
                onChangeListener?.onStopTrackingTouch(this)
                isPressed = false
                isSelected = false
            }
            MotionEvent.ACTION_CANCEL -> {
                super.onTouchEvent(event)
                isPressed = false
                isSelected = false
            }
        }
        return true
    }

    // To set custom progress
    @Synchronized
    fun setCustomProgress(progress: Int, fromUser: Boolean = true) {
        setProgress(progress)
        onSizeChanged(width, height, 0, 0)
        if (progress != lastProgress) {
            // Only enact listener if the progress has actually changed
            lastProgress = progress
            onChangeListener?.onProgressChanged(this, progress, fromUser)
        }
    }

    fun updateThumb(){
        onSizeChanged(height, width, 0, 0)
    }
}