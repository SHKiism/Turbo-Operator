package ir.taxi1880.operatormanagement.app;

public class EndPoints {

    /*TODO : check apis and ports before release*/

//    http://172.16.2.201:1881/api/operator/*****
//    http://api.parsian.ir:1881/api/operator/
//    http://172.16.2.210:1885/api/findway/citylatinname/address

    public static final String IP =
            "http://turbotaxi.ir";

    public static final String HAKWEYE_IP =
            "http://turbotaxi.ir";

    public static final String PUSH_ADDRESS =
            "http://turbotaxi.ir:6060";

    public static final String APIPort = "1881";
    public static final String PIC_APIPort = "1880";
    public static final String TRIP_APIPort = "1881";
    public static final String CALL_VOICE_APIPort = "1884";
    public static final String HAWKEYE_APIPort = "1890";

    public static final String ACRA_PATH = "http://turbotaxi.ir:6061/api/v1/crashReport";

    public static final String WEBSERVICE_PATH = IP + ":" + APIPort + "/api/operator/v3/";
    public static final String OPERATOR_MISTAKE_WEBSERVICE_PATH = IP + ":" + APIPort + "/api/operator/v3/mistake";
    public static final String SCORE_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/score/";
    public static final String SUPPORT_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/support/";
    public static final String LISTEN_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/support/listen/";
    public static final String SUPPORT_TRIP_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/support/trip/";
    public static final String SHIFT_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/shift/";
    public static final String TRIP_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/trip/";
    public static final String COMPLAINT_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/complaint/";
    public static final String DRIVER_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/support/driver/";
    public static final String DRIVER_STATION_WEBSERVICE_PATH = IP + ":" + TRIP_APIPort + "/api/operator/v3/support/driver/station/";
    public static final String CALL_VOICE_PATH = IP + ":" + CALL_VOICE_APIPort + "/api/v1/"; //  http://turbotaxi.ir:1884/api/v1/

    public static final String HAWKEYE_PATH = HAKWEYE_IP + ":" + HAWKEYE_APIPort + "/api/user/v1/";
    public static final String HAWKEYE_LOGIN_PATH = HAKWEYE_IP + ":" + HAWKEYE_APIPort + "/api/user/v1/login/phone/";

    /******************************** Base Api *********************************/

    public static final String GET_APP_INFO = WEBSERVICE_PATH + "getAppInfo";
    public static final String GET_MESSAGES = WEBSERVICE_PATH + "getMessages";
    public static final String GET_NEWS = WEBSERVICE_PATH + "getNews";
    public static final String SEND_MESSAGES = WEBSERVICE_PATH + "sendMessages";
    public static final String SET_NEWS_SEEN = WEBSERVICE_PATH + "setNewsSeen";
    public static final String GET_SHIFTS = WEBSERVICE_PATH + "shift";
    public static final String GET_SHIFT_OPERATOR = SHIFT_WEBSERVICE_PATH + "operators";
    public static final String GET_SHIFT_REPLACEMENT_REQUESTS = SHIFT_WEBSERVICE_PATH + "getReplacementRequests";
    public static final String SHIFT_REPLACEMENT_REQUEST = SHIFT_WEBSERVICE_PATH + "replacementRequest";
    public static final String CANCEL_REPLACEMENT_REQUEST = SHIFT_WEBSERVICE_PATH + "cancelReplacementRequest";
    public static final String ANSWER_SHIFT_REPLACEMENT_REQUEST = SHIFT_WEBSERVICE_PATH + "answerReplacementRequest";

    /******************************** Trip Register Api *********************************/

    public static final String STATION_INFO = TRIP_WEBSERVICE_PATH + "stationInfo";//api/operator/v3/trip/stationInfo/:city/:code/:name
    public static final String INSERT_TRIP_SENDING_QUEUE = TRIP_WEBSERVICE_PATH + "insertTripSendingQueue";
    public static final String ACTIVATE = TRIP_WEBSERVICE_PATH + "activate";
    public static final String DELETE_ADDRESS = TRIP_WEBSERVICE_PATH + "deleteAddress";
    public static final String DEACTIVATE = TRIP_WEBSERVICE_PATH + "deActivate";
    public static final String HIRE_TYPES = TRIP_WEBSERVICE_PATH + "hireTypes";
    public static final String HIRE = TRIP_WEBSERVICE_PATH + "hire";
    public static final String GET_TRIP_WITH_ZERO_STATION = TRIP_WEBSERVICE_PATH + "getTripWithZeroStation";
    public static final String WITHOUT_STATION = TRIP_WEBSERVICE_PATH + "withoutStation";
    public static final String UPDATE_TRIP_STATION = TRIP_WEBSERVICE_PATH + "updateTripStation";
    public static final String STATION = TRIP_WEBSERVICE_PATH + "station";
    public static final String EDIT_STATION = TRIP_WEBSERVICE_PATH + "editStation";

    public static final String PASSENGER_INFO = TRIP_WEBSERVICE_PATH + "passengerInfo";
    public static final String CALL_VOICE = CALL_VOICE_PATH + "voice/"; // http://turbotaxi.ir:1884/api/v1/voice/1604130536.10343290

    /******************************** Account Api *********************************/

    public static final String BALANCE = WEBSERVICE_PATH + "balance";
    public static final String UPDATE_PROFILE = WEBSERVICE_PATH + "updateProfile";
    public static final String PAYMENT = WEBSERVICE_PATH + "payment";

    /******************************** Score Api *********************************/

    public static final String SCORE = WEBSERVICE_PATH + "score";
    public static final String BESTS = SCORE_WEBSERVICE_PATH + "bests";
    public static final String REWARDS = SCORE_WEBSERVICE_PATH + "rewards";
    public static final String SINGLE = SCORE_WEBSERVICE_PATH + "single";

    /******************************** Support Api *********************************/

    public static final String SEARCH_SERVICE = SUPPORT_TRIP_WEBSERVICE_PATH + "v1/search";//api/operator/v3/support/trip/v1/search
    public static final String MISTAKE = SUPPORT_TRIP_WEBSERVICE_PATH + "v1/mistake";
    public static final String CANCEL = SUPPORT_TRIP_WEBSERVICE_PATH + "cancel";
    public static final String MAKE_DISPOSAL = SUPPORT_TRIP_WEBSERVICE_PATH + "makeDisposal";
    //    public static final String PASSENGER_INFO = SUPPORT_WEBSERVICE_PATH + "passengerInfo";
    public static final String SERVICE_DETAIL = SUPPORT_WEBSERVICE_PATH + "serviceDetail";
    public static final String LAST_DRIVER_POSITION = SUPPORT_WEBSERVICE_PATH + "lastDriverPosition";
    public static final String INSERT_DRIVER_COMPLAINT = SUPPORT_WEBSERVICE_PATH + "insertComplaint";
    public static final String INSERT_PASSENGER_COMPLAINT = SUPPORT_WEBSERVICE_PATH + "complaint/customer";
    public static final String INSERT_LOST_OBJECT = SUPPORT_WEBSERVICE_PATH + "insertLostObject";
    public static final String LOCK_TAXI = SUPPORT_WEBSERVICE_PATH + "lockTaxi";
    public static final String AGAIN_TRACKING = SUPPORT_WEBSERVICE_PATH + "againTracking";
    public static final String EDIT_ADDRESS = SUPPORT_WEBSERVICE_PATH + "editAddress";
    public static final String LISTEN = SUPPORT_WEBSERVICE_PATH + "listen";
    public static final String V1_LISTEN = SUPPORT_WEBSERVICE_PATH + "v2/listen";
    public static final String V2_LISTEN = SUPPORT_WEBSERVICE_PATH + "v3/listen";
    public static final String ACCEPT_LISTEN = LISTEN_WEBSERVICE_PATH + "accept";
    public static final String ACTIVATE_SUPPORT = SUPPORT_WEBSERVICE_PATH + "activate";
    public static final String DEACTIVATE_SUPPORT = SUPPORT_WEBSERVICE_PATH + "deActivate";
    public static final String RECENT_CALLS = CALL_VOICE_PATH + "report/"; //http://turbotaxi.ir:1884/api/v1/report/09376148583/4
    public static final String MISSED_CALL = LISTEN_WEBSERVICE_PATH + "missCall";
    public static final String REWARD_FOR_TRIP = SUPPORT_TRIP_WEBSERVICE_PATH + "reward";

    /******************************** Driver Api *********************************/

    public static final String DRIVER_INFO = DRIVER_WEBSERVICE_PATH + "info";
    public static final String DRIVER_FINANCIAL = DRIVER_WEBSERVICE_PATH + "financial";
    public static final String DRIVER_STATION_REGISTRATION = DRIVER_STATION_WEBSERVICE_PATH + "registration";
    public static final String DRIVER_STATION_POSITION = DRIVER_STATION_WEBSERVICE_PATH + "position";
    public static final String DRIVER_SEND_APP_LINK = DRIVER_WEBSERVICE_PATH + "SendAppLink";
    public static final String DRIVER_EDIT_FINANCIAL = DRIVER_WEBSERVICE_PATH + "financial/correction";

    /******************************** refresh token Api *********************************/

    public static final String REFRESH_TOKEN = HAWKEYE_PATH + "token";
    public static final String LOGIN = HAWKEYE_PATH + "login";
    public static final String VERIFICATION = HAWKEYE_LOGIN_PATH + "verification";
    public static final String CHECK = HAWKEYE_LOGIN_PATH + "check";

    /******************************** complaint Api *********************************/

    public static final String COMPLAINT_DETAIL = COMPLAINT_WEBSERVICE_PATH + "detail/";// :complaintId
    public static final String COMPLAINT_ACCEPT = COMPLAINT_WEBSERVICE_PATH + "accept";
    public static final String COMPLAINT_UPDATE_STATUS = COMPLAINT_WEBSERVICE_PATH + "updateStatus";
    public static final String COMPLAINT_MISSED_CALL = COMPLAINT_WEBSERVICE_PATH + "misscall";
    public static final String COMPLAINT_FINISH = COMPLAINT_WEBSERVICE_PATH + "v1/finish";
    public static final String COMPLAINT_DRIVER_HISTORY = COMPLAINT_WEBSERVICE_PATH + "complaintsOfDriver/";//:driverCode
    public static final String COMPLAINT_CUSTOMER_HISTORY = COMPLAINT_WEBSERVICE_PATH + "complaintsOfCustomer/";//:phoneNumber
    public static final String COMPLAINT_GET_PRICE = TRIP_WEBSERVICE_PATH + "price/";//:cityCode/:carClass/:origin/:destination/:time


    /******************************** OperatorMistakeAPI ******************************/
    public static final String GET_OPERATOR_MISTAKE = OPERATOR_MISTAKE_WEBSERVICE_PATH;
    public static final String GET_REASON_OPERATOR_MISTAKE = OPERATOR_MISTAKE_WEBSERVICE_PATH + "/reasons";
    public static final String RECHECK_OPERATOR_MISTAKE = OPERATOR_MISTAKE_WEBSERVICE_PATH + "/review";

}