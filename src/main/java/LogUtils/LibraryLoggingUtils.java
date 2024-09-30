package LogUtils;

import APIUtils.APIAuthentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * LibraryLoggingUtils class contains the methods for displaying different exceptions in a more meaningful way for each end every method within the entire library
 */
public class LibraryLoggingUtils {

    private static final Logger logger = LogManager.getLogger(APIAuthentication.class);
    /**
     * getCurrentMethodException method is used to display the exection message and actual exception thrown during execution of a method
     * @param className This is the name of the currently running class
     * @param methodName This is the name of the currently running method
     * @param actualException This is the actual exception caught and passed from the try-catch block of currently running method
     */
    public synchronized void getCurrentMethodException(String className,String methodName,Exception actualException,String... message) {
        if(message.length>0)
        {
            logger.error("Error encountered while running method: " + className + "." + methodName + ":" +message+" , Exception is as below :");
        }
        else
        {
            logger.error("Error encountered while running method: " + className + "." + methodName + " , Exception is as below :");
        }
        throw new RuntimeException(actualException);
    }
}
