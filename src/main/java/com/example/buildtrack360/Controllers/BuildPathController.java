package com.example.buildtrack360.Controllers;

import com.example.buildtrack360.Database.LoadDatabase;
import com.example.buildtrack360.Project.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class BuildPathController {

    @FXML
    VBox NewVBox;
    @FXML
    VBox BiddedVBox;
    @FXML
    VBox QualifiedVBox;
    @FXML
    VBox ProposedVBox;
    @FXML
    VBox NegotiationsVBox;
    @FXML
    VBox InProgressVBox;
    @FXML
    VBox CompletedVBox;
    @FXML
    VBox SupportVBox;
    @FXML
    Label NewAmountLabel;
    @FXML
    Label BiddedAmountLabel;
    @FXML
    Label ProposedAmountLabel;
    @FXML
    Label InProgressAmountLabel;
    @FXML
    Label CompletedAmountLabel;
    @FXML
    Label SupportAmountLabel;

    private int NewAmount=0,BiddedAmount=0,ProposedAmount=0,InProgressAmount=0, CompletedAmount=0,SupportAmount=0;

    @FXML
    void initialize(){
        LoadDatabase Load=new LoadDatabase();
        Load.LoadProject();
        Load.ProjectsList.tempNode=Load.ProjectsList.GetHead();

        while(Load.ProjectsList.tempNode!=null) {

            String Amount = String.valueOf(Load.ProjectsList.tempNode.data.getAmount());
            String StageName="Not Getting StageName";
            if(Load.ProjectsList.tempNode.data.getStageName()!=null||Load.ProjectsList.tempNode.data.getStageName()!="") {
                StageName = Load.ProjectsList.tempNode.data.getStageName();
            }

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(200, 80); // Set size for the AnchorPane
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(224, 214, 180), null, null);
            Background background = new Background(backgroundFill);
            anchorPane.setBackground(background);

            // Create a Label
            Label Namelabel = new Label();
            Namelabel.setStyle("-fx-font-size: 13px;");
            Namelabel.setFont(Font.font("System", FontWeight.BOLD, 13));
            Namelabel.setText(Load.ProjectsList.tempNode.data.getName());
            AnchorPane.setTopAnchor(Namelabel, 5.0); // Positioning inside the AnchorPane
            AnchorPane.setLeftAnchor(Namelabel, 10.0);


            // Create a Label
            Label AmountLabel = new Label();
            AmountLabel.setText(Amount);
            AnchorPane.setTopAnchor(AmountLabel, 23.0); // Positioning inside the AnchorPane
            AnchorPane.setRightAnchor(AmountLabel, 10.0);

            // Create a Label
            Label CustomerLabel = new Label();
            CustomerLabel.setText(Load.ProjectsList.tempNode.data.getCustomerName());
            AnchorPane.setTopAnchor(CustomerLabel, 23.0 + 18.0); // Positioning inside the AnchorPane
            AnchorPane.setLeftAnchor(CustomerLabel, 15.0);

            // Create a Label
            Label PriorityLabel = new Label();
            PriorityLabel.setText(Load.ProjectsList.tempNode.data.getCustomerPriority());
            AnchorPane.setTopAnchor(PriorityLabel, 23.0 + 18.0 + 18.0); // Positioning inside the AnchorPane
            AnchorPane.setRightAnchor(PriorityLabel, 10.0);

            //Set Complete Percentage for Inprogress
            if(Load.ProjectsList.tempNode.data.getStageName().equals("InProgress")){
                Label CompletePercentLabel=new Label();
                CompletePercentLabel.setText(String.valueOf(Load.ProjectsList.tempNode.data.getCompletePercent())+"%");
                AnchorPane.setTopAnchor(CompletePercentLabel, 23.0 + 18.0 + 18.0); // Positioning inside the AnchorPane
                AnchorPane.setLeftAnchor(CompletePercentLabel, 10.0);
                anchorPane.getChildren().add(CompletePercentLabel);
            }

            // Add Label to the AnchorPane
            anchorPane.getChildren().add(Namelabel);
            anchorPane.getChildren().add(AmountLabel);
            anchorPane.getChildren().add(CustomerLabel);
            anchorPane.getChildren().add(PriorityLabel);


            if(Load.ProjectsList.tempNode.data.getStage()==1) {
                // Add AnchorPane to the VBox
                NewVBox.getChildren().add(anchorPane);
                NewVBox.setAlignment(Pos.TOP_CENTER);
                NewAmount+=Load.ProjectsList.tempNode.data.getAmount();
            } else if (Load.ProjectsList.tempNode.data.getStage()==2) {
                BiddedVBox.getChildren().add(anchorPane);
                BiddedVBox.setAlignment(Pos.TOP_CENTER);
                BiddedAmount+=Load.ProjectsList.tempNode.data.getAmount();

            } else if (Load.ProjectsList.tempNode.data.getStage()==3) {
                ProposedVBox.getChildren().add(anchorPane);
                ProposedVBox.setAlignment(Pos.TOP_CENTER);
                ProposedAmount+=Load.ProjectsList.tempNode.data.getAmount();
            }
             else if (Load.ProjectsList.tempNode.data.getStage()==4) {
                InProgressVBox.getChildren().add(anchorPane);
                InProgressVBox.setAlignment(Pos.TOP_CENTER);
                InProgressAmount+=Load.ProjectsList.tempNode.data.getAmount();
            } else if (Load.ProjectsList.tempNode.data.getStage()==5) {
                CompletedVBox.getChildren().add(anchorPane);
                CompletedVBox.setAlignment(Pos.TOP_CENTER);
                CompletedAmount+=Load.ProjectsList.tempNode.data.getAmount();
            } else if (Load.ProjectsList.tempNode.data.getStage()==6) {
                SupportVBox.getChildren().add(anchorPane);
                SupportVBox.setAlignment(Pos.TOP_CENTER);
                SupportAmount+=Load.ProjectsList.tempNode.data.getAmount();
            }
            anchorPane.setTranslateY(1.0);
            // Optional: Add styling or additional properties
            anchorPane.setStyle(" -fx-border-color: #2d3436; -fx-border-width: 1;");

            //setting Userdata to get it in mouseclick
            anchorPane.setUserData(Load.ProjectsList.tempNode.data);

            // On CLick Loaders
            anchorPane.setOnMouseClicked(this::handleMouseClick);
            Load.ProjectsList.tempNode=Load.ProjectsList.tempNode.next;
        }
        updateAmountLabels();
    }

    private void updateAmountLabels(){
        NewAmountLabel.setText(String.valueOf(NewAmount));
        BiddedAmountLabel.setText(String.valueOf(BiddedAmount));
        ProposedAmountLabel.setText(String.valueOf(ProposedAmount));
        InProgressAmountLabel.setText(String.valueOf(InProgressAmount));
        CompletedAmountLabel.setText(String.valueOf(CompletedAmount));
        SupportAmountLabel.setText(String.valueOf(SupportAmount));
    }


    private void handleMouseClick(MouseEvent mouseEvent) {
        AnchorPane clickedPane = (AnchorPane) mouseEvent.getSource();
        Project clickedProject = (Project) clickedPane.getUserData(); // Retrieve the project data

        if (clickedProject == null) {
            System.out.println("Error: Project is null!");
            return; // Exit if no project data is found
        }
        else {
            // Show the project details in a new scene
            changeSceneToProjectDetails(clickedProject);
        }

    }
    private void changeSceneToProjectDetails(Project project) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/ViewandEditProject.fxml"));
            Parent root = loader.load();

            ViewandEditProjectController controller = loader.getController();

            controller.setProject(project);

            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) NewAmountLabel.getScene().getWindow();
            currentStage.close();
            stage.setMaximized(true);  // Maximize the window
            stage.setResizable(false);  // Make the window non-resizable
            // Set the new scene
            stage.setTitle("BuildPath360");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file.");
        }
    }

    public void handlenewproject(ActionEvent actionEvent) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/AddProject.fxml"));
            Parent root = loader.load();
            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) NewAmountLabel.getScene().getWindow();
            currentStage.close();
            stage.setMaximized(true);  // Maximize the window
            stage.setResizable(false);  // Make the window non-resizable
            // Set the new scene
            stage.setTitle("BuildPath360");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file.");
        }
    }

    public void handledashboardbutton(ActionEvent actionEvent) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/buildtrack360/Dashboard.fxml"));
            Parent root = loader.load();
            // Get the current stage
            Stage stage = new Stage();
            Stage currentStage = (Stage) NewAmountLabel.getScene().getWindow();
            currentStage.close();
            stage.setMaximized(true);  // Maximize the window
            stage.setResizable(false);  // Make the window non-resizable
            // Set the new scene
            stage.setTitle("BuildPath360");
            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file.");
        }
    }


}
