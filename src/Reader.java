
public class Reader extends Thread
{
	RWBasic ref;
	int numOfRead;

	public Reader(int nr, RWBasic lock)
	{
		super();
		ref = lock;
		numOfRead = nr;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < numOfRead; i++)
		{
			ref.read("Thread" + this.getId());
			try
			{
				sleep(200);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
