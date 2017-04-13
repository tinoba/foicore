package eu.tinoba.androidarcitecturetemplate.ui.login;

import android.os.Bundle;
import android.widget.EditText;

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

    @BindView(R.id.password)
    protected EditText password;

    @BindView(R.id.username)
    protected EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
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

    @OnClick(R.id.login_button)
    public void login() {
        presenter.login(new LoginApiRequest(password.getText().toString(), username.getText().toString()));
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}
