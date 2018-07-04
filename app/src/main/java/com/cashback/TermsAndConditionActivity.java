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

public class TermsAndConditionActivity extends Activity
{

LinearLayout mBackFromTerm;
    TextView mTermText;
    String data="<div id=\"dyn-content\">\n" +
            "\t<div class=\"container\">\n" +
            "<h1><strong>Terms and Conditions</strong></h1>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>WhiteCashBack Services</strong></p>\n" +
            "\n" +
            "<p>WhiteCashback receives a commission known as a referral fee payable by a retailer for purchasing made by an account holder from the retailer. It is done once the retailer has confirmed the purchase.</p>\n" +
            "\n" +
            "<p>The term retailer means the sellers and suppliers of goods or services that agree to enter into transaction and pay the referral fee to WhiteCashback.</p>\n" +
            "\n" +
            "<p>All the account holders who have entered into the transaction for which the referral fee has been received will be able to see reflected positive balance on their Account. This balance then will be converted into a payment to the account holder in the form of cashback when the account holder requests payment of balance in to the account.</p>\n" +
            "\n" +
            "<p>WhiteCashback service allows account holders to make purchases from retailers which can qualify for cashback.</p>\n" +
            "\n" +
            "<p>Please note that the balance itself should not be represented as a sum of money by WhiteCashback on an account holder’s behalf.</p>\n" +
            "\n" +
            "<p>WhiteCashback is required by the regulatory body to provide each and every account holder who is a user of WhiteCashback services with our Privacy Policy. We insist that you take time to read the document carefully and retain for future references.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Registration</strong></p>\n" +
            "\n" +
            "<p>The account holder must be at least 18 years of age to obtain an account. An account holder may have no more than one account with WhiteCashback. To register with an account, you must be a resident of India.</p>\n" +
            "\n" +
            "<p>You will offer and provide accurate and current information about yourself to register with WhiteCashback. This information may include your name, address and any other relevant details.</p>\n" +
            "\n" +
            "<p>Our Privacy Policy contains important information on how we use your personal data and information you provide with the WhiteCashback account including information derived from the activity through your Cashback Receipt Method.</p>\n" +
            "\n" +
            "<p>You shall furnish details like bank account, Paypal account, credit card and other account into which you wish to receive payments.</p>\n" +
            "\n" +
            "<p>You will ensure that the email address we hold for you are up to date and that you are having full access to it. We say it o in order to send you important messages.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Cashback</strong></p>\n" +
            "\n" +
            "<p>Once an account holder has successfully completed a transaction, and once we have received the Referral fee for that transaction, we pass the related cashback on to the account of the holder through his / her cashback receipt method. It is done when the account holder request payment of the cashback.</p>\n" +
            "\n" +
            "<p>Kindly take into consideration that there are some cases an instances in which a transaction with a retailer may not constitute a transaction, and cashback will not result from that transaction.</p>\n" +
            "\n" +
            "<p>There are many circumstances in which the sum will not appear in an account holder’s balance account, and will be forfeited back to us. Such instances can be:</p>\n" +
            "\n" +
            "<ul>\n" +
            "\t<li>When a referral fee is received by us but not attributed to a transaction or associated with an account.</li>\n" +
            "\t<li>When a purchased product is returned or right to cancel the transaction is being exercised.</li>\n" +
            "\t<li>Balance which is attributed to an account holder or account has been associated with any fraudulent activity. The purchase is made on behalf of, or for the benefit of any other person.</li>\n" +
            "\t<li>Balance to an account which has been inactive for more than twelve months</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Intellectual Property</strong></p>\n" +
            "\n" +
            "<p>You accept that all copyrights, trademarks and intellectual property rights in and relating to the website are owned and licensed to us. This may include the material which is contributed by users or stores. Copying of any material is a punishable offence and is illegal.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Privacy policy</strong></p>\n" +
            "\n" +
            "<p>Our Privacy Policy sets out details how we use your personal information and related matters.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Our Role</strong></p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p>WhiteCashback is not a party to any transaction with retailers, and is not the seller or supplier of any good or services that they make it available for the account holder.</p>\n" +
            "\n" +
            "<p>Besides, we do not have any control over or responsibility for the safety, legality or quality of products or services made available from and through the retailers.</p>\n" +
            "\n" +
            "<p>Whether the retailer shall supply and pass good title to any good and services.</p>\n" +
            "\n" +
            "<p>Account holders are made cautious enough to exercise no lesser degree or precaution while entering into transactions with retailers than they would do a similar transaction anywhere.</p>\n" +
            "\n" +
            "<p>As much as the law permits, you release us, our employees and agents from all liability arising due to any transaction with retailers including claims and demands relating to transactions with retailers, or goods or services offered for sale or supply, or actually sold.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Misuse</strong></p>\n" +
            "\n" +
            "<p>We reserve the right to suspend or terminate any account holder’s account to the WhiteCashback services, if in our general view the relevant Account Holder or Account seems to be in breach of any provision of the agreement.</p>\n" +
            "\n" +
            "<p>The account holder should use the Account only for purchases on the Account Holder's own behalf, and not on the behalf of, or for the benefit of, any other person(s).</p>\n" +
            "\n" +
            "<p>Account Holders should not enter into, any transaction with a retailer or to endeavor to gain Cashback either by providing personal information of someone else, or a payment method which they are not entitled to use.</p>\n" +
            "\n" +
            "<p>Also an account holder must not deceptively or unfairly exploit a retailer's offering.</p>\n" +
            "\n" +
            "<p>It is the obligation of each Account Holder's to make sure that any material posted by the account holder or associated with his/her Account is not offensive, not intended to cause any inconvenience to any person or should not contain any computer virus designed to interfere the normal operation function of the computer.</p>\n" +
            "\n" +
            "<p>You should not advertise any goods or services either.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Contact from third parties</strong></p>\n" +
            "\n" +
            "<p>If anyone contacts WhiteCashback in relation to transactions or materials associated with your Account, then you agree to provide all relevant information we may require and to respond and communicate in prompt and accurate manner.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Additional Services</strong></p>\n" +
            "\n" +
            "<p>WhiteCashback or its partners may offer additional services through WhiteCashback time to time. Your use of those services may be limited or subjected to additional terms and conditions, which you must comply with provided that these terms shall be notified to you time to time in an accurate manner. Any failure to comply with such material provision may end in the termination of the agreement.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Operation of the WhiteCashback Service</strong></p>\n" +
            "\n" +
            "<p>WhiteCashback reserves the right to modify, withdraw or suspend aspects of the WhiteCashback service, or the entirety of it. It may have legal, security or technical as well as commercial reasons for the modification and withdrawal. We will provide you with 30 days advance notice before taking such action. These actions shall be taken earlier if it is for security reasons and in case of technical difficulties experienced by WhiteCashback on the internet. We take extra measure to ensure that the site is accessible to you at any given point of time but since World Wide Web functions in its own way, there may be times when the WhiteCashback services will not be available or it becomes inaccessible to visit the site. However, we will use reasonable measures to overcome such difficulties and try that you may not find it inconvenient to access WhiteCashback and its services.</p>\n" +
            "\n" +
            "<p>For security reasons, we may ask you to change password or other information that facilitates access to WhiteCashback. We will never ask you for password via telephone, email or any other means other than the <a href=\"http://www.whitecashback.in/\">www.whitecashback.in</a> website. You are advised to maintain confidentiality of your password and any additional identifying information.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Indemnity</strong></p>\n" +
            "\n" +
            "<p>You will be wholly responsible for all claims, liabilities and expenses that may arise in connection with any breach of this Agreement by you or through your Account, or any transaction with a retailer.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Assignment</strong></p>\n" +
            "\n" +
            "<p>WhiteCashback reserves the right to assign this agreement or subcontract any or all of its rights and obligations under this agreement. You may not exercise your rights without the written consent of WhiteCashback assign or dispose of this agreement.</p>\n" +
            "\n" +
            "<p>&nbsp;</p>\n" +
            "\n" +
            "<p><strong>Entire Agreement</strong></p>\n" +
            "\n" +
            "<p>This agreement intends to contain your entire agreement with us relating to the WhiteCashback services, if we find it to fair and relevant. It may replace all earlier agreements and understandings with you relating to the WhiteCashback service except in case of any fraud representation made by either of us.&nbsp;</p>\n" +
            "</div>\n" +
            "</div>";
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
        mTermText.setText("Terms & Condition");

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
