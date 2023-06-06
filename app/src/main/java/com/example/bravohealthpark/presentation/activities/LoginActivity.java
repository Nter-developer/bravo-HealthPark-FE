package com.example.bravohealthpark.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bravohealthpark.R;
import com.example.bravohealthpark.domain.authority.dto.LoginDto;
import com.example.bravohealthpark.domain.authority.dto.LoginResponse;
import com.example.bravohealthpark.global.error.ToastErrorMessage;
import com.example.bravohealthpark.infra.preferences.APIPreferences;
import com.example.bravohealthpark.infra.preferences.SharedPreferenceBase;
import com.example.bravohealthpark.infra.preferences.UserPreferences;
import com.example.bravohealthpark.infra.retrofit.RetrofitClient;
import com.example.bravohealthpark.infra.retrofit.RetrofitService;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private Button loginBtn, singUpBtn;
    private EditText loginId, phoneNumber;
    private static Call<LoginResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Optional<String> token = Optional.ofNullable(
                SharedPreferenceBase.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_COOKIE));

        if(token.isPresent()) {
            callAutoLoginRequest(new LoginDto(
                    SharedPreferenceBase.getSharedPreference(UserPreferences.PREFERENCE_USER_LOGIN_ID),
                    SharedPreferenceBase.getSharedPreference(UserPreferences.PREFERENCE_USER_PHONE_NUMBER)));
        }

        findComponents();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceBase.initialize(LoginActivity.this.getApplicationContext());
                callLoginRequest(new LoginDto(loginId.getText().toString(), phoneNumber.getText().toString()));
            }
        });

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findComponents() {
        loginBtn = (Button) findViewById(R.id.Login_Btn);
        singUpBtn = (Button) findViewById(R.id.Signup_Btn);
        loginId = (EditText) findViewById(R.id.edittext_login_id);
        phoneNumber = (EditText) findViewById(R.id.edittext_login_pnum);
    }

    private void callAutoLoginRequest(LoginDto autoLoginDto) {
        initRetrofitServiceAndCall(autoLoginDto);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    intentMainActivityAndClearTask();
                }
                else {
                    toastCustomMessage(ToastErrorMessage.ERROR_MESSAGE_LOGIN_FAIL);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                toastCustomMessage(ToastErrorMessage.ERROR_MESSAGE_NETWORK_ERROR);
            }
        });
    }

    private void callLoginRequest(LoginDto inputLoginDto) {
        initRetrofitServiceAndCall(inputLoginDto);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    setSharedPreferenceIdPNumberTokens(response);
                    intentMainActivityAndClearTask();
                }
                else {
                    toastCustomMessage(ToastErrorMessage.ERROR_MESSAGE_LOGIN_FAIL);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                toastCustomMessage(ToastErrorMessage.ERROR_MESSAGE_NETWORK_ERROR);
            }
        });
    }

    private void initRetrofitServiceAndCall(LoginDto loginDto) {
        retrofitService = RetrofitClient.getApiService(RetrofitService.class);
        call = retrofitService.sendLoginRequest(loginDto);
    }

    private void toastCustomMessage(String customMessage) {
        Toast.makeText(LoginActivity.this.getApplicationContext(),
                customMessage, Toast.LENGTH_SHORT).show();
    }

    private void intentMainActivityAndClearTask() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setSharedPreferenceIdPNumberTokens(Response<LoginResponse> response) {
        SharedPreferenceBase.setSharedPreference(
                UserPreferences.PREFERENCE_USER_LOGIN_ID, loginId.getText().toString());
        SharedPreferenceBase.setSharedPreference(
                UserPreferences.PREFERENCE_USER_PHONE_NUMBER, phoneNumber.getText().toString());
    }
}
