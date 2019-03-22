package demos.gui.uicomponents;

import io.datafx.controller.ViewController;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/Chat.fxml", title = "Material Design Example")
public class ChatController {
    @PostConstruct
    private void construct(){
        System.out.println("construct");
    }
}
