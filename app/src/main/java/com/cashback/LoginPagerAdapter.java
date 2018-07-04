package com.cashback;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jack on 13/09/2016.
 */
public class LoginPagerAdapter extends FragmentStatePagerAdapter {

    Context ctx;
    //integer to count number of tabs
    int tabCount;

    LoginFragment mLoginFragment;
    RegisterFragment mRegisterFragment;


    //Constructor to the class
    public LoginPagerAdapter(FragmentManager fm, int tabCount, Context ctx) {
        super(fm);
        this.ctx = ctx;
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                mLoginFragment=new LoginFragment();
                mLoginFragment.setOnSocialLoginClickListener(new OnSocialLoginClick() {
                    @Override
                    public void onFBClick() {
                        ((MLoginActivity)ctx).onFBClicked(mLoginFragment,0);
                    }

                    @Override
                    public void onGoogleClick() {
                        ((MLoginActivity)ctx).onGClicked(mLoginFragment,1);
                    }
                });
                return mLoginFragment;

            default:
                mRegisterFragment= new RegisterFragment();
                mRegisterFragment.setOnSocialLoginClickListener(new OnSocialLoginClick() {
                    @Override
                    public void onFBClick() {
                        ((MLoginActivity)ctx).onFBClicked(mRegisterFragment,0);
                    }

                    @Override
                    public void onGoogleClick() {
                        ((MLoginActivity)ctx).onGClicked(mRegisterFragment,1);
                    }
                });
                return mRegisterFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";
            default:
                return "Register";

        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}