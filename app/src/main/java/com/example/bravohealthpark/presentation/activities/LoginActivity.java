package com.example.bravohealthpark.presentation.activities;

import static com.example.bravohealthpark.infra.preferences.SharedPreferenceBase.getSharedPreference;
import static com.example.bravohealthpark.infra.utils.IntentUtils.startNewActivity;
import static com.example.bravohealthpark.infra.utils.ToastUtils.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bravohealthpark.R;
import com.example.bravohealthpark.domain.authority.dto.LoginDto;
import com.example.bravohealthpark.domain.authority.dto.LoginResponse;
import com.example.bravohealthpark.domain.user.service.UserRetrofitService;
import com.example.bravohealthpark.global.error.ErrorMessages;
import com.example.bravohealthpark.infra.preferences.APIPreferences;
import com.example.bravohealthpark.infra.preferences.SharedPreferenceBase;
import com.example.bravohealthpark.infra.preferences.UserPreferences;
import com.example.bravohealthpark.infra.retrofit.RetrofitClient;
import com.example.bravohealthpark.infra.utils.Messages;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private UserRetrofitService userRetrofitService;
    private Button buttonLogin, buttonSignup;
    private EditText editTextLoginId, editTextPNumber;
    private static Call<LoginResponse> callLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialize components
        initComponents();

        Optional<String> token = Optional.ofNullable(
                getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_COOKIE));

        if(token.isPresent()) {
            performAutoLoginRequest(new LoginDto(
                    getSharedPreference(UserPreferences.PREFERENCE_USER_LOGIN_ID),
                    getSharedPreference(UserPreferences.PREFERENCE_USER_PHONE_NUMBER)));
        }



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceBase.initialize(getApplicationContext());
                performLoginRequest(new LoginDto(editTextLoginId.getText().toString(), editTextPNumber.getText().toString()));
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        buttonLogin = (Button) findViewById(R.id.Login_Btn);
        buttonSignup = (Button) findViewById(R.id.Signup_Btn);
        editTextLoginId = (EditText) findViewById(R.id.edittext_login_id);
        editTextPNumber = (EditText) findViewById(R.id.edittext_login_pnum);
    }

    private void performAutoLoginRequest(LoginDto autoLoginDto) {
        initRetrofitServiceAndCreateCall(autoLoginDto);
        callLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    showToast(getApplicationContext(), Messages.LOGIN_SUCCESS);
                    startNewActivity(getApplicationContext(), MainActivity.class, true);
                }
                else {
                    showToast(getApplicationContext(), ErrorMessages.LOGIN_FAIL);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showToast(getApplicationContext(), ErrorMessages.NETWORK_ERROR);
            }
        });
    }

    private void performLoginRequest(LoginDto inputLoginDto) {
        initRetrofitServiceAndCreateCall(inputLoginDto);
        callLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    showToast(getApplicationContext(), Messages.LOGIN_SUCCESS);
                    saveLogIdAndPNumber();
                    startNewActivity(getApplicationContext(), MainActivity.class, true);
                }
                else {
                    showToast(getApplicationContext(), ErrorMessages.LOGIN_FAIL);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showToast(getApplicationContext(), ErrorMessages.NETWORK_ERROR);
            }
        });
    }

    private void initRetrofitServiceAndCreateCall(LoginDto loginDto) {
        userRetrofitService = RetrofitClient.getApiService(UserRetrofitService.class);
        callLogin = userRetrofitService.sendLoginRequest(loginDto);
    }

    private void saveLogIdAndPNumber() {
        SharedPreferenceBase.setSharedPreference(
                UserPreferences.PREFERENCE_USER_LOGIN_ID, editTextLoginId.getText().toString());
        SharedPreferenceBase.setSharedPreference(
                UserPreferences.PREFERENCE_USER_PHONE_NUMBER, editTextPNumber.getText().toString());
    }
}
