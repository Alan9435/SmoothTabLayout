package com.example.smoothtablayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.tabs.TabLayout
import java.lang.reflect.Field

class SmoothTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    /** tab count*/
    private var dividerFactor = 2

    /** tab minWidth(for reflection)*/
    private val scrollableTabMinWidth = "scrollableTabMinWidth"

    /** tab maxWidth (for reflection)*/
    private val requestedTabMaxWidth = "requestedTabMaxWidth"

    init {
        initTabMinWidth()
    }

    /** setting Tab count*/
    fun setDividerFactor(divCount: Int) {
        dividerFactor = divCount
        updateTabLayout()
    }

    /** update TabLayout*/
    private fun updateTabLayout() {
        removeAllTabs() // clear tab
        initTabMinWidth() //init TabLayout
    }

    private fun initTabMinWidth() {
        /** tab minWidth = screen width/tab count */
        val tabMinWidth = resources.displayMetrics.widthPixels / dividerFactor

        try {
            /** 利用反射修改private的變數...*/
            val minWidthField: Field = TabLayout::class.java.getDeclaredField(scrollableTabMinWidth)
            minWidthField.apply {
                isAccessible = true //set to variable
                set(this, tabMinWidth) // Set the variable scrollableTabMinWidth of the current instance (this) to tabMinWidth
            }

            //default : give maxWidth according to dpi
            //so we need to setting requestedTabMaxWidth cover it
            val maxWidthField: Field = TabLayout::class.java.getDeclaredField(requestedTabMaxWidth)
            maxWidthField.apply {
                isAccessible = true
                set(this, resources.displayMetrics.widthPixels)
            }
        } catch (e: Exception) {
            Log.e("SmoothTabLayout", "error: $e" )
        }
    }
}