package kademlia.ChatService;

import com.google.gson.Gson;
import connector.InitializerImpl;
import model.ChatMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lzb
 * @date 2019/3/23 22:25
 */
public class GroupSender_Impl implements GroupSender {

    private String[][] groupMatrix = new String[3][3];//用户id的通信矩阵

    private Set<String> idHasBeenSent = new HashSet<>();

    private Sender sender = new Sender_Impl();

    public GroupSender_Impl(String[] idArray) {
        for (int i = 0; i < idArray.length; i++) {
            this.groupMatrix[i/3][i%3] = idArray[i];
        }
    }

    public GroupSender_Impl(String[][] groupMatrix) {
        this.groupMatrix = groupMatrix;
    }

    @Override
    public boolean send(ChatMessage msg) {
        if (msg == null) {
            System.err.println("群聊输入的信息是null");
            return false;
        }
        if (!msg.isGroup()){
            System.err.println("群聊输入的不是群聊消息");
        }
        if (this.idHasBeenSent.contains(msg.getMessageID())){
            System.out.println("这条消息已经发送过了，所以不做处理");
            return false;
        }

        String lastUser = msg.getFrom();//消息来源的用户id
        int index = InitializerImpl.current_ID;//当前用户id，在群聊问题中收到msg的to都是当前用户
        String currUser = this.groupMatrix[index/3][index%3];
        System.out.println("当前发消息的用户是 = " + currUser);
        List<String> userIdToSent = this.findUserIdToSent(lastUser , currUser);//查询到接下来应该发送的用户

        userIdToSent.forEach(nextId -> {
            ChatMessage temp_meg = new ChatMessage(msg);
            //更改信息的发送者，和接收者
            //这里原来应该clone一下的，但是偷懒了，如果有bug，都怪我~~~~~~~~~
            temp_meg.setBefore(currUser);

            temp_meg.setTo(nextId);
            System.out.println("群聊当前发送的消息是" + new Gson().toJson(temp_meg));
            this.sender.send(temp_meg);

        });


        //添加已发送的id
        this.idHasBeenSent.add(msg.getMessageID());


        return true;
    }

    /**
     * 找到接下来发送信息的用户id list
     * @param lastUserId
     * @param currUserId
     * @return user id list
     */
    private List<String> findUserIdToSent(String lastUserId,String currUserId){
        List<String> resultList = new ArrayList<>();


        //找到当前用户位置和上一个用户的位置
        int currX = -1,currY = -1,
                lastX = -1 , lastY = -1;
        for (int i = 0; i < this.groupMatrix.length; i++) {
            for (int j = 0; j < this.groupMatrix[0].length; j++) {
                if (currUserId.equals(this.groupMatrix[i][j])){
                    currX = i;
                    currY = j;
                }
                if (lastUserId.equals(this.groupMatrix[i][j])){
                    lastX = i;
                    lastY = j;
                }
            }
        }


        Direction[] allDirection = new Direction[4];
        allDirection[0] = new Direction(0,-1);
        allDirection[1] = new Direction(0,+1);
        allDirection[2] = new Direction(1,0);
        allDirection[3] = new Direction(-1,0);

        //向四面八方扩散
        for (int i = 0; i < 4; i++) {
            try {
                String nextUserId = this.groupMatrix[currX + allDirection[i].x][currY + allDirection[i].y];
                resultList.add(nextUserId);
            }catch (ArrayIndexOutOfBoundsException ignored){

            }
        }

        //除去上一个用户的id
        resultList.remove(lastUserId);




        return resultList;
    }

    //简单内部类，用来简化操作的
    private class Direction{
        public int x,y;

        public Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


}
