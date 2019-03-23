package connector;

import model.MessageWrapper;

public interface Initializer {

    /**
     * 所有Kademlia相关的初始化工作都通过init方法来完成
     */
    MessageWrapper init(int ID);
}
