package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        if(username.getText() == null || username.length()==0) {
            Toast.makeText(getActivity(), getString(R.string.invalid_name), Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.PREF_USERNAME), username.getText().toString());
            editor.commit();

            MainActivity_.intent(getActivity()).start();
            getActivity().finish();
        }

    }
}
