package connector;

import model.MessageWrapper;
import viewer.context.UIContext;

public class InitializerImpl implements Initializer {


    /**
     * @param nodeId 好友的用户名（用nodeId来表示）
     */
    private void addFriendInUI(String nodeId) {
        UIContext.addFriend(nodeId);
    }


    /**
     * @param nodeId 当前用户的用户名
     */
    private void setCurrentUserInUI(String nodeId) {
        UIContext.setCurrentUser(nodeId);
    }


    @Override
    public MessageWrapper init() {

        //TODO 在init方法中，需要设置当前好友，添加所有的好友，并完成Kademlia的初始化工作等
        return null;
    }
}
