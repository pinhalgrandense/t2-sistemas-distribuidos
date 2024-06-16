package src.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import src.interfaces.IRoomChat;
import src.interfaces.IServerChat;
import src.room.RoomChat;

public class ServerChat extends UnicastRemoteObject implements IServerChat {
  private ArrayList<String> roomList;
  private Map<String, IRoomChat> roomMap;

  public ServerChat() throws RemoteException {
    roomList = new ArrayList<>();
    roomMap = new HashMap<>();
  }

  public ArrayList<String> getRooms() throws RemoteException {
    return roomList;
  }

  public synchronized void createRoom(String roomName) throws RemoteException {
    if (!roomMap.containsKey(roomName)) {
      try {
        RoomChat newRoom = new RoomChat(roomName);
        roomMap.put(roomName, newRoom);
        roomList.add(roomName);

        // Vincular a sala ao registro RMI
        Registry registry = LocateRegistry.getRegistry(2020);
        registry.rebind(roomName, newRoom);
        System.out.println("Room " + roomName + " created and bound in RMI registry.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    try {
      ServerChat server = new ServerChat();
      Registry registry = LocateRegistry.createRegistry(2020);
      registry.rebind("Servidor", server);
      System.out.println("Server is running...");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
