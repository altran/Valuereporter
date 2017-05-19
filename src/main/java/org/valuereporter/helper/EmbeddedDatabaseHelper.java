package org.valuereporter.helper;


import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valuereporter.ValuereporterException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Setting up the embedding database. Hsqldb.
 * See http://hsqldb.org/doc/guide/running-chapt.html#rgc_new_db
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class EmbeddedDatabaseHelper {
    private static final Logger log = LoggerFactory.getLogger(EmbeddedDatabaseHelper.class);
    private static final String USE_EMBEDDED_KEY = "jdbc.useEmbedded";
    private static final String DEFAULT_JDBC_URL = "jdbc:hsqldb:file:bootstrapdata/hsqldb/ValueReporter";
    private static final String JDBC_URL_KEY = "jdbc.url";
    private static final String DEFAULT_JDBC_USERNAME = "vr";
    private static final String JDBC_USERNAME_KEY = "jdbc.username";
    private static final String DEFAULT_JDBC_PASSWORD = "vr1234";
    private static final String JDBC_PASSWORD_KEY = "jdbc.password";
    private static final String DEFAULT_JDBC_ADMIN_USERNAME = "vrAdmin";
    private static final String JDBC_ADMIN_USERNAME_KEY = "admin.connection.username";
    private static final String DEFAULT_JDBC_ADMIN_PASSWORD = "vrAdmin1234";
    private static final String JDBC_ADMIN_PASSWORD_KEY = "admin.connection.password";
    private final QueryRunner queryRunner;
    private final String jdbcUrl;
    private final String jdbcUserName;
    private final String jdbcPassword;
    private final String jdbcAdminPassword;
    private boolean useEmbeddedDb = true;
    private String jdbcAdminUserName;

    public EmbeddedDatabaseHelper(Properties resources) {
        queryRunner = new QueryRunner();
        useEmbeddedDb = useEmbeddedDb(resources);
        log.info("Using embedded database {}", useEmbeddedDb);
        this.jdbcUrl = resources.getProperty(JDBC_URL_KEY, DEFAULT_JDBC_URL);
        this.jdbcUserName = resources.getProperty(JDBC_USERNAME_KEY, DEFAULT_JDBC_USERNAME);
        this.jdbcPassword = resources.getProperty(JDBC_PASSWORD_KEY, DEFAULT_JDBC_PASSWORD);
        this.jdbcAdminUserName = resources.getProperty(JDBC_ADMIN_USERNAME_KEY, DEFAULT_JDBC_ADMIN_USERNAME);
        this.jdbcAdminPassword = resources.getProperty(JDBC_ADMIN_PASSWORD_KEY, DEFAULT_JDBC_ADMIN_PASSWORD);

    }

    public void initializeDatabase() {
        if (useEmbeddedDb && isHSQLdbAvailable()) {
            log.info("Embedded Database is selected, and available at url {}", jdbcUrl);
            return;
        }

        if (useEmbeddedDb) {
            log.info("Creating new database at url {}", jdbcUrl);
            Connection connection = connectToHsqldb();
            createUsers(connection);
            createTables(connection);

        }

    }

    public static boolean useEmbeddedDb(Properties resources) {
        boolean useEmbedded = true;
        String useEmbeddedDbValue = resources.getProperty(USE_EMBEDDED_KEY);
        if (useEmbeddedDbValue != null && useEmbeddedDbValue.equalsIgnoreCase("false")) {
            useEmbedded =  false;
        }
        return useEmbedded;
    }

    public Connection connectToHsqldb() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
        } catch (SQLException e) {
            log.info("Database is not available {}, user {}", jdbcUrl, jdbcUserName);
        }
        return c;

    }

    public boolean isHSQLdbAvailable() {
        boolean isExistingDb = false;
        try {
            String hsqldbUrl = jdbcUrl + ";ifexists=true";
            log.info("Try to connect to existing database with url {}", hsqldbUrl);
            DriverManager.getConnection(hsqldbUrl, jdbcUserName, jdbcPassword);
            isExistingDb = true;
        } catch (SQLException e) {
            log.info("Database does not exits, or is not available to the user. It will have to be created. url {}, user {}.", jdbcUrl, jdbcUserName);
        }
        return isExistingDb;
    }

    protected void createUsers(Connection connection) {
        log.info("Creating Basic users.");
        try {
            queryRunner.update(connection,"CREATE USER " + jdbcUserName + " PASSWORD "+ jdbcPassword +";");
            queryRunner.update(connection,"CREATE USER " + jdbcAdminUserName + " PASSWORD "+ jdbcAdminPassword +";");
            queryRunner.update(connection,"GRANT DBA TO " + jdbcAdminUserName +";");
        } catch (SQLException e) {
            log.info("Error creating tables", e);
        }
    }

    protected void createTables(Connection connection) {
        log.info("Creating basic tables.");
        try {
            createObservedMethod(connection);
            createImplementedMethod(connection);
        } catch (SQLException e) {
            throw new ValuereporterException("Error creating tables. Intiialization fails", e, StatusType.RETRY_NOT_POSSIBLE);
        }
    }

    private void createObservedMethod(Connection connection) throws SQLException {
        String tableSql = ("CREATE TABLE ObservedMethod (" +
                "  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
                "  prefix varchar(255) NOT NULL," +
                "  methodName varchar(255) NOT NULL," +
                "  startTime TIMESTAMP ," +
                "  endTime TIMESTAMP ," +
                "  duration INTEGER" +
                "); ");


        queryRunner.update(connection,tableSql);
    }

    private void createImplementedMethod(Connection connection) throws SQLException {
        String tableSql = ("CREATE TABLE ImplementedMethod (" +
                "  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
                "  prefix varchar(255) NOT NULL," +
                "  methodName varchar(255) NOT NULL," +
                "  CONSTRAINT IMK_PrefixMethodName UNIQUE(prefix, methodName)"+
                "); ");


        queryRunner.update(connection,tableSql);
    }



    /*
    private BasicDataSource getDataSource() {
        String jdbcdriver = AppConfig.appConfig.getProperty("roledb.jdbc.driver");
        String jdbcurl = AppConfig.appConfig.getProperty("roledb.jdbc.url");
        String roledbuser = AppConfig.appConfig.getProperty("roledb.jdbc.user");
        String roledbpasswd = AppConfig.appConfig.getProperty("roledb.jdbc.password");

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(jdbcdriver);
        dataSource.setUrl(jdbcurl);//"jdbc:hsqldb:file:" + basepath + "hsqldb");
        dataSource.setUsername(roledbuser);
        dataSource.setPassword(roledbpasswd);
        return dataSource;
    }
    */
}
