package memberQueue;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TabletUI implements Initializable {


    final int PORT = 9090;
    final String HOSTNAME = "10.50.20.110";//"10.0.208.102";
    private ObjectOutputStream out;
    private InputStream is;
    private ObjectInputStream ois;
    private ArrayList<Member> memberList = new ArrayList<Member>();
    private ObservableList<Task> list = FXCollections.observableArrayList();
    private Font spireFont = Font.loadFont(getClass().getResourceAsStream("Roomy Wide Normal.ttf"), 30);
    private ObservableList<Member> data = FXCollections.observableArrayList();


    @FXML
    private Label upNextText;

    @FXML
    private TextField firstNameField;

    @FXML
    private TableView table;

    @FXML
    private ComboBox comboBox;

    @FXML
    private TextField initialField;

    @FXML
    private Label upNextMember;

    public TabletUI() throws IOException {
    }


    public void ButtonPressed(ActionEvent actionEvent) throws IOException, Exception {
        int curWaitTime=0;
        if ((comboBox.getValue() == null) || (comboBox.getValue().equals("Select a Service")) || (firstNameField.getText().equals("")) || (initialField.getText().equals(""))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter Information in All Fields");
            alert.showAndWait();
            return;
        }

        Task selectedTask = (Task) comboBox.getValue();

        //Accumulate current wait time
        for (int i = 0; i < memberList.size(); i++) {
            Member member = memberList.get(i);
            curWaitTime += member.getTaskTime();
        }


        //Create new member object with vars from fields
        Member curMember = new Member(firstNameField.getText() + " " + initialField.getText(), curWaitTime, selectedTask.getTaskName(), selectedTask.getTaskTime());


        memberList.add(curMember);
        initialField.setText("");
        firstNameField.setText("");
        comboBox.setValue("Select a Service");

        //send the list to the server
        Platform.runLater(() -> {
            try {
                out.writeObject(memberList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        list.add(new Task("Check Deposit", 3));
        list.add(new Task("Money Transfer", 5));
        list.add(new Task("Cash Withdrawal", 4));
        list.add(new Task("Auto Loan", 20));
        comboBox.setItems(list);
        comboBox.setPromptText("Select a Service");
        upNextText.setFont(spireFont);
        upNextMember.setFont(spireFont);

          if (!hostAvailabilityCheck()){
            new Thread(){
             public void run() {
                Server.main(new String[]{});}
              }.start();
        }

        TableColumn col1 = new TableColumn("Member Name");
        col1.setMinWidth(260);
        col1.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn col2 = new TableColumn("Wait Time (minutes)");
        col2.setMinWidth(150);
        col2.setCellValueFactory(
                new PropertyValueFactory<>("waitTime"));

        table.getColumns().addAll(col1,col2);

        table.setItems(data);




        //create new thread
        javafx.concurrent.Task task = new javafx.concurrent.Task<Void>() {
            @Override
            public Void call() throws ClassNotFoundException {
                try {
                    Socket s = new Socket(HOSTNAME, PORT);
                    out = new ObjectOutputStream(s.getOutputStream());
                    is = s.getInputStream();
                    ois = new ObjectInputStream(is);
                    while (true) {

                        //get list from server
                        memberList = (ArrayList<Member>) ois.readObject();
                        //refresh this UI
                        if (!memberList.isEmpty()) {
                            Platform.runLater(() ->
                                    refresh(memberList));

                        }
                        //System.out.println("from server "+memberList);
                    }
                } catch(IOException ioe) {
                    Member bad = new Member("Server error",0,"",0);
                    data.add(bad);
                }

                return null;
            }
        };
        Thread severIO = new Thread(task);
        severIO.setDaemon(true);
        severIO.start();
    }

    public  void refresh(ArrayList<Member> curList){
        upNextMember.setText(curList.get(0).getName());
        table.getItems().clear();

        for (int i = 0; i < curList.size(); i++) {
            data.add(curList.get(i));
        }
    }
    public boolean hostAvailabilityCheck() {
        try (Socket s = new Socket(HOSTNAME, PORT)) {
            return true;
        } catch (IOException ex) {
        }
        return false;
    }
}


