package eu.tinoba.androidarcitecturetemplate.data.api;

public interface APIConstants {

    String GRANT_TYPE = "client_credentials";
    String CLIENT_ID = "jt839qVtgjPhLXUigipC8wAXhNyQiODK";
    String CLIENT_SECRET = "qIO19d6j2rki1IV4";
    String SCOPE = "hybris.tenant=foicore1 hybris.product_create hybris.product_publish hybris.product_read_unpublished";
    String PRODUCT_QUERY = "code:";

    String AUTHORIZATION_HEADER = "Authorization";

    String CONTENT_TYPE_HEADER = "Content-Type: application/vnd.api+json";

    String BASE_URL = "https://api.beta.yaas.io/hybris/";
    String BASE_URL2 = "http://barka.foi.hr/WebDiP/2015_projekti/WebDiP2015x008/";

    String PATH_LOGIN = "customer/v1/foicore1/login";
    String PATH_TOKEN = "oauth2/v1/token";
    String PATH_PRODUCT = "product/v2/foicore1/products";
    String PATH_CHECKOUT = "history.php";
    String PATH_HISTORY = "history.php";
}
