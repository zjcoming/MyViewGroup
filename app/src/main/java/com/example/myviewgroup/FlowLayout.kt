package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlowLayout:ViewGroup {
    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context, attrs){}
    //定义控件之间的间隔
    private val space = 30
    //定义所有的二维数组保存所有的View 方便等会儿layout
    private var allViews:MutableList<MutableList<View>> = mutableListOf()
    //定义数组保存一排的View
    private var everyLineViews = mutableListOf<View>()
    //定义所有行中最长的长度
    private var maxWidth = 0
    //定义一个变量记录已经使用了多少宽度
    var widthUsed = 0
    //定义一个变量记录当前行最大的高度
    var maxHeight = 0
    //定义一个数组保存所有的宽度
    private var totalHeight = 0
    //定义父容器的长度
    private var parentHeight = 0
    //定义父容器的宽度
    private var parentWidth = 0


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //确定子控件的measureSpec
        for(i in 0 until childCount){
            var child = getChildAt(i)
            //得到每个子控件的布局参数
            val lp = child.layoutParams
            //得到子控件的widthSpec
            var childWidthSpec = getChildMeasureSpec(widthMeasureSpec,2*space,lp.width)
            //得到子控件的heightSpec
            var childHeightSpec = getChildMeasureSpec(heightMeasureSpec,2*space,lp.width)
            //测量每个子控件
            child.measure(childWidthSpec,childHeightSpec)
        }

    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
    }
}