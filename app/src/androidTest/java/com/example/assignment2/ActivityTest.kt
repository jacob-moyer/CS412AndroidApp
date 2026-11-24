package com.example.assignment2

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityTest {

    private lateinit var device: UiDevice
    private val appPackage = "com.example.assignment2"  // <-- your applicationId
    private val appName = "Assignment2"                  // <-- label under the launcher icon
    private val timeout = 5000L

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), timeout)
    }

    @Test
    fun startActivityExplicitly_showsChallengeInSecondActivity() {
        // 1. Launch the app by clicking its launcher icon
        var icon = device.findObject(By.text(appName))
        if (icon == null) {
            // Icon not on home screen: open the app drawer and look there
            device.swipe(
                device.displayWidth / 2, device.displayHeight - 100,
                device.displayWidth / 2, device.displayHeight / 4, 20
            )
            icon = device.wait(Until.findObject(By.text(appName)), timeout)
        }
        if (icon != null) {
            icon.click()
        } else {
            // Fallback so the test still runs if the icon can't be found
            val context = ApplicationProvider.getApplicationContext<Context>()
            val intent = context.packageManager.getLaunchIntentForPackage(appPackage)!!
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
        device.wait(Until.hasObject(By.pkg(appPackage).depth(0)), timeout)

        // 2. Navigate to the second activity
        val button = device.wait(Until.findObject(By.text("Explicit")), timeout)
        assertNotNull("Start Activity Explicitly button not found", button)
        button.click()

        // 3. Verify a mobile SE challenge is displayed (use text from YOUR app)
        val challenge = device.wait(
            Until.findObject(By.textContains("1) Support for older client versions must be maintained")), timeout
        )
        assertNotNull("Expected challenge text not found in second activity", challenge)
    }
}