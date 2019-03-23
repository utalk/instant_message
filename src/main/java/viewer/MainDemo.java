package viewer;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import viewer.entity.Message;
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
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class MainDemo extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext = new ViewFlowContext();;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        initStub();
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
        
        stage.setTitle("JFoenix Demo");

        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 2.5;
            height = bounds.getHeight() / 1.35;
        }catch (Exception e){
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

    private void initStub() {
        UIContext.toUser = "ABCDEF";
        Message[] messages = new Message[20];
        for(int i = 0;i < 20;i++){
            Message message = new Message();
            message.setFrom("ABCDEF");
            message.setTo("c");
            message.setContent("message:"+i);
            message.setMessageId(i);
            message.setSendTime(new Timestamp(new Date().getTime()));
            messages[i] = message;
        }
        UIContext.pairs = new Pair[1];
        UIContext.pairs[0] = new Pair<>("ABCDEF",messages);
    }

}
