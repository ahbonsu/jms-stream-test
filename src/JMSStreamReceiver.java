import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;


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
	
	public void receiveMessage() throws IOException
	{
		try
		{
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url); //ctx.lookup 
			
			Connection con = cf.createConnection("admin", "admin");
			con.start();
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageConsumer consumer = session.createConsumer(session.createQueue(queue));
			
			BlobMessage message = (BlobMessage) consumer.receive();
			
			InputStream is = message.getInputStream();
			
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] buffer = new byte[4* 1024];
			int read = 0;
			while((read = is.read(buffer))  != -1)
			{
				fos.write(buffer, 0, read);
			}
			
			is.close();
			fos.close();
			
			System.out.println("RECEIVED!");
			
			consumer.close();
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
			receiveMessage();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
