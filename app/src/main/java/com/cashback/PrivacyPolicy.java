package com.cashback;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dev on 3/4/17.
 */

public class PrivacyPolicy extends Activity
{

    LinearLayout mBackFromTerm;
    TextView mTermText;
    String data="<div class=\"contents\"> <div id=\"dyn-content\">\n" +
            "\t<div class=\"container\">\n" +
            "<h1><strong>Privacy Policy</strong></h1>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p>Your personal data or information is the data that is related to or is capable of finding or identifying to a living person. We take account holder privacy very seriously and do all we can to protect it.</p>\n" +
            "\n" +
            "<p>The very purpose of this Privacy Policy is to make you aware of the type of information we may gather in order to proceed, how we may use it, whether the information your provide is disclosed to anyone, and other choices that you have regarding the very use of the information provided by you. Words, sentences and phrases used in this Privacy Policy have the same meaning as Account Holder take it in a positive manner in order to initiate a healthy business relation with our customers.</p>\n" +
            "\n" +
            "<p><strong>General Information</strong></p>\n" +
            "\n" +
            "<p>The personal data that you will provide will be collected, processed, stored and used by us and passed to and processed by companies named within our group which are acting under the contract with us. We will do so in order to provide customer support, track your transactions with retailers, process payments and for other purposes as referred to in our Privacy Policy.</p>\n" +
            "\n" +
            "<p><strong>Personal Data</strong></p>\n" +
            "\n" +
            "<p>When you sign up for White Cashback, we ask for your name and email address and other relevant personal information including your choice of cash back method, be it PayPal, a credit card or a bank account. On a related note, we may also seek your IP address to know your location. Other information that you may provide is optional, for example your address, age, occupation, interests, etc.</p>\n" +
            "\n" +
            "<p><strong>Sensitive Data</strong></p>\n" +
            "\n" +
            "<p>In some cases, we may ask you to offer your sensitive information when obtaining or comparing quotations of insurance from a retailer. This sensitive data may be information on your origin, physical or mental condition, criminal allegations (if any), or sentences for convictions. You may provide it or it will be provided only with your consent including as to the retailers with whom it will be shared. We may retain your sensitive information as long as it is permitted to carry our legal proceedings or regulatory, taxation and fraud prevention purposes.</p>\n" +
            "\n" +
            "<p><strong>Cookies Information</strong></p>\n" +
            "\n" +
            "<p>When your web browser access a website such as White Cashback, cookies may be automatically stored your system’s hard drive. These cookies are a type of small text file, depending on your browser settings.</p>\n" +
            "\n" +
            "<p>While using the White Cashback services, cookies will be put and placed both by White Cashback and by the third parties.</p>\n" +
            "\n" +
            "<p>The main functions of the cookies are as following:</p>\n" +
            "\n" +
            "<ol>\n" +
            "\t<li>It tells us the route through which you came to our site.</li>\n" +
            "\t<li>It tracks your transactions. Cookies give us the information whenever you make a purchase on a retailer’s site. It does so to track your transaction with the retailer(s). It basically suggests which retailer’s site have you clicked and when and if you go on to make a transaction.</li>\n" +
            "\t<li>Cookies keep you logged in to your main account for a limited or specific period of time.</li>\n" +
            "\t<li>Cookies allow us to comply with your interests and choices about the display of information in your account.</li>\n" +
            "\t<li>It tracks your use of functionality such as blogging available through White Cashback.</li>\n" +
            "\t<li>It helps us remember the options you select such as who are your favorite merchants.</li>\n" +
            "\t<li>It permits initiation and integration with social media sites (liking White Cashback on Facebook)</li>\n" +
            "\t<li>Cookies provide you with offers based on your use of our site and the transactions you enter into through accessing those specific offers.</li>\n" +
            "</ol>\n" +
            "\n" +
            "<p>Besides, if you visit the White Cashback website, and do not establish an account, cookies allow us to present and advertisement for White Cashback on other sites that you visit. Please note that the advertisement is intended to make you return or rather prompt you to return to the sire in order to make you register as an account holder.</p>\n" +
            "\n" +
            "<p>Please take into consideration that retailers may place cookies on your device whenever you make a transaction with them. These cookies will be scrutinized or governed by the retailer’s own policies. White Cashback will not be responsible for retailer’s acts and omissions.</p>\n" +
            "\n" +
            "<p>White Cashback may include cookies on the site that allows third party advertises to present or prompt you with advertising on the White Cashback site based on your interests which can be detailed our through your web browsing activity and your use of White Cashback service.</p>\n" +
            "\n" +
            "<p>We gather, believe and understand that the cookies you may encounter with on the White Cashback site are either necessary to perform the transactions or are non-intrusive.</p>\n" +
            "\n" +
            "<p>Kindly take into consideration that if you set your browser in a manner that it may reject cookies, you may not be able to use certain services of White Cashback and may not be able to earn Cashback<strong>.</strong></p>\n" +
            "\n" +
            "<p><strong>Sharing on information with retailers and others</strong></p>\n" +
            "\n" +
            "<p>As an account holder with White Cashback, you will make use of your personal data available to other businesses or retailers who work with us. It will happen each time you enter into a transaction with retailer. This may also happen in circumstances wherein you use a price – quote-comparison service.</p>\n" +
            "\n" +
            "<p>Each and every retailer to whom you provide your personal data will have their own practice concerning users’ privacy. These retailers are independent of and from White Cashback in such terms and conditions and it is advisable that you check their privacy terms to ensure you are happy with their services.</p>\n" +
            "\n" +
            "<p><strong>Sharing on information with others</strong></p>\n" +
            "\n" +
            "<p>We wish to understand or account holder and their interests as much as we can in order to cater them with personalized offers through White Cashback services as much as possible. Therefore, it is possible that we may provide or share the data we hold about you with one or more retailers which will match that given data against other information that the hold about you. These information may include age, gender, demographic information, and known interests. We always request retailers to delete your personal data from their systems as soon as the task or transaction is complete. We do so in order to keep your data confidential.</p>\n" +
            "\n" +
            "<p>We use the personal information that the retailers provide to us to target more appropriately and accurately the offers that are made available to you through White Cashback service.</p>\n" +
            "\n" +
            "<p>We would also like to state that the information we gather about our account holders are kept anonymous in order to keep your data and identity inaccessible. We also do so to ensure that it is not used for the internal management purposes or share it with the retailers. The information in discussion includes usage of account holder’s cashback receipt method such as the kind or types of products that the user purchases and the value of those purchases made.</p>\n" +
            "\n" +
            "<p>Besides, White Cashback will also share data only with trusted 3<sup>rd</sup> parties to enable the following:</p>\n" +
            "\n" +
            "<ul>\n" +
            "\t<li>To enable instore program to track your transactions carefully and in a correct manner.</li>\n" +
            "\t<li>To track the qualifying transaction and related referral fees.</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p><strong>Other Terms</strong></p>\n" +
            "\n" +
            "<p>When you register the number of your credit card and/or debit card and/or charge card in the White Cashback service or programme, you hereby give us the authority to disclose your card numbers to businesses or retailers which assist White Cashback by associating with tour account pertaining to certain instore purchase you may make with and to the retailers or their suppliers.</p>\n" +
            "\n" +
            "<p>You ensure and promise to provide that you are entitled to register the card number and receive information about the transactions effecting using such card.</p>\n" +
            "\n" +
            "<ul>\n" +
            "\t<li>You are entitled to register a maximum of 5 card numbers per member’s account. However, this may include cards belonging to other members of your household. They must provide that they have consented to register their cards to assist you to collect the cashback.</li>\n" +
            "\t<li>We may request you to provide written consent at any time. If we are not satisfied that with the information you have entered, we reserve the right to de-register any or all Identifier Card Numbers on your account.</li>\n" +
            "\t<li>You may be notified that each and every card number may be registered at any one time to one service only through which cards are used as identifiers for the purpose of crediting rewards. These rewards may be in form of cashback, points or other kinds of rewards.</li>\n" +
            "\t<li>In case of more than one attempted registration of the same card number, the last registration will only prevail and previous registrations will be de-registered automatically.</li>\n" +
            "\t<li>We may disclose your personal data if we or an entity of ours who is processing your data on our behalf are compelled to do so by law. This shall also happen if we receive a valid or legal complaint requesting for your account or information disclosure.</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p><strong>Security Information</strong></p>\n" +
            "\n" +
            "<p>We take your personal information seriously and do all we can to protect it. All and any information that you provide is stored on our secured servers, access to which is strictly prohibited by any external members. It is understood that no system is completely secured; we take extra measures to reduce the chances of security problems.</p>\n" +
            "\n" +
            "<p>You will understand that the access to your account is password protected and comes under the privacy and security act. Here is the detailed view of the above mentioned statement:</p>\n" +
            "\n" +
            "<ul>\n" +
            "\t<li>To keep your passwords and other information secret.</li>\n" +
            "\t<li>To ensure to only use WhiteCashback while your system is signed in to our site.</li>\n" +
            "\t<li>To ensure that you sign out from your account when not using it.</li>\n" +
            "\t<li>To make sure that only you have the access to the details of your cashback receipt method such as PayPal, Credit Card or Bank account.</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p><strong>Account and Mail Settings</strong></p>\n" +
            "\n" +
            "<p>We would like to stay in touch with our account holders. Therefore after you have signed up we send you information about products or services provided by us or our partners. This information we think will be of your interest.</p>\n" +
            "\n" +
            "<p>We understand that receiving emails in bulk can be annoying that’s why we try to keep it to a minimum level. However, if you feel you are being sent mail in bulk, you may anytime change the settings in your account. We also have a one-click unsubscribe option, and each and every communication that you receive from us gives you the liberty to opt out of receiving such and similar communications in near future. You may also review, edit, change, or correct your identity and other information by signing into your account and heading to the settings option.</p>\n" +
            "\n" +
            "<p>Kindly take into consideration that some communications are an integral part of the White Cashback service and cannot be opted out of. These types of communication takes into account the changes to the White Cashback service and your actual transaction with retailers. These communications ca be brought to an end by de-registering your account.</p>\n" +
            "\n" +
            "<p><strong>Privacy Statement Changes</strong></p>\n" +
            "\n" +
            "<p>This Privacy Policy is correct as of 01/10/2016.<br>\n" +
            "&nbsp;</p>\n" +
            "</div>\n" +
            "</div>\n" +
            " </div>";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);
        mBackFromTerm=(LinearLayout)findViewById(R.id.backFromFaq);
        mBackFromTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTermText=(TextView)findViewById(R.id.term);
        mTermText.setText("Privacy Policy");

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
