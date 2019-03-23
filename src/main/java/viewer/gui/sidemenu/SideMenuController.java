package viewer.gui.sidemenu;

import com.jfoenix.controls.JFXListView;
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
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ViewController(value = "/fxml/SideMenu.fxml")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXListView<Label> sideList;

    private UIContext uiContext = UIContext.getInstance();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> new Thread(() -> Platform.runLater(() -> {
            if (newVal != null) {
                try {
                    contentFlowHandler.handle(newVal.getId());
                } catch (VetoException | FlowException exc) {
                    exc.printStackTrace();
                }
            }
        })).start());
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        renderSideList(contentFlow);
    }

    private void renderSideList(Flow flow) {
        ArrayList<Label> labels = new ArrayList<>();
        String labelStr = "群聊1";
        Label label = new Label(labelStr);
        label.setId(labelStr);
        label.setOnMouseClicked(e -> uiContext.setGroupTalking(true));
        flow.withGlobalLink(label.getId(), ChatController.class);
        labels.add(label);
        List<String> friendList = uiContext.getFriendList();
        if (friendList != null) {
            for (String nodeId : friendList) {
                Label labelTemp = new Label(nodeId);
                labelTemp.setId(nodeId);
                labelTemp.setOnMouseClicked(e -> {
                    uiContext.setToUser(nodeId);
                    uiContext.setGroupTalking(false);
                });
                flow.withGlobalLink(label.getId(), ChatController.class);
                labels.add(labelTemp);
            }
        }
        sideList.setItems(FXCollections.observableArrayList(labels));
    }

}
