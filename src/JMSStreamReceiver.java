import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;


public class JMSStreamReceiver implements Runnable
{
	private String url;
	private String queue;
	private File file;
	
	public JMSStreamReceiver(String url, String queue, File file)
	{
		this.url = url;
		this.queue = queue;
		this.file = file;
	}
	
	public void receiveMessage()
	{
		try
		{
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url); //ctx.lookup
			Connection con = cf.createConnection("admin", "admin");
			con.start();
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageConsumer consumer = session.createConsumer(session.createQueue(queue));
			
			BytesMessage message = (BytesMessage) consumer.receive(120000);
			
			//String fileName = message.getStringProperty("fileName");
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			
			message.setObjectProperty("JMS_AMQ_SaveStream", bos);
			
			System.out.println("RECEIVED!");

			consumer.close();
			session.close();
			con.close();
			cf.close();

			
		} catch (JMSException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		receiveMessage();
		
	}
}
