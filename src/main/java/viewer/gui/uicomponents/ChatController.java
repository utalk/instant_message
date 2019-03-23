package viewer.gui.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextField;
import viewer.entity.Message;
import viewer.context.UIContext;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/Chat.fxml", title = "Material Design Example")
public class ChatController {
    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXTextField textField;
    @FXML
    @ActionTrigger("sendMessage")
    private JFXButton button;
    @FXML
    private VBox vBox;

    @PostConstruct
    private void construct() {
        render();
    }

    @ActionMethod("sendMessage")
    private void handleClick() {

    }

    private void render() {
        for (Pair pair : UIContext.pairs) {
            if (pair.getKey().equals(UIContext.toUser)) {
                for (Message message : (Message[]) pair.getValue()) {
                    Label send = new Label(message.getFrom() + " (" + message.getSendTime() + ")");
                    Label messageText = new Label(message.getContent());
                    messageText.setTextFill(Color.GREEN);
                    messageText.setFont(Font.font("Cambria", 50));
                    vBox.getChildren().addAll(send, messageText);
                }
                scrollPane.setContent(vBox);
            }
        }
    }
}
