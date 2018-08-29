package com.bobocode.util;

import com.bobocode.exception.JdbcAccountUtilException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcAccountUtil {
    private final static String CREATE_ACCOUNT_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS account (\n" +
                    "  id            SERIAL NOT NULL,\n" +
                    "  first_name     VARCHAR(255) NOT NULL,\n" +
                    "  last_name     VARCHAR(255) NOT NULL,\n" +
                    "  email         VARCHAR(255) NOT NULL,\n" +
                    "  birthday      TIMESTAMP NOT NULL,\n" +
                    "  sex      SMALLINT NOT NULL DEFAULT 0 CHECK (sex >= 0 AND sex <=2) ,\n" +
                    "  balance       DECIMAL(19, 4),\n" +
                    "  creation_time TIMESTAMP NOT NULL DEFAULT now(),\n" +
                    "  CONSTRAINT account_pk PRIMARY KEY (id)\n" +
                    ");";

    public static void createAccountTable(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute(CREATE_ACCOUNT_TABLE_SQL);
        } catch (SQLException e) {
            throw new JdbcAccountUtilException("Cannot create account table", e);
        }
    }
}
