package codingpark.net.bunnylogger;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by ethanshan on 14-9-23.
 * A log wrapper, support convenient function
 *  1. Store log to local file system
 *  2. Use android Log or customer Log free switch
 * Java log map Android log
 * Log.d
 *  ASSERT      -->     Level.FINEST
 *	DEBUG       -->     Level.FINE
 *  ERROR       -->     Level.SEVERE
 *	INFO        -->     Level.INFO
 *  VERBOSE     -->     Level.FINNER
 *	WARN        -->     Level.WARNING
 */
public class BunnyLog {

    private static BunnyLog instance        = null;
    private static final String LOG_DIR     = "bunnylogger";
    private static String log_path          = "";
    private static String log_file_ext      = ".log";
    private static Context mContext         = null;
    private static final String TAG         = "BunnyLog";

    public static final int FUNC_ANDROID_LOG    = 0;
    public static final int FUNC_LOCAL_LOG      = 1;
    // TODO future need support transfer log to server
    public static final int FUNC_NETWORK_LOG    = 2;
    // Configure which log function to use, default use Android log
    // TODO The configuration need read from configure file
    //public static int FUNCTION                  = FUNC_ANDROID_LOG;
    public static int FUNCTION                  = FUNC_LOCAL_LOG;

    private static FileHandler fileTxt          = null;
    private static SimpleFormatter formatterTxt = null;
    private static Logger log                   = null;


    private BunnyLog() {
        if (FUNCTION == FUNC_LOCAL_LOG) {
            // Create log file
            // Exp: /data/data/codingpark.net.bunnylogger/files/bunnylogger/2014-09-23-11-27-59.log
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            File dir_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            dir_file.mkdirs();
            log_path = dir_file.getPath() + File.separator
                    + DateFormat.format("yyyy-MM-dd-hh-mm-ss", cal)
                    + log_file_ext;
            Log.d(TAG, "Store Path: " + log_path);
            File log_file = new File(log_path);
            if (!log_file.exists()) {
                try {
                    log_file.createNewFile();
                    setup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setup() throws IOException {
        Log.d(TAG, "getLogger");
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.setLevel(Level.FINE);
        fileTxt = new FileHandler(log_path);

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
        log = logger;
    }



    public static BunnyLog getInstance() {
        if (instance == null) {
            instance = new BunnyLog();
        }
        return instance;
    }

    public static BunnyLog getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new BunnyLog();
        }
        return instance;
    }

    public void d(Class c, String msg) {
        switch (FUNCTION) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                log.config(cal.getTime().toString() + " -->--> "
                        + c.getName() + " -->--> " + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }


    public void i(Class c, String msg) {
        switch (FUNCTION) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                log.info(cal.getTime().toString() + " -->--> "
                        + c.getName() + " -->--> " + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

    public void e(Class c, String msg) {
        switch (FUNCTION) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                log.severe(cal.getTime().toString() + " -->--> "
                        + c.getName() + " -->--> " + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

    public void w(Class c, String msg) {

        switch (FUNCTION) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                log.warning(cal.getTime().toString() + " -->--> "
                        + c.getName() + " -->--> " + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

}
