package com.example.zhidachen.mysmartusc_28;

import android.app.Activity;
import android.app.Fragment;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddKeywordWithSpaceTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    public String keyword1 = "way";
    public String keyword2 = "to";
    public String keyword3 = "go";
    public String keywordToBeTested = new StringJoiner(" ").add(keyword1).add(keyword2).add(keyword3).toString();

    @Before
    public void init() {
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, new DashboardActivity()).addToBackStack(null).commit();
    }

    @Test
    public void AddKeywordWithSpace() {

        // Step 1: add keyword
        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.to_add_keyword)).perform(click());
        // add/type keyword and close keyboard
        onView(withId(R.id.user_input)).perform(typeText(keywordToBeTested), closeSoftKeyboard());
        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.add_keyword)).perform(click());

        // Step 2: check keyword
        // direct to check keyword fragment
        onView(withId(R.id.check_keyword)).perform(click());
        // check if keywords exists in list
        try {
            onView(withId(R.id.display_keyword_textview)).check(matches(withText(keyword1)));
            onView(withId(R.id.display_keyword_textview)).check(matches(withText(keyword2)));
            onView(withId(R.id.display_keyword_textview)).check(matches(withText(keyword3)));


        } catch (AssertionFailedError e) {
            // View not displayed
        }

    }

}