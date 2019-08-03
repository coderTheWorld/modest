import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MyRemoteServer extends UnicastRemoteObject implements MyRemote {

    @Override
    public String sayHello() throws RemoteException {
        return "Hello client";
    }

    public MyRemoteServer() throws RemoteException {

    }

    public static void main(String[] args) {
        try {
            /*MyRemote service = new MyRemoteServer();
            UnicastRemoteObject.unexportObject(service,true);
            MyRemote stub=(MyRemote)UnicastRemoteObject.exportObject(service,0);
            registry.rebind("RemoteHello", stub);*/
            MyRemote service=new MyRemoteServer();
            //Registry registry= LocateRegistry.getRegistry();
            Naming.rebind("RemoteHello",service);
            System.out.println("Server start!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
