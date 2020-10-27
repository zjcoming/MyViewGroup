package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

class FlowLayout:ViewGroup {
    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context, attrs){}
    //定义控件之间的间隔
    private val space = 10
    //定义所有的二维数组保存所有的View 方便等会儿layout
    private var allViews:MutableList<MutableList<View>> = mutableListOf()
    //定义数组保存一排的View
    private var everyLineViews = mutableListOf<View>()

    //定义一个数组保存每一行的最大高度，方便等会儿布局使用
    private var allLineHeightList = mutableListOf<Int>()

    //定义一个变量保存所有的高度（相当于父容器的高度）
    private var totalHeight = 0
    //定义所有行中最长的长度(相当于父容器的宽带）
    private var maxWidth = 0

    //定义父容器的长度
    private var parentHeight = 0
    //定义父容器的宽度
    private var parentWidth = 0
    //定义一个变量记录当前行最大的高度
    private var currentWidth = space
    // 定义一个变量记录已经使用了多少宽度
    private var currentHeight = space
    var i = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        allViews = mutableListOf()
        everyLineViews = mutableListOf()

 //       Log.v("zj","onMeasure执行了：${++i}次")


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取父容器的最大宽高
        val parentMaxWidth = MeasureSpec.getSize(widthMeasureSpec)
//        Log.v("zj","父容器最大宽度：${parentMaxWidth}")
        val parentMaxHeight = MeasureSpec.getSize(heightMeasureSpec)
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
            //添加孩子到数组中
            everyLineViews.add(child)
            //在这一行添加
            if(currentWidth+space+child.measuredWidth <= parentMaxWidth){
                currentWidth+=child.measuredWidth+space
                currentHeight = Math.max(currentHeight,(child.measuredHeight+space))

            }else{    //换到下一行
                //将上一行的添加到保存所有View的数组中
                allViews.add(everyLineViews)
                //重置上每一行的数组  但是不能用清空 ，必须重新分配内存
                everyLineViews = mutableListOf()
                //将上一行的高度加到总的高度里面
                totalHeight+=currentHeight
                //讲上一行的最大高度存到数组中
                allLineHeightList.add(currentHeight)
                maxWidth = Math.max(maxWidth,currentWidth)

                //进行下一行的初始化
                currentHeight = child.measuredHeight+space
                currentWidth = child.measuredWidth+space

            }
        }
        //判断是否还有不满一行的控件没有被测量到
        if(everyLineViews.size>0){
            currentWidth = 0
            currentHeight = 0
            for (i in 0 until everyLineViews.size){
                Log.v("za","${everyLineViews.size}")      /////
                val child = getChildAt(i)
                currentWidth += child.measuredWidth+space
                currentHeight = Math.max(currentHeight,child.measuredHeight+space)
            }
            maxWidth = Math.max(maxWidth,currentWidth)
            allLineHeightList.add(currentHeight)
        }
        parentHeight = totalHeight
        parentWidth = maxWidth
        setMeasuredDimension(parentWidth,parentHeight)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = space
        var top = space
        var right = 0
        var bottom = 0
//        Log.v("zj","子控件的个数：${childCount}")
//        Log.v(("zj"),"添加所有视图有几行：${allViews.size}")
        for (i in 0 until allViews.size){//得到每一行的控件
//            Log.v("zj","第${i+1}行控件个数：${allViews[i].size}")
            for(j in 0 until allViews[i].size){//得到每一行中的每个控件
                val child = allViews[i][j]

                right = left+child.measuredWidth
                bottom = top + child.measuredHeight
                child.layout(left,top,right,bottom)
                left+=child.measuredWidth+space
            }
            //计算下一行的相关位置
            top += allLineHeightList[i]
            left = space
        }

    }
}