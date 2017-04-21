package eu.tinoba.androidarcitecturetemplate.ui.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ybq.android.spinkit.SpinKitView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;
import eu.tinoba.androidarcitecturetemplate.injection.component.ActivityComponent;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;

public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    LoginPresenter presenter;

    @BindView(R.id.login_activity_login_button)
    protected Button button;

    @BindView(R.id.login_activity_spin_kit)
    protected SpinKitView spinKitView;

    @BindView(R.id.login_activity_input_layout_password)
    protected TextInputLayout textInputLayoutPassword;

    @BindView(R.id.login_activity_input_email)
    protected TextInputEditText username;

    @BindView(R.id.login_activity_input_password)
    protected TextInputEditText password;

    @BindView(R.id.login_activity_error_icon)
    protected ImageView imageViewErrorIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        imageViewErrorIcon.setVisibility(View.GONE);
        //test data
        username.setText("noeply@yaastest.com");
        password.setText("secret");
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.dispose();
    }

    @OnClick(R.id.login_activity_login_button)
    public void login() {
        spinKitView.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
        presenter.login(new LoginApiRequest(password.getText().toString(), username.getText().toString()));
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void showError() {
        textInputLayoutPassword.setError(getString(R.string.login_error_description));
        imageViewErrorIcon.setVisibility(View.VISIBLE);
        spinKitView.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }
}
