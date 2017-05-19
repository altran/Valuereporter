package org.valuereporter.helper;

import java.util.Properties;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class DatabaseHelperTest {

    public static void main(String[] args) {
        EmbeddedDatabaseHelper databaseHelper = new EmbeddedDatabaseHelper(new Properties());
        databaseHelper.initializeDatabase();
    }
}
