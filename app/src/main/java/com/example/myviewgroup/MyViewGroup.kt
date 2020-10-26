package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

//知道父控件MyViewGroup的大小，根据跟则算出子控件的大小
class MyViewGroup:ViewGroup {
    private var space = 50
    constructor(context: Context,attrs:AttributeSet?):super(context, attrs){}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //得到自己的限制
        var parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        var parentheight = MeasureSpec.getSize(heightMeasureSpec)

        //确定子控件的宽高
        var childWidth =0
        var childHeight =0
        if(childCount ==1){
            var childWidth = parentWidth - 2*space
            var childHeight = parentheight - 2*space
        }else{
            childWidth = (parentWidth -3*space)/2
            //确定第几行第几列
            var row = (childCount+1)/2
            childHeight = (parentheight - (row+1)*space)/row
        }

        //让子控件测量自己，设置宽高
        var childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY)
        var childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY)
        for( i in 0 until childCount){
            getChildAt(i).also {
                it.measure(childWidthSpec,childHeightSpec)
            }
        }
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        for(i in 0 until childCount){
            //确定具体位置row和column
            val row = i/2//这里相当于row=0是代表第一行
            val column =i%2
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