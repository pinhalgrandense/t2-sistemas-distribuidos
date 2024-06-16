package src.interfaces;

import java.rmi.RemoteException;

public interface IUserChat extends java.rmi.Remote {
  void deliverMsg(String senderName, String msg) throws RemoteException;
}
