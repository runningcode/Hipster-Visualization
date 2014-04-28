package com.osacky.hipsterviz;

import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseSpiceFragment {

    @ViewById(R.id.username_text)
    EditText username;

    @Click(R.id.submit_user_button)
    void submitButtonClicked(){
        //TODO: validate user
        if(username.getText() == null || username.length()==0)
            Toast.makeText(getActivity(), getString(R.string.invalid_name), Toast.LENGTH_SHORT).show();
        else
            MainActivity_.intent(getActivity()).username(username.getText().toString()).start();
    }
}
