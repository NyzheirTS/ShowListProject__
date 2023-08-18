package com.warnercloud.showlistproject__;

import com.warnercloud.showlistproject__.showlists.watchedShows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.warnercloud.showlistproject__.showlists.wantToWatch;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {




    //General Variables.
    public AnchorPane pane;
    private Stage stage;
    private Parent root;

    //sql database connection object
    SQLConnect myconnect = new SQLConnect();


    //Variables needd for the WatchedShowes table.
    public Tab allShows;
    public TableView<watchedShows> watchedShowsTable = new TableView<>();
    public TableColumn<watchedShows, Integer> tableNumber = new TableColumn<>();
    public TableColumn<watchedShows, String> showName = new TableColumn<>();
    public TableColumn<watchedShows, Integer> showScore = new TableColumn<>();
    public TableColumn<watchedShows, String> showType = new TableColumn<>();
    public TableColumn<watchedShows, Integer> numberEpisodes = new TableColumn<>();
    public TextField showNameTXTField;
    public TextField numberOfEpisodesField;
    public Button addToListButton;
    public Button cancelListButton;
    public ChoiceBox<String> showTypeChoiceBOX = new ChoiceBox<>();
    private String[] showTypeChoice = {"Movie","TV","Anime"};
    private Integer[] showScoreChoice = {1,2,3,4,5,6,7,8,9,10};
    public ChoiceBox<Integer> scoreChoiceBox = new ChoiceBox<>();



    //Variables needed for the WantToWatch Table.
    public Tab wantToWatch;
    public TableView<wantToWatch> wantToWatchTable = new TableView<>();
    public TableColumn<wantToWatch, Integer> tabelNumber_W = new TableColumn<>();
    public TableColumn<wantToWatch, String> showName_W = new TableColumn<>();
    public TableColumn<wantToWatch, String> showType_W = new TableColumn<>();
    public TableColumn<wantToWatch, Integer> numberEpisodes_W = new TableColumn<>();
    public TableColumn<wantToWatch, String> genre_W = new TableColumn<>();
    public TextField showNameTXTField_W;
    public TextField numberOfEpisodes_W;
    public Button cancelListButton_W;
    public Button addToListButton_W;
    public TextField genreTxtBox_W;
    public ChoiceBox<String> showTypeChoiceBOX_W = new ChoiceBox<>();
    private String[] showTypeChoice_w = {"Movie","TV","Anime"};


    //Interaction Buttons and Search Field
    public TextField filterTxtField = new TextField();
    public Button addEntryButton;

    ///Test Code Area///









    //Refresh Button
    public Button refreshButton;
    public void refreshTable(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main_FXML.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    //Switchs button to add an entrys to table and cancle entry.
    public void addEntrySwitch(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addEntryPopUp.fxml"));
        root = fxmlLoader.load();
        stage = new Stage();
        stage.setTitle("Add Entry");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void cancelEntry(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void confirmEntry(ActionEvent event) throws IOException {
        String show_name = showNameTXTField.getText();
        String show_type = showTypeChoiceBOX.getValue();
        Integer show_score = scoreChoiceBox.getValue();
        Integer number_episodes = Integer.parseInt(numberOfEpisodesField.getText());

        myconnect.dataEntry(show_name,show_type,show_score,number_episodes);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void confirmEntry_W(ActionEvent event) {
        String show_name_w = showNameTXTField_W.getText();
        String show_type_w = showTypeChoiceBOX_W.getValue();
        Integer show_episodes_w = Integer.parseInt(numberOfEpisodes_W.getText());
        String show_genre_w = genreTxtBox_W.getText();

        myconnect.dataEntry_W(show_name_w, show_type_w, show_episodes_w, show_genre_w);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        showTypeChoiceBOX.getItems().addAll(showTypeChoice);
        scoreChoiceBox.getItems().addAll(showScoreChoice);
        showTypeChoiceBOX_W.getItems().addAll(showTypeChoice_w);

        addColum();
        DB_CONNECT();
        DB_CONNECT_W();

        //Search Filter
        FilteredList<watchedShows> filterData = new FilteredList<>(data, b -> true);
        FilteredList<wantToWatch> filterData_W = new FilteredList<>(data_W, f -> true);
        filterTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            //uses a predicate to determine compare input and determine whether true or false. b
            filterData.setPredicate(Shows -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                //cast toLower for easy comparison
                String lowerCaseFilter = newValue.toLowerCase();
                if (Shows.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter match to name
                }
                else if(Shows.getType().toLowerCase().contains(lowerCaseFilter)){
                    return true; // filter match to type
                }
                else if(String.valueOf(Shows.getScore()).contains(lowerCaseFilter)) {
                    return true; // filter match to score
                }
                else
                    return false;
            });
            filterData_W.setPredicate(wantToWatch -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                //cast toLower for easy comparison
                String lowerCaseFilter_w = newValue.toLowerCase();
                if (wantToWatch.getName_w().toLowerCase().contains(lowerCaseFilter_w)) {
                    return true; // filter match to name
                }
                else if(wantToWatch.getType_w().toLowerCase().contains(lowerCaseFilter_w)){
                    return true; // filter match to type
                }
                else if(wantToWatch.getGenre_w().toLowerCase().contains(lowerCaseFilter_w)) {
                    return true; // filter match to genre
                }
                else
                    return false;
            });
        });
        // new sorted list base upon inputed txt field.
        SortedList<watchedShows> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(watchedShowsTable.comparatorProperty());
        watchedShowsTable.setItems(sortedData);

        SortedList<wantToWatch> sortedData_W = new SortedList<>(filterData_W);
        sortedData_W.comparatorProperty().bind(wantToWatchTable.comparatorProperty());
        wantToWatchTable.setItems(sortedData_W);


    }

    //Adds data to the table
    public void addColum(){
        tableNumber.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        showName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        showScore.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        showType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        numberEpisodes.setCellValueFactory(cellData -> cellData.getValue().episodesProperty().asObject());

        tabelNumber_W.setCellValueFactory((cellData -> cellData.getValue().id_wProperty().asObject()));
        showName_W.setCellValueFactory(cellData -> cellData.getValue().name_wProperty());
        showType_W.setCellValueFactory(cellData -> cellData.getValue().type_wProperty());
        numberEpisodes_W.setCellValueFactory(cellData -> cellData.getValue().episodes_wProperty().asObject());
        genre_W.setCellValueFactory(cellData -> cellData.getValue().genre_wProperty());
    }



    //saves data from DB to an observable array list to be put on table.
    ObservableList<watchedShows> data = FXCollections.observableArrayList();
    ObservableList<wantToWatch> data_W = FXCollections.observableArrayList();
    public watchedShows DB_CONNECT(){

        try {
            Connection connection = myconnect.getDatabaseLink();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM showlistdatabase.mylist_table");

            while(resultSet.next()){
                Integer id =  resultSet.getInt("id");
                String name = resultSet.getString("show_name");
                String type = resultSet.getString("show_type");
                Integer score = resultSet.getInt("show_score");
                Integer episodes = resultSet.getInt("number_episodes");
                data.add(new watchedShows(id, name, type, score, episodes));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public wantToWatch DB_CONNECT_W(){
        try {
            Connection connection_w = myconnect.getDatabaseLink();
            Statement statement_w = connection_w.createStatement();
            ResultSet resultSet_w = statement_w.executeQuery("SELECT * FROM showlistdatabase.wtw_table");

            while(resultSet_w.next()){
                Integer id =  resultSet_w.getInt("id");
                String name = resultSet_w.getString("name");
                String type = resultSet_w.getString("type");
                Integer episodes = resultSet_w.getInt("number_episodes_w");
                String genre = resultSet_w.getString("genre");
                data_W.add(new wantToWatch(id, name, type, episodes, genre));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

}