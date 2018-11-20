package com.example.zhidachen.mysmartusc_28;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

public class SpinnerTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container,new PreferenceActivity()).addToBackStack(null).commit();
    }

    @Test
    public void testForSpinnerDropdown() {
        onView(withId(R.id.check_area)).perform(click());

        //click just the first option -- this will fail if the spinner dropdown isn't present.
        String[] areasArray = activityTestRule.getActivity().getResources().getStringArray(R.array.areasforcheck);
        onData(is(areasArray[0])).perform(click());
    }

    @Test
    public void testSpinnerData() {
        onView(withId(R.id.check_area)).perform(click());
        String[] areasArray = activityTestRule.getActivity().getResources().getStringArray(R.array.areasforcheck);
        for (String area : areasArray) {
            onData(allOf(is(instanceOf(String.class)), is(area))).perform(click());
            onView(withId(R.id.check_area)).check(matches(withSpinnerText(containsString(area))));

            //un-focus the spinner
            onView(withId(R.id.check_area)).perform(click());
        }
    }

}
