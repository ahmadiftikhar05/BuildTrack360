package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Project.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;


public class ViewandEditProjectController {

    @FXML
    TextField NameTextField;
    @FXML
    TextField AmountTextField;
    @FXML
    Hyperlink AgreementHyperLink;
    @FXML
    ComboBox<String> CustomerCombobox;
    @FXML
    ComboBox<String> StageCombobox;

    private Project project;

    @FXML
    void initialize(){


    }

    void setProject(Project propproject){
        project=propproject;
        NameTextField.setText(propproject.getName());
        AmountTextField.setText(String.valueOf(propproject.getAmount()));
        AgreementHyperLink.setText(propproject.getAgreement());
       // String urlpath=propproject.getAgreement();
       // Path path= (Path) Paths.get(urlpath);
       // if (path != null ) {
       //     AgreementHyperLink.setOnAction(event -> openFile(path));
       // } else {
       //     AgreementHyperLink.setOnAction(event -> {
       //         System.out.println("No agreement file available.");
       //     });
      //  }
    }


    public void handlebackbutton(ActionEvent actionEvent) {
    }

    public void handleeditbutton(ActionEvent actionEvent) {
    }
}
