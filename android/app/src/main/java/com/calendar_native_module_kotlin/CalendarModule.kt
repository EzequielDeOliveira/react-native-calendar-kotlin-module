package com.calendar_native_module_kotlin

import android.content.Intent
import android.provider.CalendarContract
import android.provider.CalendarContract.Events.*
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class CalendarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val context: ReactApplicationContext = reactContext;

    override fun getName(): String {
        return "CalendarModule"
    }

    @ReactMethod
    fun createCalendarEvent(name: String, location: String) {
        Log.d("CalendarModuleKotlin", "Create event called with name: " + name
                + " and location: " + location);
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CONTENT_URI)
            .putExtra(TITLE, "Kotlin")
            .putExtra(EVENT_LOCATION, "Remote")
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis() + (60 * 60 * 1000))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }
}