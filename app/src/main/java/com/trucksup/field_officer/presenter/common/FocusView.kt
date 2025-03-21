package com.trucksup.field_officer.presenter.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class FocusView : View {
    private var mTransparentPaint: Paint? = null
    private var mSemiBlackPaint: Paint? = null
    private val mPath: Path = Path()

    constructor(context: Context?) : super(context) {
        initPaints()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaints()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaints()
    }

    private fun initPaints() {
        mTransparentPaint = Paint()
        mTransparentPaint?.setColor(Color.TRANSPARENT)
        mTransparentPaint?.setStrokeWidth(10f)

        mSemiBlackPaint = Paint()
        mSemiBlackPaint?.setColor(Color.TRANSPARENT)
        mSemiBlackPaint?.setStrokeWidth(10f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.reset()

        mPath.addCircle(
            (canvas.getWidth() / 2).toFloat(),
            (canvas.getHeight() / 2).toFloat(), 450f, Path.Direction.CW
        )
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD)

        canvas.drawCircle(
            (canvas.getWidth() / 2).toFloat(),
            (canvas.getHeight() / 2).toFloat(), 450f, mTransparentPaint!!
        )

        canvas.drawPath(mPath, mSemiBlackPaint!!)
        canvas.clipPath(mPath)
        canvas.drawColor(Color.parseColor("#A6000000"))
    }
}