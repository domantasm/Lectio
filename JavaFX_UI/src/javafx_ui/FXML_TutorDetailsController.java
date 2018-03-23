/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx_ui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author filippopiggici
 */
public class FXML_TutorDetailsController implements Initializable {

    @FXML
    private ComboBox<String> comboBox1;

    @FXML
    private ComboBox<String> comboBox2;

    @FXML
    private Button register;

    @FXML
    private TextField textfield;

    @FXML
    private TextArea textarea;

    @FXML
    private Label invalid_label;

    ObservableList<String> list = FXCollections.observableArrayList("Camden", "Greenwich", "Hackney", "Hammersmith", "Islington", "Kensington and Chelsea", "Lambeth", "Lewisham", "Southwark", "Tower Hamlets", "Wandsworth", "Westminster", "Barking", "Barnet", "Bexley", "Brent", "Bromley", "Croydon", "Ealing", "Enfield", "Haringey", "Harrow", "Havering", "Hillingdon", "Hounslow", "Kingston upon Thames", "Merton", "Newham", "Redbridge", "Richmond upon Thames", "Sutton", "Waltham Forest");

    ;
    
    ObservableList<String> list2 = FXCollections.observableArrayList("Art", "Biology", "Business Studies", "Chemistry", "Design and Technology", "Drama", "English Language", "English Literature", "Geography", "History", "ICT", "Latin", "Law", "Maths", "Media Studies", "Music", "Physics", "Psychology", "Science", "Sociology", "Statistics");

    ;
    
    
    
    
    /**
     * Initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboBox1.setItems(list);
        comboBox2.setItems(list2);

        //comboBox2.getItems().addAll("Math", "History");
    }

    public void registrationClicked(MouseEvent event) throws IOException {

        if (isValid()) {
            Parent homepage_parent = FXMLLoader.load(getClass().getResource("FXML_Homepage.fxml"));
            Scene homepage_scene = new Scene(homepage_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide();
            app_stage.setScene(homepage_scene);
            app_stage.show();
        } else {

            invalid_label.setText("Select every field please");
        }
    }

    public boolean isValid() {

        if (comboBox1.getSelectionModel().isEmpty()) {
            return false;
        } else {

            Connection c = null;
            java.sql.Statement stmt = null;

            try {
                c = DriverManager.getConnection("jdbc:sqlite:users.db");

                System.out.println("Opened database succesfully");
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT ID FROM Users ORDER BY ID DESC LIMIT 1");
                int id = rs.getInt(1);
                String sql = "INSERT INTO TUTOR (ID, BOROUGH, SUBJECT, Price, details) VALUES (?,?,?,?,?)";
                try (
                        PreparedStatement pstmt = c.prepareStatement(sql)) {
                    //System.out.println("inserting");
                    pstmt.setInt(1, id);
                    // System.out.println("inserting1");
                    pstmt.setString(2, comboBox1.getValue());
                    //System.out.println("inserting2");

                    pstmt.setString(3, comboBox2.getValue());
                    //System.out.println("inserting3");
                    pstmt.setString(4, textfield.getText());
                    // System.out.println("inserting4");
                    pstmt.setString(5, textarea.getText());
                    //System.out.println("inserting5");
                    pstmt.executeUpdate();
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("data inside tutor inserted");

                rs.close();
                stmt.close();

                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("Operation done succesfully");

        }
        return true;
    }

}
