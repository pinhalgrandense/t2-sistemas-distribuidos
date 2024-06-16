package src.room;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import src.interfaces.IRoomChat;
import src.interfaces.IUserChat;

public class RoomChat extends UnicastRemoteObject implements IRoomChat {
  private String roomName;
  private Map<String, IUserChat> userList;

  public RoomChat(String roomName) throws RemoteException {
    this.roomName = roomName;
    userList = new HashMap<>();
  }

  public void sendMsg(String usrName, String msg) throws RemoteException {
    for (IUserChat user : userList.values()) {
      user.deliverMsg(usrName, msg);
    }
  }

  public void joinRoom(String usrName, IUserChat user) throws RemoteException {
    userList.put(usrName, user);
    sendMsg("System", usrName + " has joined the room.");
  }

  public void leaveRoom(String usrName) throws RemoteException {
    userList.remove(usrName);
    sendMsg("System", usrName + " has left the room.");
  }

  public void closeRoom() throws RemoteException {
    sendMsg("System", "Sala fechada pelo servidor.");
    userList.clear();
  }

  public String getRoomName() throws RemoteException {
    return roomName;
  }
}
