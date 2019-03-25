package viewer;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import connector.Initializer;
import connector.InitializerImpl;
import kademlia.exception.MessageNotSetException;
import model.MessageWrapper;
import viewer.context.UIContext;
import viewer.gui.main.MainController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class MainDemo extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext = new ViewFlowContext();

    private Initializer initializer = new InitializerImpl();

    private UIContext uiContext = UIContext.getInstance();

    private static int init_id = 2;

    public static void main(String[] args) {
        if (args.length == 1) {
            init_id = Integer.parseInt(args[0]);
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MessageWrapper messageWrapper = initializer.init(init_id);
        try {
            if (messageWrapper == null || messageWrapper.getSender() == null || messageWrapper.getCurrentUser() == null || messageWrapper.getFriends() == null || messageWrapper.getGroupSender() == null || messageWrapper.getUsernameGetter() == null) {
                throw new MessageNotSetException();
            }
            uiContext.setFriendList(messageWrapper.getFriends());
            uiContext.setCurrentUser(messageWrapper.getCurrentUser());
            uiContext.setSender(messageWrapper.getSender());
            uiContext.setGroupSender(messageWrapper.getGroupSender());
            uiContext.setUsernameGetter(messageWrapper.getUsernameGetter());
        } catch (MessageNotSetException e) {
            e.printStackTrace();
        }

        //下面是载入图标，没用到可以去掉
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(MainDemo.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new SVGGlyph(""));
        decorator.setOnCloseButtonAction(() -> System.exit(0));

        stage.setTitle("Instant Message Demo");

        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 2.5;
            height = bounds.getHeight() / 1.35;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainDemo.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                MainDemo.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                MainDemo.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
