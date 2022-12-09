package com.example.class23a_ands_1.all_activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.class23a_ands_1.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsActivity  extends AppCompatActivity {

    private final int REQUEST_CODE_CONTACTS = 101;
    private final int MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE = 102;
    private  boolean contactExist;
    ArrayList<JSONObject> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestContactsPermission();

    }

    private void requestContactsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_CONTACTS);
    }

    private void requestPermissionWithRationaleCheck() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            Log.d("noa", "shouldShowRequestPermissionRationale = true");
            // Show user description for what we need the permission

            String message = "permission description for approve";
            AlertDialog alertDialog =
                    new AlertDialog.Builder(this)
                            .setMessage(message)
                            .setPositiveButton(getString(android.R.string.ok),
                                    (dialog, which) -> {
                                        requestContactsPermission();
                                        dialog.cancel();
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // disabled functions due to denied permissions
                                }
                            })
                            .show();
            alertDialog.setCanceledOnTouchOutside(true);
        } else {
            Log.d("noa", "shouldShowRequestPermissionRationale = false");
            openPermissionSettingDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE) {
            boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (result) {
                getContacts();
                Log.d("noa","in contacts activity"+contactExist);
                Intent myIntent = new Intent(ContactsActivity.this, MainActivity.class);
                myIntent.putExtra("booleanContactExist",contactExist);
                startActivity(myIntent);
                finish();

                return;
            }
        }
    }

    private void getContacts() {
        //to store name-number pair
        JSONObject obj = new JSONObject();

        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //@SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                if(name.equals("Bar Mizrahi תוכנה")){
                    contactExist=true;
                }
                contacts.add(obj);
                Log.d("NOA", name);
            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_CONTACTS: {
                Log.d("pttt", "REQUEST_CODE_PERMISSION_CONTACTS");
                boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
                // If request is cancelled, the result arrays are empty.
                if (result) {
                    getContacts();
                    Intent myIntent = new Intent(ContactsActivity.this, MainActivity.class);
                    myIntent.putExtra("booleanContactExist",contactExist);
                    //Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();

                    return;

                }

                requestPermissionWithRationaleCheck();
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void openPermissionSettingDialog() {
        String message = "Setting screen if user have permanently disable the permission by clicking Don't ask again checkbox.";
        AlertDialog alertDialog =
                new AlertDialog.Builder(ContactsActivity.this)
                        .setMessage(message)
                        .setPositiveButton(getString(android.R.string.ok),
                                (dialog, which) -> {
                                    openSettingsManually();
                                    dialog.cancel();
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void openSettingsManually() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
