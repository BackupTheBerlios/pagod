import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import localhost.axis.ElectionWS_jws.ElectionWS;
import localhost.axis.ElectionWS_jws.ElectionWSService;
import localhost.axis.ElectionWS_jws.ElectionWSServiceLocator;


public class Client {
	
	
	public static void main(String[] args) {
		ElectionWSService service = new ElectionWSServiceLocator();
		try {
			ElectionWS election = service.getElectionWS();
			System.out.println(""+election.getResult(1));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
