package BasicUtils;

import LogUtils.LibraryLoggingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * CommonMethods class contains the methods for basic java operations and function which are helpful to testers ( this class can be modiifed as per project requirement )
 */
public class CommonMethods {
    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();
    /**
     * getCurrentTimeStamp method is used to get the date and timestamp in a user defined format.
     * @param timeStampFormat This is the user defined format ( Eg: yyyy-MM-dd ).
     * @param timeZone This is the optional argument for timeZone ( Eg: UTC ).
     * @return  formattedTimeStamp This is the formatted date_timestamp as per the user defined format.
     */
    public synchronized String getCurrentTimeStamp(String timeStampFormat,String... timeZone ) // tested OK
    {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String formattedTimeStamp=null;
        Date date = new Date();
        try {
            if(timeZone.length>0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStampFormat);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone[0]));
                formattedTimeStamp = simpleDateFormat.format(date);
            }else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStampFormat);
                formattedTimeStamp = simpleDateFormat.format(date);
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return formattedTimeStamp;
    }
    /**
     * getCurrentTimeStamp method is used to get the date and timestamp in a user defined format.
     * @param timeStampFormat This is the user defined format ( Eg: yyyy-MM-dd HH:mm:ss ).
     * @param numberOfDaysToBeAltered This is the number of days which needs to be added or subtracted ( + value for future date and negative valuie for back dated date)
     * @param userProvidedStartDate This is the optional date for subtracting/adding days from/to ( Eg: 2024-08-08 ).
     * @return formattedTimeStamp This is the formatted timestamp as per the user defined format.
     */
    public synchronized String getFutureTimeStamp(String timeStampFormat,int numberOfDaysToBeAltered,String... userProvidedStartDate) // tested OK
    {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String formattedTimeStamp=null;
        try {
            if (userProvidedStartDate.length == 0) {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysToBeAltered);
                Date futureDate = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStampFormat);
                formattedTimeStamp = simpleDateFormat.format(futureDate);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(userProvidedStartDate[0]);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysToBeAltered);

                // Get current time
                Calendar currentTime = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, currentTime.get(Calendar.SECOND));

                Date newDate = calendar.getTime();

                SimpleDateFormat outputSdf = new SimpleDateFormat(timeStampFormat);
                formattedTimeStamp = outputSdf.format(newDate);
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
        }
        return formattedTimeStamp;
    }
    /**
     * generateRandomGuid method is used to generate a random HEXADECIMAL string.
     * @return guid This is the HEXADECIMAL string.
     */
    public synchronized String generateRandomGuid() // tested OK
    {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String guid = null;
        try {
            UUID uuid = java.util.UUID.randomUUID();
            guid = uuid.toString();
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
        }

        return guid;
    }


}
