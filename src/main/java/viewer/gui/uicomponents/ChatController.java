package viewer.gui.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextArea;
import io.datafx.controller.ViewNode;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import kademlia.ChatService.Sender;
import model.ChatMessage;
import viewer.context.UIContext;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ViewController(value = "/fxml/ui/Chat.fxml", title = "Material Design Example")
public class ChatController implements UIMessageReceiver {
    @ViewNode("outer")
    private VBox outer;

    @ViewNode("scrollPane")
    private JFXScrollPane scrollPane;
    @FXML
    private VBox vBox;


    @ViewNode("textAreaContainer")
    private AnchorPane textAreaContainer;
    @FXML
    private JFXTextArea textArea;


    @ViewNode("footer")
    private HBox footer;
    @FXML
    private JFXButton button;


    private UIContext uiContext = UIContext.getInstance();

    @PostConstruct
    private void construct() {
        button.setOnAction(e -> handleSend());
        uiContext.setUiMessageReceiver(this);
        scrollPane.prefHeightProperty().bind(outer.heightProperty().multiply(4.0 / 7));
        textAreaContainer.prefHeightProperty().bind(outer.heightProperty().multiply(2.0 / 7));
        footer.prefHeightProperty().bind(outer.heightProperty().multiply(1.0 / 7));

        render();
    }

    private void handleSend() {
        ChatMessage chatMessage = new ChatMessage(uiContext.getCurrentUser(), uiContext.getToUser(), textArea.getText(), uiContext.isGroupTalking());
        sendMessage(chatMessage);
        textArea.setText("");
    }

    private void render() {
        if (uiContext.isGroupTalking()) {
            for (ChatMessage chatMessage : uiContext.getGroupTalkingMessages()) {
                addMessageToScreen(chatMessage);
            }
        } else {
            List<ChatMessage> resultMessages = uiContext.getP2pTalkingMessages().get(uiContext.getToUser());
            if (resultMessages != null) {
                for (ChatMessage chatMessage : resultMessages) {
                    addMessageToScreen(chatMessage);
                }
            }
        }
    }

    private void addMessageToScreen(ChatMessage chatMessage) {
        Label message = new Label(chatMessage.getFrom() + "@" + new Date(chatMessage.getTime()).toString());
        Label messageText = new Label(chatMessage.getContent());
        messageText.setTextFill(Color.GREEN);
        messageText.setFont(Font.font("Cambria", 50));
        VBox messageContainer = new VBox();
        if (chatMessage.getFrom().equals(uiContext.getCurrentUser())) {
            messageContainer.setAlignment(Pos.TOP_RIGHT);
        }
        messageContainer.getChildren().addAll(message, messageText);
        vBox.getChildren().add(messageContainer);
    }

    private void addMessageToScreenIfIsAReply(ChatMessage chatMessage) {
        //当前是群聊页面且是群聊消息
        boolean groupCondition = uiContext.isGroupTalking() && chatMessage.isGroup();
        //当前不是群聊页面，不是群聊消息
        boolean p2pCondition = !uiContext.isGroupTalking() && !chatMessage.isGroup();
        boolean onceSendTo = uiContext.getToUser().equals(chatMessage.getFrom());
        boolean currentTarget = uiContext.getCurrentUser().equals(chatMessage.getTo());
        if (groupCondition || (p2pCondition && onceSendTo && currentTarget)) {
            addMessageToScreen(chatMessage);
        }
    }


    @Override
    public void receiveUIMessage(ChatMessage chatMessage) {
        uiContext.setUiMessageReceiver(this);
        writeMessageData(chatMessage, OperationType.RECEIVE);
        addMessageToScreenIfIsAReply(chatMessage);
    }

    private void sendMessage(ChatMessage chatMessage) {
        Sender sender = uiContext.getSender();
        if (sender != null) {
            sender.send(chatMessage);
            writeMessageData(chatMessage, OperationType.SEND);
            addMessageToScreen(chatMessage);
        }
    }

    private void writeMessageData(ChatMessage chatMessage, OperationType operationType) {
        String key;
        if (operationType.equals(OperationType.SEND)) {
            key = chatMessage.getTo();
        } else if (operationType.equals(OperationType.RECEIVE)) {
            key = chatMessage.getFrom();
        } else {
            return;
        }
        if (chatMessage.isGroup()) {
            List<ChatMessage> chatMessages = uiContext.getGroupTalkingMessages();
            if (chatMessages != null) {
                chatMessages.add(chatMessage);
            }
        } else {
            Map<String, List<ChatMessage>> p2pTalkingMessages = uiContext.getP2pTalkingMessages();
            if (p2pTalkingMessages != null) {
                List<ChatMessage> resultMessages = p2pTalkingMessages.get(key);
                if (resultMessages == null) {
                    List<ChatMessage> chatMessages = new ArrayList<>();
                    chatMessages.add(chatMessage);
                    p2pTalkingMessages.put(key, chatMessages);
                } else {
                    resultMessages.add(chatMessage);
                }
            }
        }
    }
}
