package com.example.karyc.testapplication;

import android.support.annotation.NonNull;

import com.redmadrobot.inputmask.helper.Mask;
import com.redmadrobot.inputmask.model.CaretString;

import java.io.Serializable;

public class Data implements Serializable {
    @NonNull
    private String email;
    @NonNull
    private String phone;
    @NonNull
    private String password;
    @NonNull
    private String photo;

    public Data(@NonNull String email, @NonNull String phone, @NonNull String password, @NonNull String photo) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.photo = photo;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getPhoto() {
        return photo;
    }

    public String getFormattedPhone (){
        final Mask mask = new Mask("+7 ([000]) [000] [00] [00]");
        final String input = getPhone();
        final Mask.Result result = mask.apply(
                new CaretString(
                        input,
                        input.length()
                ),
                true // you may consider disabling autocompletion for your case
        );
        final String output = result.getFormattedText().getString();
        return output;
    }
}
