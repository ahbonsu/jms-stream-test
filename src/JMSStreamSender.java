import java.io.File;
import java.io.FileNotFoundException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;



public class JMSStreamSender implements Runnable
{
	private String url;
	private String queue;
	private File file;
	
	public  JMSStreamSender(String url, String queue, File file)
	{
		this.url = url;
		this.queue = queue;
		this.file = file;
	}
	
	public void sendMessage() throws FileNotFoundException
	{
		
		try
		{
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url); //ctx.lookup 
			
			Connection con = cf.createConnection("admin", "admin");
			con.start();
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer producer = session.createProducer(session.createQueue(queue));
			
			BlobMessage message = ((ActiveMQSession) session).createBlobMessage(file);

			producer.send(message);
			
			System.out.println("SEND!");
			
			producer.close();
			session.close();
			con.close();
			
		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run()
	{
		
		try
		{
			sendMessage();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
