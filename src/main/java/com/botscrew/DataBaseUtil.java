package com.botscrew;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Created by Michael Rudyy on 19-Nov-17.
 */

/**
 * Data Base Util
 * Provide connection to DataBase
 *
 * @author Michael Rudyy
 * @version 1.0
 */
public class DataBaseUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutDown() {
        getSessionFactory().close();
    }
}
