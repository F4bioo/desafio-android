package com.picpay.desafio.android.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class PrefsTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("provideTestPrefs")
    lateinit var prefs: Prefs

    @Before
    fun setup() {
        hiltRule.inject()
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
