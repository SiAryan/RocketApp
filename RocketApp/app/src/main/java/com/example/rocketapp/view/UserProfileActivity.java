package com.example.rocketapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rocketapp.R;
import com.example.rocketapp.controller.ExperimentManager;
import com.example.rocketapp.controller.UserManager;
import com.example.rocketapp.helpers.Validate;
import com.example.rocketapp.model.users.User;

/**
 * User has the ability to update their email or phone number through this page
 */
public class UserProfileActivity extends RocketAppActivity {
    public ImageButton saveProfileData;
    public User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        user = UserManager.getUser(getIntent().getSerializableExtra("id"));

        EditText userName = findViewById(R.id.userNameOnProfile);
        userName.setText(user.getName());

        EditText userEmail = findViewById(R.id.userEmail);      //field to enter email
        EditText userPhoneNumber = findViewById(R.id.userPhoneNumber);      //field to enter phone number

        userEmail.setText(user.getEmail());
        userPhoneNumber.setText(user.getPhoneNumber());

        saveProfileData = findViewById(R.id.saveUserProfileData);

        if (!UserManager.getUser().equals(user)) {
            userEmail.setEnabled(false);
            userPhoneNumber.setEnabled(false);
            userName.setEnabled(false);
            saveProfileData.setVisibility(View.GONE);
        } else {

            saveProfileData.setOnClickListener(v -> {
                // Check inputs and update profile
                if (!Validate.lengthInRange(userName, 3, 50, true)) return;
                if (!Validate.lengthInRange(userEmail, 3, 50, true)) return;
                if (!Validate.lengthInRange(userPhoneNumber, 7, 11, true)) return;

                UserManager.getUser().setName(userEmail.getText().toString());
                UserManager.getUser().setPhoneNumber(userPhoneNumber.getText().toString());
                UserManager.getUser().setEmail(userName.getText().toString());

                UserManager.updateUser(user -> {
                    Toast.makeText(UserProfileActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                    toggleKeyboard(false);
                    finish();
                }, e -> {
                    Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                });
            });
        }

        // Create back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
