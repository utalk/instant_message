package demos.gui.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import demos.entity.Message;
import demos.global.GlobalValue;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/Chat.fxml", title = "Material Design Example")
public class ChatController {
    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXTextField textField;
    @FXML
    private JFXButton button;
    @PostConstruct
    private void construct(){
        for(Pair<String, Message[]> pair : GlobalValue.pairs){
            if(pair.getKey().equals(GlobalValue.toUser)){
                VBox vBox = new VBox();
                for(Message message : pair.getValue()){
                    Label send = new Label(message.getFrom() + " (" + message.getSendTime() + ")");
                    Label messageText = new Label(message.getContent());
                    messageText.setTextFill(Color.GREEN);
                    messageText.setFont(Font.font("Cambria", 50));
                    vBox.getChildren().addAll(send,messageText);
                }
                scrollPane.setContent(vBox);
            }
        }
        System.out.println("construct");

    }
}
