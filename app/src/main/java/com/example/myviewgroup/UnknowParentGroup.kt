package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * 不知道父控件的大小但是知道子控件的大小根据子控件的大小算出子控件的大小
 */
class UnknowParentGroup:ViewGroup {
    var space = 50
    constructor(context: Context, attrs: AttributeSet?):super(context, attrs){}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //定义父控件的宽高
        var parentHeight = 0
        var parentWidth = 0
        //var tempWidth = 0
        //确定多少行 多少列

        var row = (childCount-1)/2 //row = 0代表第一行
        //测量父控件的宽度
        var child = getChildAt(0)
        if(childCount ==1){
            parentWidth = 2*space+child.measuredWidth
        }else {
            parentWidth = 3*space+ 2*child.measuredWidth
        }
        //父容器高度
        parentHeight = space + (row+1)*(space+child.measuredHeight)

        //确定子控件的尺寸，虽然xml中给出类但是还是要测量子控件大小，相当于确定（设置）大小，xml中只是给出大小
        for(i in 0 until childCount) {
            getChildAt(i).also {
                measureChild(it, widthMeasureSpec, heightMeasureSpec)
            }
        }
        //设置父控件的大小
        setMeasuredDimension(parentWidth,parentHeight)
    }
    //进行子控件的布局
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        for(i in 0 until childCount){
            var column = i%2
            var row = i/2
            getChildAt(i).also {
                left = space+column*(it.measuredWidth+space)
                top = space+row*(space+it.measuredHeight)
                right = left+it.measuredWidth
                bottom = top+it.measuredHeight
                it.layout(left,top,right,bottom )
            }
        }

    }

    }
