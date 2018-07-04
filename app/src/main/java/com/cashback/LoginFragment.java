package com.cashback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Pattern;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment implements View.OnClickListener {
    AppCompatButton login_btn;
    EditText edit_mail, edit_pass;
    String mail, password;
    View root, fb_btn, g_btn;
    TextView mForgetPass,mContinue;
    boolean show_password = false;
    ProgressDialog pd;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    Activity activity;
    public LoginFragment() {
        // Required empty public constructor
    }

    OnSocialLoginClick mSocialLoginListener;

    public void setOnSocialLoginClickListener(OnSocialLoginClick mSocialLoginListener) {
        this.mSocialLoginListener = mSocialLoginListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        initViews();
        if(!CommonFunctions.isInternetOn(getActivity()))
        {
            MLoginActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
        }
        setListener();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Wait...");
        pd.setCanceledOnTouchOutside(false);
    }

    void initViews() {
        edit_mail = (EditText) root.findViewById(R.id.input_contact);
        edit_pass = (EditText) root.findViewById(R.id.input_password);
        mForgetPass=(TextView)root.findViewById(R.id.link_forgot);
        mContinue=(TextView)root.findViewById(R.id.continue_without_login);
        login_btn=(AppCompatButton)root.findViewById(R.id.btn_login);
        //fb ang gplus
        //loginButtonFB = (LoginButton) root.findViewById(R.id.login_fb);
        fb_btn = root.findViewById(R.id.btn_fb);
        g_btn = root.findViewById(R.id.btn_gplus);
        mContinue.setVisibility(View.GONE);
    }

    void setListener() {
        login_btn.setOnClickListener(this);
        fb_btn.setOnClickListener(this);
        g_btn.setOnClickListener(this);
        root.findViewById(R.id.toggle_vis).setOnClickListener(this);
        root.findViewById(R.id.link_forgot).setOnClickListener(this);
        mForgetPass.setOnClickListener(this);
        mContinue.setOnClickListener(this);
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(Activity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }

    @Override
    public void onClick(View v) {
        if(!CommonFunctions.isInternetOn(getActivity()))
        {
            MLoginActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            return;
        }

        switch (v.getId()) {
            case R.id.btn_login:
                if (validate()) {
                    pd.show();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", mail)
                            .addFormDataPart("password", password)
                            .addFormDataPart("reg_id", prefs.getString("FCM_TOKEN","NO_TOKEN"))
                            .build();
                    System.out.println("request body "+requestBody.toString());
                    new ExecuteServices().executePost(CommonFunctions.mUrl+ "userLogin", new ExecuteServices.OnServiceExecute() {
                        @Override
                        public void onServiceExecutedResponse(final String response) {
                            pd.dismiss();
                            System.out.println(response);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status=obj.getString("status");
                                        if(status.equalsIgnoreCase("1"))
                                        {
                                            MainActivity.loginFlag=1;
                                            prefEditor.putString("USER_ID",obj.getString("user_id")).commit();
                                            prefEditor.putString("LOGIN_FROM","App").commit();
                                            prefEditor.putString("USER_NAME",obj.getString("name")).commit();
                                            if(MainActivity.sComingFrom.equalsIgnoreCase("MyAccount")||MainActivity.sComingFrom.equalsIgnoreCase("MyFav")||
                                                    MainActivity.sComingFrom.equalsIgnoreCase("invite")||MainActivity.sComingFrom.equalsIgnoreCase("LikeFav")||MainActivity.sComingFrom.equalsIgnoreCase("RecentStores"))
                                            {
                                                System.out.println("in my account/invite");
                                                getActivity().setResult(Activity.RESULT_OK);
                                                getActivity().finish();

                                            }
                                            else if(MainActivity.sComingFrom.equalsIgnoreCase("HotDeals")||MainActivity.sComingFrom.equalsIgnoreCase("FavFragment")
                                                    ||MainActivity.sComingFrom.equalsIgnoreCase("StoreCoupon"))
                                            {
                                                getActivity().startActivity(new Intent(getActivity(),WebviewActivity.class));
                                                getActivity().finish();
                                            }
                                            else if(MainActivity.sComingFrom.equalsIgnoreCase("FavUnselected"))
                                            {
                                                getActivity().finish();
                                            }
                                        }
                                        else
                                        {
                                            MLoginActivity.showMessageDialog("Invalid Email/Password.","#f2dede","#a94442");
                                        }
                                    }
                                    catch (Exception e)
                                    {

                                    }
                                    // getActivity().finish();
                                }

                            });
                        }
                        @Override
                        public void onServiceExecutedFailed(String msg) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                        }
                    }, requestBody);
                }
                else
                {
                    System.out.println("some issue");
                }
                break;
            case R.id.btn_fb:
                MainActivity.loginFlag=1;
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onFBClick();
                break;
            case R.id.link_forgot:
                showForgetDialog();
                break;
            case R.id.btn_gplus:
                MainActivity.loginFlag=1;
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onGoogleClick();
                break;
            case R.id.toggle_vis:
                show_password = !show_password;
                //     VectorDrawableCompat vc = VectorDrawableCompat.create(getResources(), show_password ? R.drawable.ic_visibility_off_black_24dp : R.drawable.ic_visibility_black_24dp, getActivity().getTheme());
                // DrawableCompat.setTint(vc, ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                //edit_pass.setCompoundDrawablesWithIntrinsicBounds(null, null, vc, null);
                //   ((AppCompatImageView) root.findViewById(R.id.toggle_vis)).setImageDrawable(vc);
                edit_pass.setInputType(show_password ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_pass.setSelection(edit_pass.getText().toString().length());
                break;

            case R.id.continue_without_login:
                getActivity().finish();
                break;

        }
    }
    void showForgetDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.forget_password_alert, null);
        final TextInputLayout contLay, mailLay;
        final EditText cont, mail;
        contLay = (TextInputLayout) dialoglayout.findViewById(R.id.input_mob_lay);
        mailLay = (TextInputLayout) dialoglayout.findViewById(R.id.input_mail_lay);
        cont = (EditText) dialoglayout.findViewById(R.id.mob);
        mail = (EditText) dialoglayout.findViewById(R.id.mail);

        final AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = alertDialogBuilder3.setView(dialoglayout).create();
        dialog.setCancelable(false);
        dialog.show();

        dialoglayout.findViewById(R.id.No).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialoglayout.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View focus = cont;
                boolean valid = true;
                // View focus= contactinput;
                String cont_ = cont.getText().toString();
                String mail_ = mail.getText().toString();
                contLay.setError(null);
                mailLay.setError(null);
                if (mail_.isEmpty() || !Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), mail_)) {
                    focus = mail;
                    mailLay.setError("Enter valid email address");
                    valid = false;
                }

                if (!valid) {
                    focus.requestFocus();
                } else {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", mail.getText().toString())
                            .build();
                    new ExecuteServices().executePost(CommonFunctions.mUrl + "forgotpassword", new ExecuteServices.OnServiceExecute() {
                        @Override
                        public void onServiceExecutedResponse(final String response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject obj=new JSONObject(response);
                                        String status=obj.getString("status");
                                        if(status.equalsIgnoreCase("1"))
                                        {
                                            MLoginActivity.showMessageDialog("Please check your email.","#dff0d8","#3c763d");
                                        }
                                        else
                                        {
                                            MLoginActivity.showMessageDialog("User not exist.","#f2dede","#a94442");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }

                        @Override
                        public void onServiceExecutedFailed(String msg) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MLoginActivity.showMessageDialog("Something went wromg.","#f2dede","#a94442");
                                }
                            });

                        }
                    },requestBody);
                    dialog.cancel();
                }
            }
        });
    }

    boolean validate() {
        mail = edit_mail.getText().toString().trim();
        password = edit_pass.getText().toString().trim();
        if (TextUtils.isEmpty(mail) || (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), mail) && !Pattern.matches(Patterns.PHONE.pattern(), mail))) {
            edit_mail.requestFocus();
            MLoginActivity.showMessageDialog("Email field is empty/not valid.","#f2dede","#a94442");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            edit_pass.requestFocus();
            MLoginActivity.showMessageDialog("Password field is empty.","#f2dede","#a94442");

            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }




}
