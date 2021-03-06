package RoomReservationSystem.security;

/**
 * A védelemmel kapsolatos konstansokat tartalmazó osztály
 *
 * @author Tomecz Patrik
 */
public class SecurityConstants {

    /**
     *
     */
    public static final String SECRET = "SecretKeyToGenJWTs";

    /**
     *
     */
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days

    /**
     *
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     *
     */
    public static final String HEADER_STRING = "Authorization";

    /**
     *
     */
    public static final String REGISTER_URL = "/api/user/createUser";

    /**
     *
     */
    public static final String EVENT_URLS = "/api/event/**";

    /**
     *
     */
    public static final String CHECK_USERNAME_URL = "/api/user/existsByUsername*";

    /**
     *
     */
    public static final String GET_EVENT_NAMES_URL = "/api/eventReservation/getNames";

    /**
     *
     */
    public static final String GET_USER_NAME_URL = "/api/user/getName*";

    /**
     *
     */
    public static final String GET_SUBJECT_NAME_URL = "/api/subject/getSubjectName*";

    /**
     *
     */
    public static final String GET_BUILDING_NAMES_URL = "/api/building/getNames";

    /**
     *
     */
    public static final String GET_SUBJECT_NAMES_URL = "/api/subject/getSubjectNames";

    /**
     *
     */
    public static final String GET_USER_NAMES_URL = "/api/user/getNames";

    /**
     *
     */
    public static final String GET_SEMESTER_NAMES_URL = "/api/semester/getSemesterNames";

    /**
     *
     */
    public static final String GET_CLASSROOM_NAMES_URL = "/api/classroom/getNamesByBuilding*";

    /**
     *
     */
    public static final String CLASS_RESERVATIONS_URL = "/api/classReservation/getAccepted";

    /**
     *
     */
    public static final String CLASS_RESERVATIONS_FIND_BY_ID_URL = "/api/classReservation/findById*";

    /**
     *
     */
    public static final String EVENT_RESERVATIONS_URL = "/api/eventReservation/getAccepted";

    /**
     * 
     */
    public static final String EVENT_RESERVATIONS_FIND_BY_ID_URL = "/api/eventReservation/findById*";

    /**
     * Frontend home:
     */
    public static final String HOME_URL = "/*";

    /**
     * Frontend assets:
     */
    public static final String ASSETS_URL = "/assets/**";
    
    /**
     * Frontend web:
     */
    public static final String WEB_URL = "/web/**";

    /**
     *
     */
    public static final String LOGIN_URL = "/api/user/login";
}
