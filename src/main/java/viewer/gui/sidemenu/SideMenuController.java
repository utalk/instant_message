package viewer.gui.sidemenu;

import com.jfoenix.controls.JFXListView;
import connector.UsernameGetter;
import javafx.collections.FXCollections;
import viewer.context.UIContext;
import viewer.gui.uicomponents.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ViewController(value = "/fxml/SideMenu.fxml")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXListView<Label> sideList;

    private UIContext uiContext = UIContext.getInstance();

    private String groupStr = "群聊1";

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        renderSideList(contentFlow);
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> new Thread(() -> Platform.runLater(() -> {
            if (newVal != null) {
                try {
                    if (newVal.getId().equals(groupStr)) {
                        uiContext.setGroupTalking(true);
                    } else {
                        uiContext.setToUser(newVal.getId());
                        uiContext.setGroupTalking(false);
                    }
                    contentFlowHandler.handle(newVal.getId());
                } catch (VetoException | FlowException exc) {
                    exc.printStackTrace();
                }
            }
        })).start());
    }

    private void renderSideList(Flow flow) {
        UsernameGetter usernameGetter = uiContext.getUsernameGetter();
        ArrayList<Label> labels = new ArrayList<>();
        Label label = new Label(groupStr);
        label.setId(groupStr);
        flow.withGlobalLink(groupStr, ChatController.class);
        labels.add(label);
        List<String> friendList = uiContext.getFriendList();
        if (friendList != null) {
            for (String nodeId : friendList) {
                Label labelTemp = new Label(usernameGetter.getUsername(nodeId));
                labelTemp.setId(nodeId);
                flow.withGlobalLink(nodeId, ChatController.class);
                labels.add(labelTemp);
            }
        }
        sideList.setItems(FXCollections.observableArrayList(labels));
    }

}
