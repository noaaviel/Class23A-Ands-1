package com.example.class23a_ands_1.all_activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.class23a_ands_1.R;
import com.example.class23a_ands_1.utills.UtilFuncs;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private final String wifiAvielHome = "Aviel";
    private Button btnLogin;
    private int percentage;
    private String password;
    private String wifiName;
    private boolean wifiIsCorrect;
    private boolean isPlugged;
    private boolean isMuted;
    private boolean isUserNameCorrect;
    private boolean isPasswordCorrect;
    private String ValUsername;
    private String ValPassword;

    //this app will make a successful login if the following will happen:
    //1. username = admin@gmail.com
    //2. password = admin concatenated with the battery percent that is currently set on device
    //3. device is plugged with charging cable
    //4. the device has a contact under the name "Bar Mizrahi תוכנה"
    //5. device is connected to wifi named - "Aviel"
    //6. device ring sound level is on mute/vibrate
    // these all need to happen at the same time. if one of the following will not happen
    // the login would not be successful

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initListeners(){
        btnLogin.setOnClickListener(v -> {
            ValUsername = edtUsername.getText().toString();
            ValPassword = edtPassword.getText().toString();

            //Fetch Battery Information and build password
            percentage = UtilFuncs.getBatteryPercentage(v.getContext());
            password="admin";

            password = password.concat(""+percentage);

            Intent mIntent = getIntent();
            boolean contactExist = mIntent.getBooleanExtra("booleanContactExist", false);

            wifiName = UtilFuncs.getWifiName(this);
            initConditions();
            if (isUserNameCorrect && isPasswordCorrect && isPlugged && wifiIsCorrect && isMuted && contactExist){
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("userdata", ValUsername);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "Username or password is wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initConditions(){
         wifiIsCorrect= wifiName.equals(wifiAvielHome);
         isPlugged = UtilFuncs.isPlugged(this);
         isMuted= UtilFuncs.getSoundLevelOfPhoneRing(this)==0;
         isUserNameCorrect=(ValUsername.equals("admin@gmail.com"));
         isPasswordCorrect=ValPassword.equals(password);

    }

    private void initViews() {
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        btnLogin    = (Button)findViewById(R.id.btnLogin);
    }

}