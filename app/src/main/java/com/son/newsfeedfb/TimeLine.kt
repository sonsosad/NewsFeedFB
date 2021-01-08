package com.son.newsfeedfb

import android.content.Context
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.core.graphics.minus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.*
import com.son.newsfeedfb.Adapter.ListPostAdapter
import com.son.newsfeedfb.Model.Comment

class TimeLine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        if (savedInstanceState == null) {
            val fragment = PostFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fg_TimeLine, fragment)
                .commit()
        }

    }

//    fun showPopUp(list: ArrayList<Comment>,view : View){
//        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        var inflateview : View = layoutInflater.inflate(R.layout.cmt_popup_layout,null,false)
//        var display: Display = windowManager.defaultDisplay
//        var size : Point = Point()
//        display.getSize(size)
//
//        popupWindow = PopupWindow(inflateview, size.x - 50,size.y - 400,true)
//        popupWindow.setBackgroundDrawable(getDrawable(R.drawable.fb_popup_bg))
//        popupWindow.isFocusable = true
//        popupWindow.isOutsideTouchable = true
//        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,100)
//    }
//
//    override fun onClickItem(list: ArrayList<Comment>) {
//
//
//    }
}