package com.example.organization.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;

import com.example.organization.data.ConnectivityHelper;
import com.example.organization.data.LoginRepository;
import com.example.organization.data.Result;
import com.example.organization.data.model.LogInInitiator;
import com.example.organization.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private Context context;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(Context context, String username, String password) {
        // can be launched in a separate asynchronous job
       this.context = context;
       Auth auth = new Auth();

       auth.execute(username, password);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private class Auth extends AsyncTask <String, Void, Result<LogInInitiator>> {

        @Override
        protected Result<LogInInitiator> doInBackground(String... param) {

            Result<LogInInitiator> result = loginRepository.login(param[0], param[1]);

            return result;
        }

        @Override
        protected void onPostExecute(Result<LogInInitiator> result) {

            if (result instanceof Result.Success) {
                LogInInitiator data = ((Result.Success<LogInInitiator>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getInitiatorName())));
            } else if(!ConnectivityHelper.isConnectedToNetwork(context)) {
                loginResult.setValue(new LoginResult(R.string.internet_not_available));
            }else{
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }

    }
}
