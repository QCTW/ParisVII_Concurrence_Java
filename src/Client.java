import java.util.concurrent.Semaphore;

public class Client extends Thread
{
	private final static int NC = 2;
	private final static int NP = 2;
	private static Semaphore semCabine = new Semaphore(NC);
	private static Semaphore semPanier = new Semaphore(NP);

	@Override
	public void run()
	{
		try
		{
			while (!tryToGetBasketAndRoom())
			{
				sleep(500);
			}
			safePrint(this.getId() + " is changing clothes. " + getStatus());
			sleep(500);
			semCabine.release();
			safePrint(this.getId() + " went to swim. Now waiting a room to put on clothes. " + getStatus());
			semCabine.acquire();
			safePrint(this.getId() + " got a room to take off clothes. " + getStatus());
			sleep(500);
			semCabine.release();
			safePrint(this.getId() + " left the room. " + getStatus());
			semPanier.release();
			safePrint(this.getId() + " left swimming pool. " + getStatus());

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private boolean tryToGetBasketAndRoom() throws InterruptedException
	{
		boolean bSuccess = true;
		safePrint(this.getId() + " wants to get into the pool. " + getStatus());
		semCabine.acquire();
		safePrint(this.getId() + " got a room. Now waiting a basket..." + getStatus());
		if (!semPanier.tryAcquire(1))
		{
			safePrint(this.getId() + " unable to get a basket. Release the room. " + getStatus());
			semCabine.release();
			bSuccess = false;
		}
		return bSuccess;
	}

	public void _run()
	{
		try
		{
			safePrint(this.getId() + " wants to get a basket. " + getStatus());
			semPanier.acquire();
			safePrint(this.getId() + " got basket. Now waiting a room to take off clothes. " + getStatus());
			semCabine.acquire();
			safePrint(this.getId() + " got a room to take off clothes. " + getStatus());
			sleep(500);
			semCabine.release();
			safePrint(this.getId() + " goes to swim. Now waiting a room to put on clothes. " + getStatus());
			sleep(500);
			semCabine.acquire();
			safePrint(this.getId() + " got a room to put on clothes. " + getStatus());
			sleep(500);
			semCabine.release();
			safePrint(this.getId() + " is going to leave. " + getStatus());
			semPanier.release();
			safePrint(this.getId() + " left swimming pool. " + getStatus());

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private String getStatus()
	{
		return "Basket: " + semPanier.getQueueLength() + "/" + semPanier.availablePermits() + " Room:" + semCabine.getQueueLength() + "/" + semCabine.availablePermits();
	}

	private void safePrint(String msg)
	{
		synchronized (System.out)
		{
			System.out.println(msg);
		}
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 6; i++)
		{
			Client c = new Client();
			c.start();
		}
	}
}
