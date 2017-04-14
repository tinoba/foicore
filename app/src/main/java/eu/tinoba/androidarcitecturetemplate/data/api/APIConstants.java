package eu.tinoba.androidarcitecturetemplate.data.api;

public interface APIConstants {

    String GRANT_TYPE = "client_credentials";
    String CLIENT_ID = "27pXeRQdv43VDFGwVTM6Y3Ke3ZRUFI3T";
    String CLIENT_SECRET = "7x6kb7ywGw0n8oQj";
    String SCOPE = "hybris.tenant=foicore hybris.product_create hybris.product_publish";
    String PRODUCT_QUERY = "code:";

    String AUTHORIZATION_HEADER = "Authorization";

    String CONTENT_TYPE_HEADER = "Content-Type: application/vnd.api+json";

    String BASE_URL = "https://api.beta.yaas.io/hybris/";

    String PATH_LOGIN = "customer/v1/foicore/login";
    String PATH_TOKEN = "oauth2/v1/token";
    String PATH_PRODUCT = "product/v2/foicore/products";
}
