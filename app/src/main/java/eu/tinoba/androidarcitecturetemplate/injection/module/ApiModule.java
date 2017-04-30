package eu.tinoba.androidarcitecturetemplate.injection.module;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.tinoba.androidarcitecturetemplate.data.api.NetworkInterceptor;
import eu.tinoba.androidarcitecturetemplate.data.api.converter.ProductsApiConverter;
import eu.tinoba.androidarcitecturetemplate.data.api.converter.ProductsApiConverterImpl;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomAPI;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomNetworkService;
import eu.tinoba.androidarcitecturetemplate.data.service.CustomNetworkServiceImpl;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkService;
import eu.tinoba.androidarcitecturetemplate.data.service.NetworkServiceImpl;
import eu.tinoba.androidarcitecturetemplate.data.service.TemplateAPI;
import eu.tinoba.androidarcitecturetemplate.device.ApplicationInformation;
import eu.tinoba.androidarcitecturetemplate.device.DeviceInformation;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.BASE_URL;
import static eu.tinoba.androidarcitecturetemplate.data.api.APIConstants.BASE_URL2;

@Module
public final class ApiModule {

    public static final String RETROFIT_1 = "retrofit1";
    public static final String RETROFIT_2 = "retrofit2";

    private static final int CONNECTION_TIMEOUT = 10;

    @Provides
    @Singleton
    @Named(RETROFIT_1)
    Retrofit provideRetrofit(final OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named(RETROFIT_2)
    Retrofit provideRetrofit2(final OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NetworkService provideNetworkService(final TemplateAPI templateAPI) {
        return new NetworkServiceImpl(templateAPI);
    }

    @Provides
    @Singleton
    CustomNetworkService provideCustomNetworkService(final CustomAPI customAPI) {
        return new CustomNetworkServiceImpl(customAPI);
    }

    @Provides
    @Singleton
    TemplateAPI provideTemplateAPI(@Named(RETROFIT_1) final Retrofit retrofit) {
        return retrofit.create(TemplateAPI.class);
    }

    @Provides
    @Singleton
    CustomAPI provideCustomAPI(@Named(RETROFIT_2) final Retrofit retrofit) {
        return retrofit.create(CustomAPI.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(final HttpLoggingInterceptor loggingInterceptor, final NetworkInterceptor networkInterceptor) {
        final OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                .addInterceptor(networkInterceptor);
        okhttpBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS).interceptors().add(loggingInterceptor);

        return okhttpBuilder.build();
    }

    @Provides
    @Singleton
    public NetworkInterceptor provideNetworkInterceptor(final DeviceInformation deviceInformation,
                                                        final ApplicationInformation applicationInformation) {
        final int osVersion = deviceInformation.getOsVersionInt();
        final String appVersionName = applicationInformation.getVersionName();

        return new NetworkInterceptor(osVersion, appVersionName);
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    @Provides
    @Singleton
    ProductsApiConverter provideProductsApiConverter() {
        return new ProductsApiConverterImpl();
    }
}
