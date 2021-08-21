package com.lyj.pinstagram.view.splash


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class ViewRecordTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule(order = 2)
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Before fun setUp(){
        hiltAndroidRule.inject()
    }

    @Test
    fun viewRecordTest() {
        val textView = onView(
            allOf(
                withId(R.id.navigation_bar_item_large_label_view), withText("홈"),
                withParent(
                    allOf(
                        withId(R.id.navigation_bar_item_labels_group),
                        withParent(allOf(withId(R.id.main_home_item), withContentDescription("홈")))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("홈")))

        val textView2 = onView(
            allOf(
                withId(R.id.navigation_bar_item_small_label_view), withText("지도"),
                withParent(
                    allOf(
                        withId(R.id.navigation_bar_item_labels_group),
                        withParent(allOf(withId(R.id.main_map_item), withContentDescription("지도")))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("지도")))

        val textView3 = onView(
            allOf(
                withId(R.id.navigation_bar_item_small_label_view), withText("톡톡"),
                withParent(
                    allOf(
                        withId(R.id.navigation_bar_item_labels_group),
                        withParent(allOf(withId(R.id.amin_talk_item), withContentDescription("톡톡")))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("톡톡")))

        val imageView = onView(
            allOf(
                withId(R.id.navigation_bar_item_icon_view),
                withParent(
                    allOf(
                        withId(R.id.main_home_item), withContentDescription("홈"),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withId(R.id.navigation_bar_item_icon_view),
                withParent(
                    allOf(
                        withId(R.id.main_map_item), withContentDescription("지도"),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))

        val imageView3 = onView(
            allOf(
                withId(R.id.navigation_bar_item_icon_view),
                withParent(
                    allOf(
                        withId(R.id.amin_talk_item), withContentDescription("톡톡"),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageView3.check(matches(isDisplayed()))

        val imageButton = onView(
            allOf(
                withId(R.id.mainBtnFloating),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))


        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.amin_talk_item), withContentDescription("톡톡"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainBottomNavigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.main_home_item), withContentDescription("홈"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainBottomNavigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.mainBtnFloating),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.mainBtnAuth),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mainToolbar),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
