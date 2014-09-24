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
 *  3. Use network transfer log to remote server
 * Java log map Android log
 * Log.d
 *  ERROR       -->     Level.SEVERE
 *  WARN        -->     Level.WARNING
 *  DEBUG       -->     Level.CONFIG
 *  INFO        -->     Level.INFO
 *  VERBOSE     -->     Level.FINE
 *  ASSERT      -->
 */
public class BunnyLog {

    private static BunnyLog instance        = null;
    private static final String LOG_DIR     = "bunnylogger";
    private static String log_path          = "";
    private static String log_file_ext      = ".log";
    private static Context mContext         = null;
    private static final String TAG         = "BunnyLog";

    /**
     * Android internal logging mechanism
     */
    public static final int FUNC_ANDROID_LOG    = 0;
    /**
     * Java environment support logging mechanism,
     * Store log to local file system
     */
    public static final int FUNC_LOCAL_LOG      = 1;
    // TODO future need support transfer log to server
    /**
     * Java environment support logging mechanism,
     * transfer log to remote server.
     */
    public static final int FUNC_NETWORK_LOG    = 2;
    // Configure which log function to use, default use Android log
    // TODO The configuration need read from configure file
    private static int function                     = FUNC_LOCAL_LOG;

    private static FileHandler fileTxt          = null;
    private static SimpleFormatter formatterTxt = null;
    private static Logger log                   = null;


    private BunnyLog() {
        if (function == FUNC_LOCAL_LOG) {
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

    public static BunnyLog getInstance(Context context, int func) {
        mContext = context;
        function = func;
        if (instance == null) {
            instance = new BunnyLog();
        }
        return instance;
    }

    /**
     * Debug level info
     * @param c
     * @param msg
     */
    public void d(Class c, String msg) {
        switch (function) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                log.config(c.getName() + "\t\t\t" + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }


    /**
     * Info level info
     * @param c
     * @param msg
     */
    public void i(Class c, String msg) {
        switch (function) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                log.info(c.getName() + "\t\t\t" + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

    /**
     * Error level info
     * @param c
     * @param msg
     */
    public void e(Class c, String msg) {
        switch (function) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                log.severe(c.getName() + "\t\t\t" + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

    /**
     * Warning level info
     * @param c
     * @param msg
     */
    public void w(Class c, String msg) {
        switch (function) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                log.warning(c.getName() + "\t\t\t" + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

    /**
     * Verbose level info
     * @param c
     * @param msg
     */
    public void v(Class c, String msg) {
        switch (function) {
            case FUNC_ANDROID_LOG:
                Log.d(c.getName(), msg);
                break;
            case FUNC_LOCAL_LOG:
                log.fine(c.getName() + "\t\t\t" + msg);
                break;
            case FUNC_NETWORK_LOG:
                break;
            default:
                return;
        }
    }

}
