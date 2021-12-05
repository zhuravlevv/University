import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Hospital {
    public Statement statement = null;

    public Hospital() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/ssp", "postgres", "postgres");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNote(String table, Object[] objects) {
        try {
            switch (table) {
                case "patients": {
                    statement.execute("INSERT INTO patients (first_name, second_name, diagnose) VALUES ('" + objects[0] + "', '" + objects[1] + "', '" + objects[2] + "');");
                    break;
                }
                case "records": {
                    statement.execute("INSERT INTO records (description, patient_id, record_date) VALUES ('" + objects[0] + "', '" + objects[1] + "', '" + objects[2] + "');");
                    break;
                }
            }
            System.out.println("Adding successfully");
        } catch (SQLException e) {
            System.out.println("Adding error :" + e.getMessage());
        }
    }

    public void removeNote(String table, Integer index) {
        try {
            switch (table) {
                case "patients": {
                    statement.execute("DELETE FROM patients WHERE  id=" + index + ";");
                    break;
                }
                case "records": {
                    statement.execute("DELETE FROM records WHERE  id=" + index + ";");
                    break;
                }
            }
            System.out.println("Deleting successfully");
        } catch (SQLException e) {
            System.out.println("Deleting error :" + e.getMessage());
        }
    }

    public void updateNote(String table, Object[] objects, Integer index) {
        try {
            switch (table) {
                case "patients": {
                    statement.execute("UPDATE patients SET first_name='" + objects[0] + "', second_name='" + objects[1] + "', diagnose='" + objects[2] + "' WHERE  id=" + index + ";");
                    break;
                }
                case "records": {
                    statement.execute("UPDATE records SET description='" + objects[0] + "', patient_id='" + objects[1] + "', record_date='" + objects[2] + "' WHERE  id=" + index + ";");
                    break;
                }
            }
            System.out.println("Updating successfully");
        } catch (SQLException e) {
            System.out.println("Updating error :" + e.getMessage());
        }
    }
}
