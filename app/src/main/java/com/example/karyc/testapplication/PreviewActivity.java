package com.example.karyc.testapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.karyc.testapplication.databinding.ActivityPreviewBinding;

import activitystarter.ActivityStarter;
import activitystarter.Arg;

public class PreviewActivity extends AppCompatActivity {
    private Uri photoUri;
    @Arg
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this);
        ActivityPreviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_preview);
        binding.setInteractor(new PreviewActivityInteractor());

        binding.setData(data);
        photoUri = Uri.parse(data.getPhoto());
        Glide.with(this).load(photoUri).into(binding.cameraImage);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void composeEmail(String addresses, String subject, Uri attachment, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresses});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class PreviewActivityInteractor {
        public void onClickSendEmail(@SuppressWarnings("unused") View view) {
            String packageName = getApplicationContext().getPackageName();
            String text = getString(R.string.email_text_template, data.getEmail(), data.getPhone());
            composeEmail(data.getEmail(), packageName + ": " + getString(R.string.project_name), photoUri, text);
        }
    }
}
