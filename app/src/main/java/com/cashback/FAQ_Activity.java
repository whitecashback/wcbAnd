package com.cashback;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by dev on 21/3/17.
 */

public class FAQ_Activity extends Activity
{
    String data=
            "<html>\n" +
                    "    <div>\n" +
                    "<h2>Frequently Asked Questions</h2>\n" +
                    "\n" +
                    "<p style=\"font-size:18px;font-weight:bold\">Cashback related Terms</p>\n" +
                    "\n" +
                    "<p><strong>What Is Cashback ?</strong></p>\n" +
                    "\n" +
                    "<p>WhiteCashback gets commission from the stores/retailers associated with WhiteCashback, as well as the purchase will confirmed, WhiteCashback shared the cashback amount with the users for their purchase. It is very simple, We are getting the paid from the retailers and we keep some commision with us and rest of the amount we release into your account.</p>\n" +
                    "\n" +
                    "<p><strong>Why WhiteCashback Should Pay Cashback To Us ?</strong></p>\n" +
                    "\n" +
                    "<p>WhiteCashback will pay you the cashback because we are affiliate partners of the retailers, retailers pay to us as we are doing advertising for them, that's why they get pay to us, we receive some commission from that and rest we do pay to the user as a cashback.</p>\n" +
                    "\n" +
                    "<p><strong>What Is Extra Cashback ?</strong></p>\n" +
                    "\n" +
                    "<p>The cashback or discount provided by the retailers / stores to the customer directly, It doesn't matter that you should visit our website : www.whitecashback.in or not, you will get that discount or cashback from the retailers but apart from that cashback, we are giving some extra cashback as we are referring you to retailers, so you save on your every purchase some extra. For example : If you are going to purchase an iphone from Amazon.in. You are going to www.amazon.in via www.whitecashback.in, as well as you will redirect to amazon from whitecashback, your id will be track to amazon. Maybe there will be some cashback offer, deals, discount available on Amazon, you will get that and apart from that you will get extra cashback provided by WhiteCashback as you are visiting to Amazon via Whitecashback.</p>\n" +
                    "\n" +
                    "<p><strong>What Is Signup Bonus Or Registration Bonus On WhiteCashback ?</strong></p>\n" +
                    "\n" +
                    "<p>Sign bonus is an autonomous body of WhiteCashback. It doesn't relate to any retailers or stores or any affiliates. Signup bonus or registration bonus provided by WhiteCashback in order to encourage users to get register and achieve their minimum wallet balance to transfer into their bank.</p>\n" +
                    "\n" +
                    "<p><strong>What Is Referral Bonus ?</strong></p>\n" +
                    "\n" +
                    "<p>When you refer a WhiteCashback link to someone, as he will get register and will do any transaction via WhiteCashback, In this case, you will get a referral bonus. You can share WhiteCashback link to n number of people, in order to get referral bonus. For example, if you refer WhiteCashback link to 500 users and 100 will get register and they do a minimum transaction, you will get referral bonus Rs. 5 for each.</p>\n" +
                    "\n" +
                    "<p><strong>Cashback related Queries</strong></p>\n" +
                    "\n" +
                    "<p><strong>How Can I Transfer My Cashback From My WhiteCashback Wallet To My Bank Account?</strong></p>\n" +
                    "\n" +
                    "<p>You can only transfer your confirmed cashback into your bank account. You cannot trasfer your pending cashback. There is a simple way to transfer your real cash, you need to follow the few instruction. Your confirmed cashback should be minimum Rs. 250, only then you can generate the request to transfer it into your bank. First of all, you need to put your payment method, either it is Paypal or wire transfer. For wire transfer you need to fill your bank details, like : Account holder's name, Account Number, Bank Name, IFSC. For Paypal, you just need to fill your Paypal id. After filling this information, you can generate a request to transfer your cashback to your bank.</p>\n" +
                    "\n" +
                    "<p><strong>What Is Pending Cashback ?</strong></p>\n" +
                    "\n" +
                    "<p>As well as you do any transaction via WhiteCashback, within 72 hours, cashback will come into your wallet as a pending cashback. Pending cashback is a cashback which is under process from the retailer, as well as we will get the cashback from the retailers, system will automatically put your cashback as confirmed cashback. In order to confirm the cashback, this process takes minimum 90 days from pending to confirm. Sometimes some transaction may decline due to some tracking reason or some other reasons, In this case your cashback will be rejected.</p>\n" +
                    "\n" +
                    "<br/>\n" +
                    "\n" +
                    "<p><strong>How Long Time My Pending Cashback Will Take To Confirmed Cashback ?</strong></p>\n" +
                    "\n" +
                    "<p>Pending cashback takes 90 days to make it confirm, it is due to return policy, cancellation policy cookiee policy and other things. For example you did buy a product from www.ebay.in via WhiteCashback, your cashback will come as a pending cashback, within one month you return your product as per ebay's return policy, In this case your cashback will get reject from pending to rejected.</p>\n" +
                    "\n" +
                    "<p><strong>Why My Transaction Got Rejected, If It Was Tracked Earlier?</strong></p>\n" +
                    "\n" +
                    "<p>If your transaction got tracked and later on it gets rejected, it may cause many reason. In order to confirm your transaction should not get reject 99%, please follow the following instruction :-</p>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li>Whenever you place any order or do any transaction, make sure you are login to www.whitecashback.in and proper redirecting to retailer from where you want to buy product.</li>\n" +
                    "\t<li>If you are doing transaction from multiple retailers/ stores like flipkart / Amazon /Jabong / eaby etc. for every retailer, you have to redirect via www.whitecashback.in</li>\n" +
                    "\t<li>While you are doing transaction, make sure that there should not be other cashback site open, this may leads to not proper tracking.</li>\n" +
                    "\t<li>Sometimes you can do to return/ cancel some product with any reason, In such type of cases your cashback will get rejected.</li>\n" +
                    "\t<li>Sometimes cashback doesn't track due to google analytics / browser cookies or javascript.</li>\n" +
                    "\t<li>Apart from this, many times retailers don't give the clear reason for the declined / rejected transaction.</li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<p><strong>I think my Cash Back amount is incorrect. What can I do?</strong></p>\n" +
                    "\n" +
                    "<p>You first ensure that you are calculating the correct Cash Back percentage. For example, some stores offer various rates of cash back, such as 2% on mobiles and 5% on Apparels. Also make sure to exclude taxes and shipping charges before calculating the Cash Back percentage.</p>\n" +
                    "\n" +
                    "<p>If still your Cash Back amount is incorrect, please email us at <a href=\"mailto:contact@whitecashback.in\">contact@whitecashback.in</a></p>\n" +
                    "\n" +
                    "<p><strong>How do I receive my Cash Back?</strong></p>\n" +
                    "\n" +
                    "<p>We offer our members two convenient payment methods:</p>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li>PayPal</li>\n" +
                    "\t<li>Bank Transfer</li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<p>The minimum payout is 250 INR.</p>\n" +
                    "\n" +
                    "<p>You can change your preferred payment method by going to your account page and clicking on Payment Details mentioned thereon.</p>\n" +
                    "\n" +
                    "<p><strong>I havn\'t received a payment by now. Why?</strong></p>\n" +
                    "\n" +
                    "<p>Please ensure the following:</p>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li>The payment schedule to ensure you're due a payment.</li>\n" +
                    "\t<li>Your current balance is 250 INR or greater.</li>\n" +
                    "\t<li>Have you given the correct bank details?</li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<br/>\n" +
                    "\n" +
                    "<p><strong>What happens if I return or exchange an item that I purchased?</strong></p>\n" +
                    "\n" +
                    "<p>Please note that- Returning an item will reverse your cash back earnings.</p>\n" +
                    "\n" +
                    "<br/>\n" +
                    "\n" +
                    "<p><strong>Using our Coupons</strong></p>\n" +
                    "\n" +
                    "<p><strong>What Is Coupon / Promocode / Deal ?</strong></p>\n" +
                    "\n" +
                    "<p>Promocode or promotional code is a type of promotional code. You are reduce the actual price of the product or you can get some other benefits like free shipping, some free gifts, buy one get one and many more by using promotional code or promo code only if applicable. You can also get some percentage discount while using promocode given by WhiteCashback, if applicable for the particular product.</p>\n" +
                    "\n" +
                    "<p><strong>How do I use a promo/coupon code?</strong></p>\n" +
                    "\n" +
                    "<p>Simply copy the coupon code and apply it during the checkout process on the retail store\'s website. Applying these coupons will save your money for the purchases you will make for a particular product you are shopping.</p>\n" +
                    "\n" +
                    "<p><strong>Do the coupons work on my mobile device?</strong></p>\n" +
                    "\n" +
                    "<p>Yes, they sure do.</p>\n" +
                    "\n" +
                    "<p><strong>What if a coupon doesn\'t work?</strong></p>\n" +
                    "\n" +
                    "<p>We update our coupons on a regular basis while removing the invalid or expired coupons. If we missed one, please let us know by emailing <a href=\"mailto:contact@whitecashback.in\">contact@whitecashback.in</a> .Thank you!</p>\n" +
                    "\n" +
                    "<br/>\n" +
                    "\n" +
                    "<p><strong>Referral Program Related Queries</strong></p>\n" +
                    "\n" +
                    "<br/>\n" +
                    "\n" +
                    "<p><strong>My WhiteCashback Referral Link Is Not Working, What Should I Do ?</strong></p>\n" +
                    "\n" +
                    "<p>To ensure that the problem you have is not being caused by your own settings, please follow these simple steps: First let confirm what browser you are using, because tracking system based on browser's cookies, so make sure about your browser property. There are three major browser use in India.</p>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li>If you are using Mozilla Firefox, For Enabling Javascript, please go to Tools -&gt; Options -&gt; Content, here JavaScript must be enabled. In order to enable cookies, please go to Tools -&gt; Options -&gt; Privacy. Make sure, here Remember History. Please also make sure, your pop-up windows must not be blocked. Also clear the recent history. Please hit (Ctrl + h) in order to show your history, kindly clear your history.</li>\n" +
                    "\t<li>If you are a Google Chrome user, then you can follow the follwing instruction : In order to enable cookies, Go to Tools-&gt; Options -&gt; Hood -&gt; Cookie Settings. Kindly make sure to Allow all Cookies. For clearing cookies, Tools -&gt; Clear Browsing data. You can also hit (Ctrl +h ) and can clear everything according to you.</li>\n" +
                    "\t<li>If you are Internet Explorer user, kindly do the following : Go to Internet Options -&gt; Security, Make sure Security level is Medium. Make sure your Pop-up blocker must be disabled. For Privacy Settings, Tools -&gt; Internet Options -&gt; Privacy, Please do Security level low. Please delete all browsing history in order to make cookie clear.</li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<p><strong>I have referred one of my friends but I didn\'t received Rs 5 as a bonus. What's going on?</strong></p>\n" +
                    "\n" +
                    "<p>As soon as your friend or relative uses your referral link to sign up at WhiteCashback, you will receive Rs 5 as a sign up bonus. That amount will be shown pending until and unless your referral make any purchase or shop via their WhiteCashback account.</p>\n" +
                    "\n" +
                    "<p><strong>How many friends can I invite? How I can earn more using Referral Scheme?</strong></p>\n" +
                    "\n" +
                    "<p>As many as you like! The more you refer, the more referral bonus will be accumulated in your account. Moreover, for every shopping by your referral using their WhiteCashback account, you will get a commission credited into your WhiteCashback wallet.</p>\n" +
                    "\n" +
                    "<p><strong>Partner with Us!</strong></p>\n" +
                    "\n" +
                    "<p><strong>I represent a merchant, how can we work together?</strong></p>\n" +
                    "\n" +
                    "<p>We'd love to work with you! Email us at <a href=\"mailto:contact@whitecashback.in\">contact@whitecashback.in</a> with all of your details, including which network you partner with as well as if we need to launch a store page for your brand.</p>\n" +
                    "</div>\n" +
                    "</html>\n";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);
        WebView myWebView=(WebView)findViewById(R.id.faq_Webview);
        myWebView.loadData(data, "text/html; charset=UTF-8", null);

        ((LinearLayout)findViewById(R.id.backFromFaq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
