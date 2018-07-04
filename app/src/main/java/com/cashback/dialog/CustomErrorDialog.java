package com.cashback.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cashback.R;

/**
 * Created by dev on 8/5/17.
 */

public class CustomErrorDialog extends AlertDialog.Builder {

    public CustomErrorDialog(Context context, String title, String message, Color textColor,Color backgroundColor) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewDialog = inflater.inflate(R.layout.dialog_simple, null, false);

        TextView titleTextView = (TextView)viewDialog.findViewById(R.id.title);
        titleTextView.setText(title);

        this.setCancelable(false);

        this.setView(viewDialog);

    } }
