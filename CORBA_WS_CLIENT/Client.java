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
			String [][] result = election.getResult(1);
			for (int i = 0; i < result.length; i++)
			{
				for (int j=0; j < result[0].length; j++)
				{
					System.out.println(result[i][j]);
				}
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
