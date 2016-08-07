package cn.xuhongxu.xiaoya.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cn.xuhongxu.Assist.SchoolworkAssist;
import cn.xuhongxu.xiaoya.R;
import cn.xuhongxu.xiaoya.YaApplication;

public class LoginActivity extends AppCompatActivity {

    public static final String MESSAGE_LOGOUT = "cn.xuhongxu.xiaoya.Activity.LoginActivity.logout";

    YaApplication app;
    private SharedPreferences preferences;

    private TextInputEditText editUsername;
    private TextInputEditText editPassword;
    private Switch switchRemember;

    private ProgressDialog progressDialog;

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SchoolworkAssist assist = new SchoolworkAssist(params[0], params[1]);

            try {
                assist.login();
                if (app.getStudentInfo() == null) {
                    app.setStudentInfo(assist.getStudentInfo());
                }
            } catch (Exception e) {
                return e.getMessage();
            }

            ((YaApplication) getApplication()).setAssist(assist);
            return getString(R.string.succeed_login);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            View view = findViewById(android.R.id.content);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", editUsername.getText().toString());
            editor.apply();
            if (result == null) {
                result = "网络错误";
            }
            if (getString(R.string.succeed_login).contentEquals(result)) {
                if (switchRemember.isChecked()) {
                    editor.putString("password", editPassword.getText().toString());
                    editor.putBoolean("remember", true);
                    editor.apply();
                }
                loginSucceed();
            } else {
                // 登录失败
                if (view == null) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, result, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getColor(R.color.colorError));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getColor(R.color.colorWhite));
                    snackbar.show();
                }
            }
        }
    }

    // 登录成功
    protected void loginSucceed() {

        // 记住我
        SharedPreferences.Editor editor = preferences.edit();

        // 启动主活动
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (YaApplication) getApplication();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editUsername = (TextInputEditText) findViewById(R.id.edit_username);
        editPassword = (TextInputEditText) findViewById(R.id.edit_password);
        switchRemember = (Switch) findViewById(R.id.switch_remember);

        preferences =
                getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);

        editUsername.setText(preferences.getString("username", ""));
        if (preferences.getBoolean("remember", false)) {
            Intent intent = getIntent();
            if (intent.getBooleanExtra(MESSAGE_LOGOUT, false)) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("password");
                editor.putBoolean("remember", false);
                editor.apply();
            } else {
                editPassword.setText(preferences.getString("password", ""));
                switchRemember.setChecked(true);
                login(null);
            }
        }

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(v);
                    return true;
                }
                return false;
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void login(View view) {

        hideKeyboard(this);

        progressDialog = ProgressDialog.show(this, "登录中", "正在登录统一身份认证平台...", true);
        new LoginTask().execute(editUsername.getText().toString(),
                editPassword.getText().toString());
    }
}