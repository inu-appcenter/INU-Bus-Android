package com.inu.bus.custom

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.inu.bus.R
import com.ms_square.etsyblur.BlurringView
import kotlinx.android.synthetic.main.custom_popup_container.view.*
import java.lang.ref.WeakReference


/**
 * Created by Minjae Son on 2018-08-13.
 */

class IconPopUp : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)
    companion object {
        var mInstance : WeakReference<IconPopUp>? = null
    }
    private val mBtnConfirm: Button
    private val mVDivider: View
    private var mContainer: LinearLayout
    private val mTvMessage: TextView
    private val mIvIcon: ImageView
    private val mPopupWindow: PopupWindow
    private var mRefDimView: BlurringView? = null
    private var mDpIvSize: Float
    private var mShowDuration: Long?
    private var mHandler: Handler
    private var mWindow: Window? = null
    private val mAnimationDuration = 1000L
    private val mPopRoot: ConstraintLayout
    private val mTvTitle: TextView
    private val mIvBackground : ImageView
    private var mDismissCallback : (()->Unit)? = null

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
        mPopRoot = popup_root
        mTvTitle = tv_custom_popup_title_on_icon
        mIvBackground = iv_background_image
        mInstance = WeakReference(this)

        mContainer.setOnTouchListener { v, event ->
            true
        }
    }

    // 확인버튼 보이는지
    fun setBtnVisible(visible: Int): IconPopUp {
        mBtnConfirm.visibility = visible
        mVDivider.visibility = visible
        return this
    }

    // 확인버튼 텍스트
    fun setBtnText(text: String): IconPopUp {
        mBtnConfirm.setText(makeSpanText(text), TextView.BufferType.SPANNABLE)
        return this
    }

    // 메세지
    fun setMessageText(text: String): IconPopUp {
        mTvMessage.setText(makeSpanText(text), TextView.BufferType.SPANNABLE)
        return this
    }

    // 텍스트뷰에 html 스타일 적용
    @Suppress("DEPRECATION")
    private fun makeSpanText(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(text);
        }
    }

    // 상단 아이콘 설정
    fun setIcon(drawable: Drawable, isCircle: Boolean, makeCircleFit: Boolean = false): IconPopUp {
        val bitmap = getBitmapFromDrawable(drawable)

        mIvIcon.setImageDrawable(
                if (isCircle) {
                    mIvIcon.scaleType = ImageView.ScaleType.FIT_CENTER
                    val resizeWidth = dp2px(mDpIvSize) / 2
                    val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true)
                    BitmapDrawable(resources, resized)
                } else {
                    mIvIcon.scaleType = ImageView.ScaleType.CENTER
                    if (makeCircleFit) {
                        val resizeWidth = dp2px(mDpIvSize)
                        val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true)
                        val bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resized)
                        bitmapDrawable.isCircular = true
                        bitmapDrawable
                    } else {
                        val resizeWidth = dp2px(mDpIvSize) / Math.sqrt(2.0)
                        val resized = Bitmap.createScaledBitmap(bitmap, resizeWidth.toInt(), resizeWidth.toInt(), true)
                        RoundedBitmapDrawableFactory.create(resources, resized)
                    }
                }
        )
        return this
    }

    fun setIcon(res: Int, isCircle: Boolean, makeCircleFit: Boolean = false): IconPopUp {
        val drawable = ContextCompat.getDrawable(context, res)
        drawable?.let {
            setIcon(drawable, isCircle, makeCircleFit)
        }
        return this
    }

    fun setIconBackgroundColor(color: Int): IconPopUp {
        setIcon( ColorDrawable(color), false, true)
        return this
    }

    fun setIconText(text: String): IconPopUp {
        mTvTitle.setText(makeSpanText(text), TextView.BufferType.SPANNABLE)
        return this
    }

    fun setIconTextColor(color: Int): IconPopUp {
        mTvTitle.setTextColor(color)
        return this
    }

    fun setMessageBackground(res: Int, isRound: Boolean, makeRoundFit: Boolean = false): IconPopUp {
        val drawable = ContextCompat.getDrawable(context, res)
        drawable?.let {
            setImageBackground(drawable, isRound, makeRoundFit)
        }
        return this
    }

    fun setImageBackground(drawable: Drawable, isRound: Boolean, makeRoundFit: Boolean = false): IconPopUp {
        val bitmap = getBitmapFromDrawable(drawable)
        val width = dp2px(300f).toInt()
        val height = (bitmap.height/bitmap.width*width).toInt()
        val resized = Bitmap.createScaledBitmap(bitmap, width, height, true)
        val output = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect =  Rect (0, 0, width, height)
        val rectF =  RectF(rect)
        val roundPx = dp2px(25f)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode (PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(resized, rect, rect, paint)
        mContainer.visibility = View.GONE
        mIvBackground.background = BitmapDrawable(resources, output)
        mIvBackground.setOnClickListener { true }
        return this
    }


    fun setIconSizeDp(dp: Float): IconPopUp {
        mDpIvSize = dp
        val lp = mIvIcon.layoutParams
        lp.width = dp2px(mDpIvSize).toInt()
        lp.height = dp2px(mDpIvSize).toInt()
        return this
    }

    // 확인버튼 콜백
    fun setOnConfirmButtonClickListener(callback: (IconPopUp) -> Unit): IconPopUp {
        mBtnConfirm.setOnClickListener {
            callback(this)
        }
        return this
    }

    // 블러처리하는 뷰
    fun setDimBlur(blurView: BlurringView): IconPopUp {
        mRefDimView = blurView
        // blurredView를 미리 바인드하고 호출. 바인드를 나중에하다가 null 상태에서 사라지면 에러남
        return this
    }

    // show 애니메이션 시간
    fun setShowDuration(duration: Long): IconPopUp {
        mShowDuration = duration
        return this
    }

    // 팝업 보여주기
    fun show() {
        mPopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.animationStyle = R.style.AppTheme_PopupAnimation
        mPopupWindow.showAtLocation(this, Gravity.CENTER, 0, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateStatusBarColor(500,
                    ContextCompat.getColor(this.context, R.color.colorPrimary),
                    ContextCompat.getColor(this.context, R.color.colorPrimaryDim))
        }

        mRefDimView?.alpha = 0f
        mRefDimView?.visibility = View.VISIBLE
        mRefDimView
                ?.animate()
                ?.alpha(1f)
                ?.setDuration(500)
                ?.setListener(emptyAnimationListener)
                ?.start()
        mShowDuration?.let {
            mHandler.postDelayed({
                dismiss()
            }, it)
        }
    }

    // 팝업 사라질때 호출
    fun setDismissListener(callback: () -> Unit): IconPopUp {
        mDismissCallback = callback
        return this
    }

    fun setDismissOnBackgroundTouch(enable: Boolean): IconPopUp {
        if (enable) {
            mPopRoot.setOnClickListener { dismiss() }
        }
        return this
    }

    fun dismiss() {
        if(mPopupWindow.isShowing){
            mPopupWindow.dismiss()
        }
        mDismissCallback?.invoke()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateStatusBarColor(500,
                    ContextCompat.getColor(this.context, R.color.colorPrimaryDim),
                    ContextCompat.getColor(this.context, R.color.colorPrimary))
        }
        mRefDimView
                ?.animate()
                ?.alpha(0f)
                ?.setDuration(500)
                ?.setListener(dismissAnimationListener)
                ?.start()
    }

    // 상단바 색상을 맞추기 위해 window를 받아옴
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setWindow(window: Window) {
        mWindow = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(window.context, R.color.colorPrimaryDim)
    }

    // 상단바 색상 변화 애니메이션
    private fun animateStatusBarColor(duration: Long, from: Int, to: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var blended: Int
            var position: Float
            val anim = ValueAnimator.ofFloat(0f, 1f)
            anim.addUpdateListener { animation ->
                position = animation.animatedFraction
                blended = blendColors(from, to, position)

                mWindow!!.statusBarColor = blended

            }
            anim.setDuration(duration).start()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("test", "$keyCode ${event?.action}")
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mPopupWindow.isShowing){
                dismiss()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun blendColors(from: Int, to: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
        val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
        val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val radius: Int =
                    if (drawable.intrinsicWidth > 0)
                        drawable.intrinsicWidth
                    else
                        (dp2px(mDpIvSize) / 2).toInt()

            val bitmap = Bitmap.createBitmap(radius,
                    radius, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
    private val emptyAnimationListener =
            object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            }

    private val dismissAnimationListener = object : Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator?) {
            mRefDimView?.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWindow?.statusBarColor = ContextCompat.getColor(mWindow?.context!!, R.color.colorPrimary)
            }
            mInstance = null
        }
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
    }
}