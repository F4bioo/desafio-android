package com.picpay.desafio.android.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrefsTest : TestCase() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefs: Prefs

    @Before
    public override fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        sharedPreferences =
            context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        prefs = Prefs(sharedPreferences)
    }

    @Test
    fun shouldPassWhenSaveStringPreferences() {
        val myKey = "my_string_key"
        val myString = "my string"
        prefs.setString(myKey, myString)
        assertTrue(prefs.getString(myKey, "") == myString)
    }

    @Test
    fun shouldPassWhenSaveIntPreferences() {
        val myKey = "my_int_key"
        val myInt = 10
        prefs.setInt(myKey, myInt)
        assertTrue(prefs.getInt(myKey, 0) == myInt)
    }

    @Test
    fun shouldPassWhenSaveFloatPreferences() {
        val myKey = "my_float_key"
        val myFloat = 10f
        prefs.setFloat(myKey, myFloat)
        assertTrue(prefs.getFloat(myKey, 0f) == myFloat)
    }

    @Test
    fun shouldPassWhenSaveBooleanPreferences() {
        val myKey = "my_boolean_key"
        val myBoolean = true
        prefs.setBoolean(myKey, myBoolean)
        assertTrue(prefs.getBoolean(myKey, false) == myBoolean)
    }

    @Test
    fun shouldPassWhenRemoveValueFromKeyPreferences() {
        val myKey = "my_int_key"
        val myInt = 10
        prefs.setInt(myKey, myInt)
        prefs.remove(myKey)

        assertFalse(prefs.getInt(myKey, 0) == myInt)
    }

    @Test
    fun shouldPassWhenCleanPreferences() {
        val myKey = "my_int_key"
        val myInt = 10
        prefs.setInt(myKey, myInt)
        prefs.clear()

        assertFalse(prefs.getInt(myKey, 0) == myInt)
    }
}
