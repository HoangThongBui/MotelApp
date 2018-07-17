package trung.motelmobileapp.MyTools;



public class Constant {
    //Web server
    //    private static final String WEBSERVER_HOST = "10.0.2.2";
//    private static final String WEBSERVER_HOST = "192.168.43.160";
        private static final String WEBSERVER_HOST= "192.168.0.100";
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
    public static final int REQUEST_ID_FOR_REGISTER = 5;
    public static final int REQUEST_ID_FOR_ADD_POST = 6;
    public static final int REQUEST_ID_FOR_UPDATE_POST = 7;
    public static final int REQUEST_ID_FOR_DELETE_POST = 8;
    public static final int REQUEST_ID_FOR_LOGOUT = 9;
    public static final int REQUEST_ID_FOR_GO_TO_CHOOSE_IMAGE = 10;
    public static final int REQUEST_ID_FOR_CAMERA = 11;
    public static final int REQUEST_ID_FOR_GALLERY = 12;
    public static final int REQUEST_ID_FOR_STORAGE_PERMISSION = 13;
    public static final int REQUEST_ID_FOR_UPDATE_PROFILE = 14;

    //Num of max image
    public static final int MAX_POST_IMAGE = 3;
}
