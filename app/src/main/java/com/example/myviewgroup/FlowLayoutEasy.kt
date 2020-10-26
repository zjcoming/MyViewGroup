package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class FlowLayoutEasy:ViewGroup {
    private var space = 30
    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context, attrs){}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测量(设置）子控件的大小
        var child = getChildAt(0)
        var lp = child.layoutParams
        var childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,space*2,lp.width)
        var childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,2*space,lp.height)
        child.measure(childWidthMeasureSpec,childHeightMeasureSpec)
        //measureChild(child,widthMeasureSpec,heightMeasureSpec)
        //父容器的最终尺寸
        var parentWidth = 0
        var parentHeight = 0

        var parentWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        var parentWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        var parentHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        var parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        //根据父容器的模式来 设置父容器的实际宽高
        when(parentWidthMode){
            MeasureSpec.EXACTLY -> parentWidth = parentWidthSize
            MeasureSpec.AT_MOST -> parentWidth = child.measuredWidth+2*space
        }
        when(parentHeightMode){
            MeasureSpec.EXACTLY -> parentHeight = parentHeightSize
            MeasureSpec.AT_MOST -> parentHeight = child.measuredHeight+2*space
        }

        setMeasuredDimension(parentWidth,parentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var child = getChildAt(0)
        var left = space
        var top = space
        var right = child.measuredWidth +space
        var bottom = child.measuredHeight +space
        child.layout(left,top,right,bottom )

    }
}