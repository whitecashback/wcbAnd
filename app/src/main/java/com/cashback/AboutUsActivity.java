package com.cashback;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dev on 21/3/17.
 */

public class AboutUsActivity extends Activity
{
    LinearLayout mBackFromTerm;
    TextView mTermText;
    String data="<div class=\"container\">\n" +
            "             <h1>Whitecashback offers best online deals in India</h1>\n" +
            "            <div class=\"row innerPage innertxt\" id=\"dyn-content\">\n" +
            "                <p>WhiteCashback is an exclusive shopping court to provide users a comfortable shopping experience. It is a single stop destination for all your shopping needs, with our wide range of collections. Another add-on advantage is that you can save while shopping. Here you get the best online deals in India. Besides, you get fantastic online shopping deals here. Moreover, you get the best of discount offers and promo codes.</p>\n" +
            "                <p>Yes, you heard it right!! The shoppers get rewarded for each purchase they made via us. If you are looking for amazing online shopping deals, this is the place to be. Whether it is shopping for your favorite brands or anything particular that you like, you tend to focus more deals that give you greater rewards. Moreover, bargaining is the power that you’ve got through which you make silent transactions. Our deals are the best online deals in India. Furthermore, this kind of shopping is convenient for the users as it saves big on time and money. Here you get fantastic discount offers and promo codes to vouch for that makes it easy to shop for your favorites. </p>\n" +
            "                <p>For all the online shopping deals in India, our primary goal is to improve the user's shopping experience by providing countless ways to save and collect rebates while they shop with us. We give users many options like promo codes and discount offers so that they can choose a favorite store among the long list, and can choose their desired product while comparing their prices. At last the customer comes out shopping with a nice pick of product at best rates. This is as good as it gets. With convenient shopping, you get the best online deals on India. When users need to shop according to their needs, they tend to take shopping seriously.</p>\n" +
            "                 <p>We are regarded as one of the best for online shopping deals and highest paying cashback site, prompts you to open an account and go through the wide range of retailers. If you wish to purchase anything online, you need not go to the website of retailer instead you should visit us to get best cashback online offers &amp; other money saving deals over the purchase. Now that you have seemingly entered the best online deals in India mode, it is in the area of accolades, however, that we show a certain sense of nobility when it’s all about great purchases. Just search the retailer right at the India’s No. 1 Cashback site and proceed with your online shopping offers. As soon as your order gets confirmed, we will transfer your cash back in your White Cashback wallet. This way you save much while shopping! So, what else are you looking for? Login and grab amazing cash back offers, vouchers, and cashback offers.</p>\n" +
            "                <p>WhiteCashback strives to provide a smart &amp; efficient online shopping experience to its customers by providing extra cashback on their each online shopping deals made via us. Using our website, an online shopper can save extra money on the best online deals in India, over and above the existing discounts e-commerce merchants provide. We have over 500 popular retailers listed on our website. We ensure updating daily discount offers, promo codes, coupons &amp; cashback provided by each of our listed retailers thus easing the shopping experience of our customers. We pay you a cash reward when you click through us for financial products as well. The accumulated cash can be withdrawn either by Bank Transfer or PayPal once it reaches the threshold amount of Rs. 250. WhiteCashback has thousands of satisfied customers all across India and considered as one of the best sites to provide huge cashback. Join our list of Happy Customers for an amazing shopping experience.</p>\n" +
            "                <p>WhiteCashback prompts you to open an account and go through the wide range of retailers to shop from which gives you a plethora of opportunities for online shopping deals. If you wish to purchase anything in future, you need not go to the website of retailer instead you should visit WhiteCashback to win some cashback &amp; other deals over the purchase. Just search the retailer right at the WhiteCashback site and proceed with your shopping as you get the best online deals in India. As soon as your order gets confirmed, we will transfer your cashback right on your WC wallet. In this way we allow you to earn as well as save while shopping! This is one of the easiest ways to grab incredible offers, deals, and discount.</p>\n" +
            "                <h2>Brands associated with us:</h2>\n" +
            "                <p>WhiteCashback connects with all your favorite retailers/stores/brands so that we bring you a vast collection of products with best online deals in India. We are partnered with the brands like Paytm, Freecharge, Ebay, Amazon, Snapdeal, Flipkart, Jabong, Homeshop18, Naaptol, Makemytrip, Lenskart, Bigrock, Bharat Matrimonial, Jeevansaathi.com, Shaadi.com and much more. We update and highlight the best deals, offers, cash back and discount offers and promo codes that are provided by the retailers for our user benefits.</p>\n" +
            "                 <p>We endeavor to give brilliant and proficient internet online shopping deals knowledge to our clients by giving additional cashback on their every shopping made by means of us. Utilizing our site for the best online deals in India, an online customer can spare additional cash, far beyond the current rebates web based business traders give. We have more than 500 prevalent retailers recorded on our site which gives you amazing online shopping deals. We guarantee overhauling day by day offers, discount offers and cashback gave by each of our recorded retailers subsequently facilitating the shopping knowledge of our clients. Promo codes are altogether another venture where you get what you like in the best of prices.</p>\n" +
            "                <p>WhiteCashback strives to provide a smart &amp; efficient online shopping experience to its customers by providing extra cashback on their each online shopping deals made via us. Using our website, an online shopper can save extra money on the best online deals in India, over and above the existing discounts e-commerce merchants provide. We have over 500 popular retailers listed on our website. We ensure updating daily discount offers, promo codes, coupons &amp; cashback provided by each of our listed retailers thus easing the shopping experience of our customers. We pay you a cash reward when you click through us for financial products as well. The accumulated cash can be withdrawn either by Bank Transfer or PayPal once it reaches the threshold amount of Rs. 250. WhiteCashback has thousands of satisfied customers all across India and considered as one of the best sites to provide huge cashback. Join our list of Happy Customers for an amazing shopping experience.</p>\n" +
            "                <p>WhiteCashback prompts you to open an account and go through the wide range of retailers to shop from which gives you a plethora of opportunities for online shopping deals. If you wish to purchase anything in future, you need not go to the website of retailer instead you should visit WhiteCashback to win some cashback &amp; other deals over the purchase. Just search the retailer right at the WhiteCashback site and proceed with your shopping as you get the best online deals in India. As soon as your order gets confirmed, we will transfer your cashback right on your WC wallet. In this way we allow you to earn as well as save while shopping! This is one of the easiest ways to grab incredible offers, deals, and discount.</p>\n" +
            "                <h2>What do you get from us? </h2>\n" +
            "                \t<ul>\n" +
            "\t\t\t\t\t\t<li>Customers can join /connect with us at free of cost.</li>\n" +
            "\t\t\t\t\t\t<li>When the customers Sign Up with us, they get Rs. 10.00 as a bonus.</li>\n" +
            "\t\t\t\t\t\t<li>For each referral, the customers can earn Rs. 5.00.</li>\n" +
            "\t\t\t\t\t\t<li>Shop online at your favorite store in a single stop.</li>\n" +
            "\t\t\t\t\t\t<li>Customers can earn up to 35 % Cashback, Offers , Coupon codes for the purchase.</li>\n" +
            "\t\t\t\t\t\t<li>Customers can save more and earn more with us.</li>\n" +
            "                \t</ul>\n" +
            "                \t<h2>Why we Stand alone?</h2>\n" +
            "                \t<p>WhiteCashback is unique since it is not just a cashback site. We provide you numerous benefits when they shop via whitecashback.in. Check out add -on profits that we give to our valuable users. </p>\n" +
            "                \t<ul>\n" +
            "                \t\t<li>WhiteCashback enables incremental savings for the users.</li>\n" +
            "\t\t\t\t\t\t<li>The users get lucrative offers.</li>\n" +
            "\t\t\t\t\t\t<li>Exclusive cash back for each purchase.</li>\n" +
            "\t\t\t\t\t\t<li>Discounts / Coupons provided by retailers.</li>\n" +
            "\t\t\t\t\t\t<li>WhiteCashback ensures that the users get best online coupons, deals, stores easily with less effort.</li>\n" +
            "\t\t\t\t\t\t<li>Users can find thousands of money saving promo codes with us.</li>\n" +
            "\t\t\t\t\t\t<li>Our registration process is straight forward.</li>\n" +
            "\t\t\t\t\t\t<li>Users get a sign-up bonus right away when they register with us.</li>\n" +
            "\t\t\t\t\t\t<li>Users can refer their dear and near ones, friends, relatives, colleagues and receive referral bonus as well. </li>\n" +
            "\t\t\t\t\t\t<li>We let you connect with around 500 inland and global stores/brands/retailers.</li>\n" +
            "\t\t\t\t\t\t<li>WhiteCashback helps the users to compare and choose from their favorite retailer and can benefit with maximum cash back with their shopping.</li>\n" +
            "                \t</ul>\n" +
            "            </div> <!-- innerPage -->\n" +
            "        </div>";

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
        mTermText.setText("About Us");

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
