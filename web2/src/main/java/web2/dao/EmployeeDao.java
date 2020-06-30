package web2.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class EmployeeDao {

    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private final String dbUrl;
    private final String user;
    private final String pass;

    public EmployeeDao() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
        this.dbUrl = props.getProperty("db.url");
        this.user  = props.getProperty("db.user");
        this.pass  = props.getProperty("db.password");
    }

    public String getEmployees() {
        String names = "";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(dbUrl, user, pass);
            stmt = conn.createStatement();

            String sql = "SELECT id, first, last, age FROM Employee";
            ResultSet rs = stmt.executeQuery(sql);

            PreparedStatement stmt2 = conn.prepareStatement("UPDATE Employee SET last = ? WHERE id = ?");
            stmt2.setString(1, "Khan");
            stmt2.setLong(2, 100);
            int updated = stmt2.executeUpdate();

            while (rs.next()) {
                int id       = rs.getInt("id");
                int age      = rs.getInt("age");
                String first = rs.getString("first");
                String last  = rs.getString("last");
                names = first;

                System.out.println("ID: " + id + ", Age: " + age + ", First: " + first + ", Last: " + last);
            }

            rs.close();
            stmt2.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException se) { se.printStackTrace(); }
        }

        return names;
    }
}
