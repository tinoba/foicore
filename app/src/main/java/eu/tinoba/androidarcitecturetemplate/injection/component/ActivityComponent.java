package eu.tinoba.androidarcitecturetemplate.injection.component;

import dagger.Component;
import eu.tinoba.androidarcitecturetemplate.injection.module.ActivityModule;
import eu.tinoba.androidarcitecturetemplate.injection.module.PresenterModule;
import eu.tinoba.androidarcitecturetemplate.injection.module.RouterModule;
import eu.tinoba.androidarcitecturetemplate.injection.scope.ForActivity;
import eu.tinoba.androidarcitecturetemplate.ui.base.activities.BaseActivity;
import eu.tinoba.androidarcitecturetemplate.ui.cart.CartPresenter;
import eu.tinoba.androidarcitecturetemplate.ui.cart.CartRouter;
import eu.tinoba.androidarcitecturetemplate.ui.home.HomePresenter;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginPresenter;
import eu.tinoba.androidarcitecturetemplate.ui.login.LoginRouter;
import eu.tinoba.androidarcitecturetemplate.ui.search.SearchProductResultPresenter;

@ForActivity
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                ActivityModule.class,
                PresenterModule.class,
                RouterModule.class
        }
)
public interface ActivityComponent extends ActivityComponentActivityInjects, ActivityComponentFragmentsInjects {

    final class Initializer {

        private Initializer() {
        }

        public static ActivityComponent init(final ApplicationComponent applicationComponent, final BaseActivity activity) {
            return DaggerActivityComponent.builder()
                                          .applicationComponent(applicationComponent)
                                          .activityModule(new ActivityModule(activity))
                                          .build();
        }
    }

    CartRouter getHomeRouter();

    CartPresenter getCartPresenter();

    LoginRouter getLoginRouter();

    LoginPresenter getLoginPresenter();

    SearchProductResultPresenter getSearchProductResultPresenter();

    HomePresenter getHomePresenter();
}
