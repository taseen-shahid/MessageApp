package com.example.messagesapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1;

    private EditText phoneNumberEditText;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.editTextText3);
        messageEditText = findViewById(R.id.editTextTextMultiLine2);
        sendButton = findViewById(R.id.button);

        // Check and request SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        if (phoneNumber.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please enter both phone number and message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Permission to send SMS is not granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkBalance() {
        String ussdCode = "*123#"; // Replace with your network's USSD code for balance check
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("##", null, ussdCode, null, null); // Use an appropriate destination
        } catch (Exception e) {
            Log.e("SMS", "Failed to send USSD code: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with SMS operations
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
