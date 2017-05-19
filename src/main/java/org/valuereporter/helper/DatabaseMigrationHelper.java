package org.valuereporter.helper;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.valuereporter.ValuereporterException;
import org.valuereporter.ValuereporterTechnicalException;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Delegate for Main for performing database migrations before application startup.
 */
public class DatabaseMigrationHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigrationHelper.class);
    private Flyway flyway;

    public DatabaseMigrationHelper(Properties resources) {
        try {
            DataSource masterDs = createMasterDataSource(resources);
            flyway = new Flyway();
            flyway.setValidateOnMigrate(false);
            flyway.setDataSource(masterDs);
        } catch (Exception e) {
            ValuereporterTechnicalException tte = new ValuereporterTechnicalException("Error migrating the database. Please verify properties are in place " +
                    ", admin.connection.username, admin.connection.password,admin.connection.host, admin.connection.port, admin.connection.databasename", e, StatusType.RETRY_NOT_POSSIBLE);
            Marker fatal = MarkerFactory.getMarker("FATAL");
            logger.error(fatal, "No-Recoverable Error: " + tte.getMessage(), tte);
            throw tte;
        }
    }

    public void upgradeDatabase() {
        try {
            Properties configuration = new Properties();
            configuration.setProperty("flyway.validateOnMigrate", "false");
            flyway.configure(configuration);
            flyway.migrate();
        } catch (FlywayException e) {
            throw new ValuereporterTechnicalException("Database upgrade failed.", e,StatusType.RETRY_NOT_POSSIBLE);
        }
    }

    public void cleanDatabase() {
        try {
            flyway.clean();
        } catch (FlywayException e) {
            throw new RuntimeException("Database upgrade failed.", e);
        }
    }

    /**
     * Create datasource for the admin connection used by the database upgrade framework Flyway.
     *
     * The JtdsDataSource API does not support standard JDBCs URIs directly, so we require host, port and database name to be set in persistence.properties.
     * @return  a new DataSource
     * @param resources
     */
    private DataSource createMasterDataSource(Properties resources) throws ValuereporterException {


        if (resources == null ) {
            logger.error("Missing properties for database migration, can not start the application.");
            throw new ValuereporterTechnicalException("No properties for database migration are found. Look for properties like \"admin.connection.username\"", StatusType.RETRY_NOT_POSSIBLE );
        }

        String dbUserName = resources.getProperty("admin.connection.username");
        String dbUserPassword = resources.getProperty("admin.connection.password");

        String host = resources.getProperty("admin.connection.host");
        String portAsString = resources.getProperty("admin.connection.port");
        int port = Integer.parseInt(portAsString);
        String databasename = resources.getProperty("admin.connection.databasename");
        if (isNullOrEmpty(dbUserName) || isNullOrEmpty(dbUserPassword) || isNullOrEmpty(host) || isNullOrEmpty(databasename)) {
            StringBuilder strb = new StringBuilder("Master connection properties not set in persistence configuration.\n");
            strb.append("admin.connection.username=").append(dbUserName);
            strb.append(", ");
            strb.append("admin.connection.password=").append(dbUserPassword);
            strb.append(", ");
            strb.append("admin.connection.host=").append(host);
            strb.append(", ");
            strb.append("admin.connection.port=").append(portAsString);
            strb.append(", ");
            strb.append("admin.connection.databasename=").append(databasename);
            throw new IllegalStateException(strb.toString());
        }

        JtdsDataSource ds = new JtdsDataSource();
        ds.setUser(dbUserName);
        ds.setPassword(dbUserPassword);
        ds.setServerName(host);
        ds.setPortNumber(port);
        ds.setDatabaseName(databasename);
        return ds;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
