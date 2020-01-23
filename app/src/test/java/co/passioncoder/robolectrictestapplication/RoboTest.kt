package co.passioncoder.robolectrictestapplication

import android.content.Intent
import android.widget.Button
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import android.os.Bundle
import android.content.ComponentName
import org.robolectric.shadows.ShadowActivity
import android.app.Activity
import android.widget.TextView
import org.robolectric.android.controller.ActivityController
import org.hamcrest.Matchers.equalTo


@RunWith(RobolectricTestRunner::class)
class RoboTest {

    private lateinit var mainActivity: MainActivity

    @Throws(Exception::class)
    @Before
    fun setup() {
        mainActivity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()
    }

    @Test
    @Throws(Exception::class)
    fun testForNull() {
        Assert.assertNotNull(mainActivity)
    }

    @Test
    @Throws(Exception::class)
    fun testForNavigate() {
        mainActivity.navigate()
    }

    @Test
    fun testActivity() {
        val expectedIntent = Intent(mainActivity, NotMainActivity::class.java)
        mainActivity.findViewById<Button>(R.id.btnClick).callOnClick()
        val shadowActivity = Shadows.shadowOf(mainActivity)
        val actualIntent = shadowActivity.nextStartedActivity
        Assert.assertTrue(expectedIntent.filterEquals(actualIntent))
    }

    @Test
    fun testPackageName() {
        val activity = Robolectric.buildActivity(NotMainActivity::class.java).get()
        Assert.assertNull(activity.callingActivity)
    }

    @Test
    @Throws(Exception::class)
    fun testAppName() {
        val activity = Robolectric.buildActivity(NotMainActivity::class.java).get()
        val hello = activity.resources.getString(R.string.app_name)
        Assert.assertThat(hello, equalTo("Robolectric Test Application"))
    }

    @Test
    fun testActivityTextView() {
        val activity = Robolectric.buildActivity(NotMainActivity::class.java).get()
        val tv2 = activity.findViewById<TextView>(R.id.tv2)
        Assert.assertThat(
            tv2.text.toString(),
            equalTo(activity.resources.getString(R.string.app_name))
        )
    }

    @Test
    @Throws(Exception::class)
    fun testButtonClickNewActivity() {
        val button = mainActivity.findViewById(R.id.btnClick) as Button
        button.performClick()
        val intent = Shadows.shadowOf(mainActivity).peekNextStartedActivity()
        Assert.assertEquals(
            NotMainActivity::class.java.canonicalName,
            intent.component!!.className
        )
    }

}