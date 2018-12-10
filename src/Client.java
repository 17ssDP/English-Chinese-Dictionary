import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Client extends Application{
    private File selectedFile;
    private int type = 0;
    private Dictionary dictionary = new Dictionary();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("English-Chinese Client");
        initFrame(primaryStage);
        primaryStage.show();
    }

    @FXML
    private void initFrame(Stage primaryStage) throws IOException {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        VBox vBox1 = new VBox();
        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(0,10,10,10));
        Label label = new Label("MANAGEMENT");
        VBox vBox3 = new VBox();
        TextField ttfFilePath = new TextField();
        ttfFilePath.setPrefHeight(30);
        GridPane gridPane1 = new GridPane();
        gridPane1.setVgap(10);
        gridPane1.setHgap(10);
        gridPane1.setPadding(new Insets(10,25,10,25));
        Button btBrowser = new Button("Browser");
        btBrowser.setPrefWidth(120);
        btBrowser.setPrefHeight(40);
        gridPane1.add(btBrowser,0,0);
        Button btSub = new Button("Submit");
        btSub.setPrefWidth(120);
        btSub.setPrefHeight(40);
        gridPane1.add(btSub,1,0);
        VBox.setMargin(vBox3,new Insets(10,0,0,0));
        GridPane gridPane5 = new GridPane();
        Button btAdd = new Button("Add");
        Button btDelete = new Button("Delete");
        btAdd.setPrefWidth(120);
        btAdd.setPrefHeight(40);
        btDelete.setPrefWidth(120);
        btDelete.setPrefHeight(40);
        TextField tfEnglish = new TextField();
        TextField tfChinese = new TextField();
        Label lbEnglish = new Label("English:");
        Label lbChinese = new Label("       Chinese:");
        gridPane5.add(lbEnglish,0,0);
        gridPane5.add(tfEnglish,1,0);
        gridPane5.add(lbChinese,2,0);
        gridPane5.add(tfChinese,3,0);
        gridPane5.add(btAdd,1,1);
        gridPane5.add(btDelete,2,1);
        gridPane5.setVgap(10);
        gridPane5.setHgap(10);
        VBox vBox6 = new VBox();
        vBox6.getChildren().addAll(ttfFilePath,gridPane1);
        vBox6.setPadding(new Insets(10,10,10,10));
        vBox6.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color:cbcbcb;");
        VBox.setMargin(vBox6,new Insets(0,0,10,0));
        GridPane gridPane7 = new GridPane();
        VBox vBox8 = new VBox();
        vBox8.getChildren().add(gridPane5);
        vBox8.setPadding(new Insets(0,0,10,0));
        gridPane7.setHgap(10);
        gridPane7.add(vBox1,0,0);
        vBox8.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color:cbcbcb;");
        vBox3.getChildren().addAll(vBox6,vBox8);
        gridPane5.setPadding(new Insets(10,10,10,10));
        vBox2.getChildren().addAll(label,vBox3);
        VBox.setMargin(vBox2,new Insets(-10,0,0,0));
        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("B+ tree");
        rb1.setToggleGroup(group);
        RadioButton rb2 = new RadioButton("red-black tree");
        rb1.setSelected(true);
        rb2.setToggleGroup(group);
        GridPane gridPane2 = new GridPane();
        gridPane2.add(rb1,0,0);
        gridPane2.add(rb2,1,0);
        gridPane2.setPadding(new Insets(10,40,20,40));
        gridPane2.setVgap(10);
        gridPane2.setHgap(10);
        VBox vBox4 = new VBox();
        Label lbLook = new Label("LOOK-UP");
        GridPane gridPane3 = new GridPane();
        TextField tfTransEn = new TextField();
        Button btTrans = new Button("Translate");
        TextField tfFrom = new TextField();
        TextField tfto = new TextField();
        Button btSubmit = new Button("  Submit  ");
        Label lbSearch = new Label("Search From");
        TextArea txShow = new TextArea("Here show the result!");
        txShow.setEditable(false);
        txShow.setStyle("-fx-background-color: #ffffff");
        Label lbto = new Label("to");
        gridPane3.add(tfTransEn,0,0);
        gridPane3.add(btTrans,4,0);
        gridPane3.add(lbSearch,0,1);
        gridPane3.add(tfFrom,1,1);
        gridPane3.add(lbto,2,1);
        gridPane3.add(tfto,3,1);
        gridPane3.add(btSubmit,4,1);
        gridPane3.setVgap(10);
        gridPane3.setHgap(30);
        VBox vBox5 = new VBox();
        vBox1.getChildren().addAll(gridPane2,vBox5);
        vBox5.setPadding(new Insets(0,0,0,10));
        vBox4.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color:a8ced9;");
        vBox2.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color:a8ced9;");
        VBox vBox7 = new VBox();
        vBox7.getChildren().add(gridPane3);
        vBox7.setPadding(new Insets(20,10,20,10));
        VBox vBox9 = new VBox();
        vBox9.getChildren().add(txShow);
        vBox4.getChildren().addAll(lbLook,vBox7,vBox9);
        vBox4.setPadding(new Insets(0,10,10,10));
        vBox4.setPrefHeight(215);
        vBox5.getChildren().add(vBox4);
        GridPane.setColumnSpan(tfTransEn,4);
        VBox vBox11 = new VBox();
        HBox hBox1 = new HBox();
        hBox1.getChildren().add(label);
        hBox1.setPadding(new Insets(0,0,0,10));
        vBox11.getChildren().addAll(hBox1,vBox2);
        hBox.getChildren().addAll(vBox11,vBox1);
        Pane pane = new Pane(hBox);
        Scene scene = new Scene(pane,primaryStage.getWidth(),primaryStage.getHeight());
        primaryStage.setScene(scene);

        //导入文件
        btBrowser.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("字典-选择操作文件");
            String cwd = System.getProperty("user.dir");
            File file = new File(cwd);
            fileChooser.setInitialDirectory(file);
            selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile != null){
                ttfFilePath.setText(selectedFile.getAbsolutePath());
            }
        });

        btSub.setOnMouseClicked(mouseEvent -> {
            File file = new File(ttfFilePath.getText());
            if(ttfFilePath.getText() == null || ttfFilePath.getText().trim().equals("") || !file.exists()){
                txShow.setText("Please input the legal file.");
                return;
            }
            if(dictionary.update(file, type)) {
                txShow.setText("Handle file successfully!");
                ttfFilePath.setText("");
            }else {
                txShow.setText("Illegal file!");
            }
        });

        //插入新的单词
        btAdd.setOnMouseClicked(mouseEvent -> {
            txShow.setText("");
            if(tfEnglish.getText() == null || tfEnglish.getText().trim().equals("") || tfEnglish.getText() == null || tfEnglish.getText().trim().equals("")){
                txShow.setText("Please input the English and the Chinese!");
            }else{
                dictionary.insert(tfEnglish.getText(),tfChinese.getText(),type);
                txShow.setText("Add successfully");
            }
        });

        //删除单词
        btDelete.setOnMouseClicked(mouseEvent -> {
            txShow.setText("");
            String key = tfEnglish.getText();
            if(tfEnglish.getText() == null || tfEnglish.getText().trim().equals("")){
                txShow.setText("Please input the English and the Chinese!");
            }else{
                if(dictionary.delete(key, type)) {
                    txShow.setText("Delete successfully");
                }else {
                    txShow.setText("Not found!");
                }
            }
        });

        //选择树的模式
        rb1.setOnMouseClicked(mouseEvent -> {
            type = 0;//B+树模式
        });
        rb2.setOnMouseClicked(mouseEvent -> {
            type = 1;//红黑树模式
        });

        //查询单个单词
        btTrans.setOnMouseClicked(mouseEvent -> {
            String key = tfTransEn.getText();
            String value = dictionary.search(key,type);
            if(value == null || value.trim().equals("")){
                txShow.setText("Not found!");
            }else{
                txShow.setText(value);
            }
        });

        //范围查询
        btSubmit.setOnMouseClicked(mouseEvent -> {
            String from = tfFrom.getText();
            String to = tfto.getText();
            ArrayList<String> result = dictionary.search(from, to, type);
            if(result.size() == 0){
                txShow.setText("Not found!");
            }else {
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < result.size(); i++) {
                    builder.append(result.get(i)).append("\n");
                }
                txShow.setText(builder.toString());
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}

