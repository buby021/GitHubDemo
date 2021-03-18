package com.vidovicbranimir.githubdemo

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.vidovicbranimir.githubdemo.data.UserPreferences
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UserPreferencesTest : TestCase() {

    private lateinit var userPreferences: UserPreferences

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
    }

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        userPreferences = UserPreferences(context)
        runBlocking {
            userPreferences.saveString(USERNAME, "testUser")
            userPreferences.saveString(PASSWORD, "testPassword")
        }
    }

    @Test
    fun testPreferences() {
        var username = ""
        var password = ""
        runBlocking {
            username = userPreferences.getString(USERNAME).first()
            password = userPreferences.getString(PASSWORD).first()
        }
        assertThat("testUser" == username).isTrue()
        assertThat("testPassword" == password).isTrue()
    }

    @Test
    fun testCaseSensitive() {
        var username = ""
        var password = ""
        runBlocking {
            username = userPreferences.getString(USERNAME).first()
            password = userPreferences.getString(PASSWORD).first()
        }
        assertThat("testuser" == username).isFalse()
        assertThat("TESTPASSWORD" == password).isFalse()
    }
}