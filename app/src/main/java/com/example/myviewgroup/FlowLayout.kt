package com.example.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty

class FlowLayout:ViewGroup {
    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context, attrs){}
    //定义控件之间的间隔
    private val space = 30
    //定义所有的二维数组保存所有的View 方便等会儿layout
    var allViews: MutableList<MutableList<View>>  = mutableListOf()
    //定义数组保存一排的View
    var everyLineViews: MutableList<View> = mutableListOf()
    //定义一个数组保存每一行的最大高度，方便等会儿布局使用
    private var allLineHeightList = mutableListOf<Int>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 由于onMeasure（）方法的多次调用，多以要把这些变量定义在onMeasure（）中，防止出错
         */

        //定义所有行中最长的长度(相当于父容器的宽带）
        var maxWidth = 0
        //定义父容器的长度
        var parentHeight = 0
        //定义父容器的宽度
        var parentWidth = 0
        //定义一个变量记录当前行最大的高度
        var currentWidth = space
        // 定义一个变量记录已经使用了多少宽度
        var currentHeight = space
        //定义一个变量保存所有的高度（相当于父容器的高度）
        var totalHeight = 0
        //重新初始化（由于变量定义在外部）
        allViews = mutableListOf()
        everyLineViews = mutableListOf()

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //获取父容器的最大宽高
        val parentMaxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentMaxHeight = MeasureSpec.getSize(heightMeasureSpec)//不会用到，但还是写了

        //确定子控件的measureSpec
        for(i in 0 until childCount){
            var child = getChildAt(i)
            //得到每个子控件的布局参数
            val lp = child.layoutParams
            //得到子控件的widthSpec
            var childWidthSpec = getChildMeasureSpec(widthMeasureSpec,2*space,lp.width)
            //得到子控件的heightSpec
            var childHeightSpec = getChildMeasureSpec(heightMeasureSpec,2*space,lp.height)
            //测量每个子控件
            child.measure(childWidthSpec,childHeightSpec)


            /**
             * 流式布局最核心的算法
             */

            //在这一行添加
            if(currentWidth+space+child.measuredWidth <= parentMaxWidth){
                currentWidth+=child.measuredWidth+space
                currentHeight = Math.max(currentHeight,(child.measuredHeight+space))
            }else{    //换到下一行

                //将上一行的添加到保存所有View的数组中
                allViews.add(everyLineViews)
                //重置上每一行的数组  但是不能用清空 ，必须重新分配内存（由于指针问题）
                everyLineViews = mutableListOf()
                //将上一行的高度加到总的高度里面
                totalHeight+=currentHeight
                //将上一行的最大高度存到数组中
                allLineHeightList.add(currentHeight)
                maxWidth = Math.max(maxWidth,currentWidth)
                //进行下一行的初始化
                currentHeight = child.measuredHeight+space
                currentWidth = child.measuredWidth+space
            }
            //添加孩子到数组中
            everyLineViews.add(child)
        }
        //判断是否还有不满一行的控件没有被计算到
        if(everyLineViews.size>0){
            currentWidth = 0
            currentHeight = 0
            for (i in 0 until everyLineViews.size){
                //得到每个子控件
                val child = everyLineViews[i]
                //当前宽度等于余下的每个子控件的宽度加总 再加上 space间隔
                currentWidth += child.measuredWidth+space
                //当前这一行的高度就是这一个控件的高度+space与上一次保存的最大行高相比，谁大取谁
                currentHeight = Math.max(currentHeight,child.measuredHeight+space)
            }
            //余下的这一行的总宽度再与前面的n行的最长的比较，谁大取谁
            maxWidth = Math.max(maxWidth,currentWidth)
            //算出总的高度
            totalHeight += currentHeight
            allLineHeightList.add(currentHeight)
            //将这一行添加到数组中
            allViews.add(everyLineViews)
        }
        //设置父容器的最终尺寸
        parentHeight = totalHeight+space
        parentWidth = maxWidth+space
        setMeasuredDimension(parentWidth,parentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = space
        var top = space
        var right = 0
        var bottom = 0
        for (i in 0 until allViews.size){//得到每一行的控件
            //进入第一行的布局
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