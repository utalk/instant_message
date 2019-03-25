package viewer.gui.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextArea;
import connector.UsernameGetter;
import io.datafx.controller.ViewNode;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import kademlia.ChatService.GroupSender;
import kademlia.ChatService.Sender;
import model.ChatMessage;
import viewer.context.UIContext;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    //这个一开始不在组建中需要初始化
    private VBox vBox = new VBox();

    //这个一开始不在组建中需要初始化
    private Label title = new Label();


    @ViewNode("textAreaContainer")
    private AnchorPane textAreaContainer;
    @FXML
    private JFXTextArea textArea;


    @ViewNode("footer")
    private HBox footer;
    @FXML
    private JFXButton button;


    private UIContext uiContext = UIContext.getInstance();

    private boolean press_ctrl = false;
    private boolean press_enter = false;

    private static final boolean check_single_same = false;

    @PostConstruct
    private void construct() {
        vBox.setStyle("-fx-background-color: lightgray");
        title.setStyle("-fx-text-fill:WHITE; -fx-font-size: 26;");
        scrollPane.setContent(vBox);
        scrollPane.getBottomBar().getChildren().add(title);
        ScrollPane innerScrollPane = (ScrollPane) scrollPane.getChildren().get(0);
        vBox.minHeightProperty().bind(scrollPane.prefHeightProperty());
//        innerScrollPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,null,null)));
        JFXScrollPane.smoothScrolling(innerScrollPane);
        UsernameGetter usernameGetter = uiContext.getUsernameGetter();
        String from = usernameGetter.getUsername(uiContext.getCurrentUser());
        if (uiContext.isGroupTalking()) {
            title.setText("您(" + from + ")正处在群聊中");
        } else {
            String to = usernameGetter.getUsername(uiContext.getToUser());
            title.setText("您(" + from + ")与Ta(" + to + ")正在聊天");
        }
        button.setOnAction(e -> handleSend());
        textArea.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case CONTROL:
                    press_ctrl = true;
                    break;
                case ENTER:
                    press_enter = true;
                    break;
                default:
                    break;
            }
            if (press_ctrl && press_enter) {
                handleSend();
            }
        });
        textArea.setOnKeyReleased(e -> {
            press_enter = false;
            press_ctrl = false;
        });
        uiContext.setUiMessageReceiver(this);
        scrollPane.prefHeightProperty().bind(outer.heightProperty().subtract(footer.getPrefHeight()).multiply(4.0 / 7));
        textAreaContainer.prefHeightProperty().bind(outer.heightProperty().subtract(footer.getPrefHeight()).multiply(3.0 / 7));

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
        Label message = new Label(uiContext.getUsernameGetter().getUsername(chatMessage.getFrom()) + "@" + new Date(chatMessage.getTime()).toString());
        Label messageText = new Label(chatMessage.getContent());
        messageText.setTextFill(Color.GREEN);
        messageText.setFont(Font.font("Cambria", 50));
        VBox messageContainer = new VBox();
        if (chatMessage.getFrom().equals(uiContext.getCurrentUser())) {
            messageContainer.setAlignment(Pos.TOP_RIGHT);
        }
        messageContainer.getChildren().addAll(message, messageText);
        vBox.getChildren().addAll(messageContainer);
    }

    private void addMessageToScreenIfIsAReply(ChatMessage chatMessage) {
        if (isGroupCondition(chatMessage) || (isP2PCondition(chatMessage) && isOnceSendTo(chatMessage) && isCurrentTarget(chatMessage))) {
            addMessageToScreen(chatMessage);
        }
    }

    //当前是群聊页面且是群聊消息
    private boolean isGroupCondition(ChatMessage chatMessage) {
        return uiContext.isGroupTalking() && chatMessage.isGroup();
    }

    //当前不是群聊页面，不是群聊消息
    private boolean isP2PCondition(ChatMessage chatMessage) {
        return !uiContext.isGroupTalking() && !chatMessage.isGroup();
    }

    private boolean isOnceSendTo(ChatMessage chatMessage) {
        return uiContext.getToUser().equals(chatMessage.getFrom());
    }

    private boolean isCurrentTarget(ChatMessage chatMessage) {
        return uiContext.getCurrentUser().equals(chatMessage.getTo());
    }

    @Override
    public void receiveUIMessage(ChatMessage chatMessage) {
        uiContext.setUiMessageReceiver(this);
        if (!checkMessageExisted(chatMessage, check_single_same)) {
            writeMessageData(chatMessage, OperationType.RECEIVE);
            addMessageToScreenIfIsAReply(chatMessage);
        }
    }

    private void sendMessage(ChatMessage chatMessage) {
        if (!chatMessage.isGroup()) {
            Sender sender = uiContext.getSender();
            if (sender != null) {
                sender.send(chatMessage);
                writeMessageData(chatMessage, OperationType.SEND);
                addMessageToScreen(chatMessage);
            }
        } else {
            GroupSender groupSender = uiContext.getGroupSender();
            if (groupSender != null) {
                groupSender.send(chatMessage);
                writeMessageData(chatMessage, OperationType.SEND);
                addMessageToScreen(chatMessage);
            }
        }
    }

    private boolean checkMessageExisted(ChatMessage chatMessage, boolean singleCheck) {
        String key = chatMessage.getFrom();
        if (chatMessage.isGroup()) {
            List<ChatMessage> chatMessages = uiContext.getGroupTalkingMessages();
            if (chatMessages != null) {
                return chatMessages.contains(chatMessage);
            } else {
                return false;
            }
        } else if (singleCheck) {
            Map<String, List<ChatMessage>> p2pTalkingMessages = uiContext.getP2pTalkingMessages();
            if (p2pTalkingMessages != null) {
                List<ChatMessage> resultMessages = p2pTalkingMessages.get(key);
                if (resultMessages != null) {
                    return resultMessages.contains(chatMessage);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
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
            } else {
                chatMessages = new ArrayList<>();
                chatMessages.add(chatMessage);
                uiContext.setGroupTalkingMessages(chatMessages);
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
