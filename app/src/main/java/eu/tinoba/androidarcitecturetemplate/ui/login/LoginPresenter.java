package eu.tinoba.androidarcitecturetemplate.ui.login;

import eu.tinoba.androidarcitecturetemplate.data.api.models.request.LoginApiRequest;

public interface LoginPresenter {

    void setView(LoginView view);

    void login(LoginApiRequest loginApiRequest);

    void dispose();
}
