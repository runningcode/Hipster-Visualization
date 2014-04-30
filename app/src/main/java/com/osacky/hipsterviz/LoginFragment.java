package com.osacky.hipsterviz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.osacky.hipsterviz.api.lastFmApi.UserSpiceRequest;
import com.osacky.hipsterviz.models.User;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseSpiceFragment implements RequestListener<User> {

    @SuppressWarnings("unused")
    private static final String TAG = "LoginFragment";

    @ViewById(R.id.username_text)
    EditText username;

    @Click(R.id.submit_user_button)
    void submitButtonClicked() {
        //TODO: validate user
        if (username.getText() == null || username.length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.too_short), Toast.LENGTH_SHORT).show();
        } else {
            getSpiceManager().execute(new UserSpiceRequest(username.getText().toString()), this);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        String error = spiceException.getCause().getCause().getCause().getMessage();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestSuccess(User user) {
        String name = user.getRealName() != null ? user.getRealName() : user.getName();
        String welcomeMessage = String.format(getString(R.string.welcome_string), name);
        Toast.makeText(getActivity(), welcomeMessage, Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.PREF_USERNAME), user.getName());
        editor.commit();

        MainActivity_.intent(getActivity()).start();
        getActivity().finish();
    }
}
