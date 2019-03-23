package viewer.gui.uicomponents;

import model.ChatMessage;

public interface UIMessageReceiver {
    void receiveUIMessage(ChatMessage message);
}
