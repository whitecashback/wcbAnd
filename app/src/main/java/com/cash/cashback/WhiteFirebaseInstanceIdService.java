package com.cash.cashback;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by dev on 29/5/17.
 */

public class WhiteFirebaseInstanceIdService extends FirebaseInstanceIdService{


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
