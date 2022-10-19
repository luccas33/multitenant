package luccas33.multitenant.application;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date addDateField(Date date, int field, int qtt) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.add(field, qtt);
        return cld.getTime();
    }

    public static String md5(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

}
