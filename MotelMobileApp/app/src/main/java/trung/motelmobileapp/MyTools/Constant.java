package trung.motelmobileapp.MyTools;

public class Constant {
    //Web server
    //    private static final String WEBSERVER_HOST = "10.0.2.2";
//    private static final String WEBSERVER_HOST = "192.168.43.160";
        private static final String WEBSERVER_HOST= "192.168.0.105";
//    private static final String WEBSERVER_HOST = "192.168.1.126";
    private static final String WEBSERVER_PORT = "3000";
    public static final String WEB_SERVER = "http://" + WEBSERVER_HOST + ":" + WEBSERVER_PORT;

    //Session
    public static final String MY_SESSION = "My Session";

    //Request ID
    public static final int REQUEST_ID_FOR_MAKE_NEW_POST = 1;
    public static final int REQUEST_ID_FOR_EDIT_POST = 2;
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 3;
    public static final int REQUEST_ID_CHECK_CONNECTION_TO_SERVER = 4;

}
