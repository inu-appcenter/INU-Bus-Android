package com.bungabear.inubus.custom

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.text.Html
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bungabear.inubus.R
import com.ms_square.etsyblur.BlurringView


/**
 * Created by Minjae Son on 2018-08-13.
 */

class IconPopUp : ConstraintLayout {

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defStyleAttr : Int) : super(context, attributeSet, defStyleAttr)

    private val mBtnConfirm : Button
    private val mVDivider : View
    private var mContainer : LinearLayout
    private val mTvMessage : TextView
    private val mIvIcon : ImageView
    private val mPopupWindow : PopupWindow
    private var mRefDimView : BlurringView? = null
    private var mDpIvSize : Float
    private var mShowDuration : Long?
    private var mHandler : Handler

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_popup_container, this, false)
        addView(v)
        mBtnConfirm = v.findViewById(R.id.btn_custom_popup_ok)
        mBtnConfirm.setOnClickListener { dismiss() }
        mVDivider = v.findViewById(R.id.custom_popup_btn_divider)
        mContainer = v.findViewById(R.id.ll_custom_popup_container)
        mTvMessage = v.findViewById(R.id.tv_custom_popup_message)
        mIvIcon = v.findViewById(R.id.iv_custom_popup_icon)
        mPopupWindow = PopupWindow(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mDpIvSize = 76f
        mShowDuration = null
        mHandler = Handler()
    }

    fun setBtnVisible(visible : Int) : IconPopUp{
        mBtnConfirm.visibility = visible
        mVDivider.visibility = visibility
        return this
    }

    fun setBtnText(text : String) : IconPopUp{
        mBtnConfirm.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE)
        return this
    }

    fun setMessageText(text : String) : IconPopUp{
        mTvMessage.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE)
        return this
    }

    fun setIcon(drawable : Drawable, isCircle : Boolean) : IconPopUp {
        if(isCircle){
            mIvIcon.scaleType = ImageView.ScaleType.FIT_CENTER
            mIvIcon.setImageDrawable(drawable)
        }
        else {
            mIvIcon.scaleType = ImageView.ScaleType.CENTER
            val bitmap =  (drawable as BitmapDrawable).bitmap
            val resizeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDpIvSize, context.resources.displayMetrics)  / Math.sqrt(2.0)
            val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true);
            val bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resized)
            mIvIcon.setImageDrawable(bitmapDrawable)
        }

        return this
    }

    fun setIcon(res : Int, isCircle : Boolean) : IconPopUp {
        val drawable = ContextCompat.getDrawable(context, res)
        val bitmap =
                if (drawable is BitmapDrawable) {
                    drawable.bitmap;
                }
                else {
                    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                }
        if (isCircle){
            mIvIcon.scaleType = ImageView.ScaleType.FIT_CENTER
            val resizeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDpIvSize, context.resources.displayMetrics)  / 2
            val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true);
            val bitmapDrawable = BitmapDrawable(resources, resized)
            mIvIcon.setImageDrawable(bitmapDrawable)
        }
        else {
            mIvIcon.scaleType = ImageView.ScaleType.CENTER
            val resizeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDpIvSize, context.resources.displayMetrics)  / Math.sqrt(2.0)
            val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true);
            val bitmapDrawable = BitmapDrawable(resources, resized)
            mIvIcon.setImageDrawable(bitmapDrawable)
        }
        return this
    }

    fun setOnConfirmButtonClick(callback : (IconPopUp)->Unit) : IconPopUp{
        mBtnConfirm.setOnClickListener{
            callback(this)
        }
        return this
    }

    fun setDimBlur(blurView : BlurringView) : IconPopUp{
        mRefDimView = blurView
        // blurredView를 미리 바인드하고 호출. 바인드를 나중에하다가 null 상태에서 사라지면 에러남
//        mRefDimView?.blurredView(burredview)
        return this
    }

    fun setShowDuration(duration : Long) : IconPopUp{
        mShowDuration = duration
        return this
    }

    fun show(){
        mPopupWindow.animationStyle = R.style.AppTheme_PopupAnimation
        mPopupWindow.showAtLocation(this, Gravity.CENTER, 0, 0)
        mRefDimView?.alpha = 0f
        mRefDimView?.visibility = View.VISIBLE
        mRefDimView?.animate()?.alpha(1f)?.setDuration(1000)?.start()
        mShowDuration?.let {
            mHandler.postDelayed(Runnable {
                dismiss()
            }, it)
        }
    }

    fun setDismissListener(callback : ()->Unit) : IconPopUp{
        mPopupWindow.setOnDismissListener{
            callback()
        }
        return this
    }

    fun dismiss(){
        mPopupWindow.dismiss()
        mRefDimView
                ?.animate()
                ?.alpha(0f)
                ?.setDuration(1000)
                ?.setListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        mRefDimView?.visibility = View.GONE
                    }
                    override fun onAnimationCancel(animation: Animator?) {
                    }
                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                ?.start()
    }
}