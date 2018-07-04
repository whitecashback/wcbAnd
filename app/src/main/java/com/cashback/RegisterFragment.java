package com.cashback;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.cashback.util.CommonFunctions;
import com.cashback.util.ExecuteServices;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.regex.Pattern;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    AppCompatButton register_btn;
    EditText input_email, input_phone, input_name, input_pass,input_referal;
    View root;
    String stMail, stPass, stPhone, stName,stReferalCode,userId="";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    boolean show_password = false;
    final static int PERMISSIONS_REQUEST_ACCOUNT = 101;
    AppCompatCheckBox mCheckBox;
    public RegisterFragment() {
        // Required empty public constructor
    }

    TextView mTermsCondition;
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
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefEditor=prefs.edit();
        initViews();
        setListeners();

        root.findViewById(R.id.termView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),TermsAndConditionActivity.class));
            }
        });
        return root;
    }


    void implementPermissionModel() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSIONS_REQUEST_ACCOUNT);
        } else {
            setEmailAccount();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCOUNT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                setEmailAccount();
            }
        }
    }

    void initViews() {
        root.findViewById(R.id.btn_fb).setOnClickListener(this);
        root.findViewById(R.id.btn_gplus).setOnClickListener(this);
        input_email = (EditText) root.findViewById(R.id.input_email);
        input_pass = (EditText) root.findViewById(R.id.input_password);
        input_name = (EditText) root.findViewById(R.id.input_name);
        input_phone = (EditText) root.findViewById(R.id.input_phone);
        input_referal=(EditText)root.findViewById(R.id.input_referal_code);
        register_btn=(AppCompatButton)root.findViewById(R.id.btn_register);
         mCheckBox=(AppCompatCheckBox)root.findViewById(R.id.termsChk);

        //register button
        // register_btn.setSupportBackgroundTintList(csl);
    }

    void setListeners() {
        root.findViewById(R.id.toggle_vis).setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
/*    void setListener() {
        root.findViewById(R.id.toggle_vis).setOnClickListener(this);

    }*/

    @Override
    public void onClick(View v) {
        if(!CommonFunctions.isInternetOn(getActivity()))
        {
            MLoginActivity.showMessageDialog("No internet connection...!!!","#fcf8e3","#8a6d3b");
            return;
        }
        switch (v.getId()) {
            case R.id.btn_register:
                Log.e("FCM TOKEN BTN",prefs.getString("FCM_TOKEN","NO_TOKEN"));
                if (validate()) {


                    if(stPass.length()<6)
                    {
                        MLoginActivity.showMessageDialog("Password should be greater then 6 character.","#f2dede","#a94442");
                    return;
                    }
                    if(!mCheckBox.isChecked())
                    {
                        MLoginActivity.showMessageDialog("Please check our Terms & Conditions.","#f2dede","#a94442");
return;
                    }
                    pd.show();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", stMail)
                            .addFormDataPart("phone", stPhone)
                            .addFormDataPart("name", stName)
                            .addFormDataPart("password", stPass)
                            .addFormDataPart("type", "1")
                            .addFormDataPart("reg_id", prefs.getString("FCM_TOKEN","NO_TOKEN"))
                            .addFormDataPart("newsletter", "y")
                            .addFormDataPart("country", "india")
                            .addFormDataPart("ref_id", stReferalCode)
                            .build();
                    new ExecuteServices().executePost(CommonFunctions.mUrl + "userRegister", new ExecuteServices.OnServiceExecute() {
                        @Override
                        public void onServiceExecutedResponse(final String response) {

                       /* if (!Preferences.saveUser(response, getActivity(), Preferences.TYPE_R, null)) {
                            return;
                        }*/
                            Log.e("Response code is 1",""+response);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    MainActivity.registerFlag=1;
                                    parseRegisterJsonData(response);
                                }
                            });
                        }
                        @Override
                        public void onServiceExecutedFailed(String msg) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    MLoginActivity.showMessageDialog("Something went wrong.","#f2dede","#a94442");
                                }
                            });
                        }
                    },requestBody);
                }
                break;
            case R.id.btn_fb:
                MainActivity.registerFlag=1;
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onFBClick();
                break;
            case R.id.btn_gplus:
                MainActivity.registerFlag=1;
                if (mSocialLoginListener != null)
                    mSocialLoginListener.onGoogleClick();
                break;
            case R.id.toggle_vis:
                show_password = !show_password;
                input_pass.setInputType(show_password ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input_pass.setSelection(input_pass.getText().toString().length());
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    ProgressDialog pd;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Wait...");
        pd.setCanceledOnTouchOutside(false);
        implementPermissionModel();
    }

    void setEmailAccount() {
        String gmail = null;
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                gmail = account.name;
                input_email.setText(gmail);
                input_email.setSelection(gmail.length());
                break;
            }
        }
    }

    boolean validate(){
        stMail=input_email.getText().toString().trim();
        stPass=input_pass.getText().toString().trim();
        stName=input_name.getText().toString().trim();
        stPhone=input_phone.getText().toString().trim();
        stReferalCode=input_referal.getText().toString().trim();


        if(TextUtils.isEmpty(stName) ){
            input_name.requestFocus();
            input_name.setError("Field is empty");
            return false;
        }
        if(TextUtils.isEmpty(stPhone) || !Pattern.matches(Patterns.PHONE.pattern(),stPhone)){
            input_phone.requestFocus();
            input_phone.setError("Field is empty");
            return false;
        }
        if(TextUtils.isEmpty(stMail) || !Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),stMail)){
            input_email.requestFocus();
            input_email.setError("Empty/Invalid email");
            return false;
        }
        if(TextUtils.isEmpty(stPass)){
            input_pass.requestFocus();
            input_pass.setError("Field is empty");
            return false;
        }


        return true;
    }


    void parseRegisterJsonData(String response)
    {
        try {

            JSONObject obj = new JSONObject(response);
            String status=obj.getString("status");
            System.out.println(status);
            if(status.equalsIgnoreCase("1"))
            {
                JSONArray arr=obj.getJSONArray("data");
                JSONObject ob=arr.getJSONObject(0);
                System.out.println("idsssssss");
                prefEditor.putString("USER_ID",ob.getString("id")).commit();
                prefEditor.putString("USER_NAME",ob.getString("name")).commit();
                prefEditor.putString("LOGIN_FROM","App").commit();
                userId=ob.getString("id");
                if(MainActivity.sComingFrom.equalsIgnoreCase("MyAccount")||MainActivity.sComingFrom.equalsIgnoreCase("MyFav")||
                        MainActivity.sComingFrom.equalsIgnoreCase("invite")||MainActivity.sComingFrom.equalsIgnoreCase("LikeFav"))
                {
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

                MLoginActivity.showMessageDialog(obj.getString("message"),"#f2dede","#a94442");
            }
        }
        catch (Exception e)
        {

        }
    }
}
