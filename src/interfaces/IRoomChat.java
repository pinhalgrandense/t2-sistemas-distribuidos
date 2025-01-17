package src.interfaces;

import java.rmi.RemoteException;

public interface IRoomChat extends java.rmi.Remote {
  void sendMsg(String usrName, String msg) throws RemoteException;

  void joinRoom(String usrName, IUserChat user) throws RemoteException;

  void leaveRoom(String usrName) throws RemoteException;

  void closeRoom() throws RemoteException;

  String getRoomName() throws RemoteException;
}
