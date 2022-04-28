package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.protocol.x.ReusableOutputStream;

public class Util {

    static String ConnectionString = "jdbc:mysql://localhost:3306/";
    static String UserName = "root";
    static String Password = "root";
    static String DBName = "EnvironmentSimulation";

    public static boolean IsAlreadyHaveData(String systemName) throws Exception {
        boolean result = true;

        try {
            Connection sqlconn = DriverManager.getConnection(ConnectionString + DBName, UserName, Password);
            if (sqlconn != null) {
                Statement stat = sqlconn.createStatement();
                String Query = "Select Count(*) from LogDetails where SystemName='" + systemName + "'";
                ResultSet set = stat.executeQuery(Query);
                if (set != null) {
                    while (set.next()) {

                        int Count = set.getInt(1);
                        if (Count == 0) {
                            return false;
                        }
                    }
                }
                set.close();
                sqlconn.close();
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return result;
    }

    public static void AddLogDetails(LogDetail LogData) throws Exception {
        try {
            Connection sqlconn = DriverManager.getConnection(ConnectionString + DBName, UserName, Password);
            if (sqlconn != null) {
                Statement stat = sqlconn.createStatement();
                String Query = "Insert into LogDetails (SystemName,ActionName,ActionTime,AbsoluteTime) values ('"
                        + LogData.SystemName + "','" + LogData.ActionType + "','" + LogData.ActionDate + "','"
                        + LogData.ActionDate + "')";
                stat.executeUpdate(Query);
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public static void UpdateLogDetails(LogDetail LogData) throws Exception {
        try {
            Connection sqlconn = DriverManager.getConnection(ConnectionString + DBName, UserName, Password);
            if (sqlconn != null) {
                Statement stat = sqlconn.createStatement();
                String Query = "Update LogDetails set ActionName='" + LogData.ActionType + "',ActionTime='"
                        + LogData.ActionDate + "',AbsoluteTime='" + LogData.ActionDate + "' where SystemName='"
                        + LogData.SystemName + "'";
                stat.executeUpdate(Query);
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public static LocalDateTime GetLastUpdateDatetime(String SystemName) throws Exception {
        LocalDateTime lc = null;

        try {
            Connection sqlconn = DriverManager.getConnection(ConnectionString + DBName, UserName, Password);
            if (sqlconn != null) {
                Statement stat = sqlconn.createStatement();
                String Query = "Select ActionTime from LogDetails where SystemName='" + SystemName + "'";
                ResultSet set = stat.executeQuery(Query);
                if (set != null) {
                    while (set.next()) {

                        lc = set.getTimestamp(1).toLocalDateTime();
                    }
                }
                set.close();
                sqlconn.close();
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return lc;

    }

    public static LocalDateTime GetAbsoluteUpdateDatetime(String SystemName) throws Exception {
        LocalDateTime lc = null;

        try {
            Connection sqlconn = DriverManager.getConnection(ConnectionString + DBName, UserName, Password);
            if (sqlconn != null) {
                Statement stat = sqlconn.createStatement();
                String Query = "Select AbsoluteTime from LogDetails where SystemName='" + SystemName + "'";
                ResultSet set = stat.executeQuery(Query);
                if (set != null) {
                    while (set.next()) {

                        lc = LocalDateTime.parse(set.getString(1));
                    }
                }
                set.close();
                sqlconn.close();
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return lc;

    }

}
