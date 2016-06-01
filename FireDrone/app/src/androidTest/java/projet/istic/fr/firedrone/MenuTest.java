package projet.istic.fr.firedrone;


import android.content.ComponentName;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationMenuView;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.ImageButton;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import projet.istic.fr.firedrone.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.core.deps.guava.base.CharMatcher.is;
import static android.support.test.espresso.core.deps.guava.base.Predicates.instanceOf;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivity = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void loginToMain2(){
        Intents.init();//Initializes Intents and begins recording intents.

        Espresso.onView(ViewMatchers.withId(R.id.loginField)).perform(typeText("tanguy"),closeSoftKeyboard());//test account


        Espresso.onView(ViewMatchers.withId(R.id.passField)).perform(typeText("tanguy"),closeSoftKeyboard());//test account



        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());


        try {
            Thread.sleep(4444);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));


        onView(withId(R.id.drawer_layout)).perform(open());

        try {
            Thread.sleep(4444);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        //Espresso.onData(Matchers.allOf((Iterable<Matcher<? super Object>>) instanceOf(NavigationMenuView.class))).perform()

        //onData( Matchers.allOf( instanceOf( NavDrawerItem.class), navDrawerItemHavingName( rowContents))).perform( click());
        onView(withId(R.id.nav_logout))
                .perform(click());

    }


}