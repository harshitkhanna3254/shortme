package com.comp519.shortme.constants;

public class BigTableConstants {

    /*
        =======================================================
                        User Table
        =======================================================
    */

    // Account Info Column Family
    public static final String PASSWORD_QUALIFIER = "password";
    public static final String FIRST_NAME_QUALIFIER = "first_name";
    public static final String LAST_NAME_QUALIFIER = "last_name";

    // Subscription Info Column Family
    public static final String SUBSCRIPTION_PLAN_QUALIFIER = "subscription_plan";
    public static final String START_DATE_QUALIFIER = "start_date";
    public static final String END_DATE_QUALIFIER = "end_date";

    // Urls Column Family
    public static final String SHORT_URLS_QUALIFIER = "short_urls";

     /*
        =======================================================
                        Url Mappings Table
        =======================================================
    */

    // Url Data Column Family
    public static final String LONG_URL_QUALIFIER = "long_url";

    // User Data Column Family
    public static final String USERNAME_QUALIFIER = "username";


    /*
        =======================================================
                        Common
        =======================================================
    */

    public static final String CREATED_AT_QUALIFIER = "created_at";

}
