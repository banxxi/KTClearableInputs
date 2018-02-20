package com.banxxi.clearableinputs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by banxxi on 12/23/2017. GG
 */
class ClearableEditText : AppCompatEditText {

    private var iconDrawable: Drawable? = null
    private var iconTint: Int? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ClearableInputs)
        val iconId = a.getResourceId(
                R.styleable.ClearableInputs_clearIcon,
                R.drawable.ic_clear)
        val iconColor = a.getColor(
                R.styleable.ClearableInputs_iconTint,
                Color.parseColor("#888888"))
        setIconDrawable(iconId)
        setIconTint(iconColor)
        a.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text!!.isEmpty() && compoundDrawables[2] != null) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else if (text.isNotEmpty() && compoundDrawables[2] == null) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, iconDrawable, null)
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(focused && text.isNotEmpty())
            setCompoundDrawablesWithIntrinsicBounds(null, null, iconDrawable, null)
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val drawableRight = compoundDrawables[2]
        if (event.action == MotionEvent.ACTION_UP && drawableRight != null) {
            val bounds = drawableRight.bounds
            val x = event.x.toInt()
            val y = event.y.toInt()
            if (x >= this.right - bounds.width() - 50 && x <= this.right - this.paddingRight + 50
                    && y >= this.paddingTop - 50 && y <= this.height - this.paddingBottom + 50) {
                this.text.clear()
                event.action = MotionEvent.ACTION_CANCEL
            }
        }
        return super.onTouchEvent(event)
    }

    fun getIconDrawable(): Drawable? = iconDrawable

    fun setIconDrawable(@DrawableRes iconId: Int) {
        iconDrawable = ResourcesCompat.getDrawable(context!!.resources, iconId, null)
    }

    fun getIconTint(): Int? = iconTint

    fun setIconTint(@ColorInt iconColor: Int) {
        this.iconTint = iconColor
        DrawableCompat.setTint(iconDrawable!!, iconColor)
    }
}