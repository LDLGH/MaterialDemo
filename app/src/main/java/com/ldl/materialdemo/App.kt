package com.ldl.materialdemo

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * @author LDL
 * @date: 2022/9/6
 * @description:
 */
class App :Application(){

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}