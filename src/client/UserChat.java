package src.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Scanner;

import src.interfaces.IRoomChat;
import src.interfaces.IServerChat;
import src.interfaces.IUserChat;

public class UserChat implements IUserChat {
  private String userName;
  private IServerChat server;
  private IRoomChat currentRoom;

  public UserChat(String userName, IServerChat server) {
    this.userName = userName;
    this.server = server;
  }

  public void deliverMsg(String senderName, String msg) throws RemoteException {
    System.out.println(senderName + ": " + msg);
  }

  public void joinRoom(String roomName) {
    try {
      IRoomChat room = (IRoomChat) Naming.lookup(roomName);
      room.joinRoom(userName, this);
      currentRoom = room;
      System.out.println("Joined room " + roomName);
    } catch (NotBoundException | RemoteException | java.net.MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public void sendMsg(String msg) {
    try {
      if (currentRoom != null) {
        currentRoom.sendMsg(userName, msg);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void leaveRoom() {
    try {
      if (currentRoom != null) {
        currentRoom.leaveRoom(userName);
        currentRoom = null;
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    try {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter your name: ");
      String userName = scanner.nextLine();

      IServerChat server = (IServerChat) Naming.lookup("rmi://localhost:2020/Servidor");

      UserChat user = new UserChat(userName, server);
      List<String> rooms = server.getRooms();
      System.out.println("Available rooms: " + rooms);

      System.out.print("Enter room name to join or create: ");
      String roomName = scanner.nextLine();
      if (!rooms.contains(roomName)) {
        server.createRoom(roomName);
      }

      user.joinRoom(roomName);
      System.out.println("Enter messages to send or 'exit' to leave:");
      String msg;
      while (!(msg = scanner.nextLine()).equals("exit")) {
        user.sendMsg(msg);
      }

      user.leaveRoom();
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
