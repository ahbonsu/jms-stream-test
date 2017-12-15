import java.io.File;
import java.io.FileNotFoundException;

public class JMSTester
{

	private static String URL = "tcp://localhost:61616?minLargeMessageSize=250000";

	private static String SUBJECT = "testQueue";
	
	private static File FROM_FILE = new File("/home/hauensteina/testfile.tar");
	private static File TO_FILE = new File("/home/hauensteina/testfile_copy.tar");
	
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException
	{

		Thread receiver =  new Thread(new JMSStreamReceiver(URL, SUBJECT, TO_FILE));
		receiver.start();
		
		Thread sender = new Thread(new JMSStreamSender(URL, SUBJECT, FROM_FILE));
		sender.start();
	}

}
