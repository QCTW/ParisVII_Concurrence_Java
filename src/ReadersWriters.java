
public class ReadersWriters extends RWBasic
{
	private int nReadCounter = 0;

	private synchronized void startRead(String sCallerName)
	{
		nReadCounter++;
		System.out.println(sCallerName + " startRead()");
	}

	private synchronized void endRead(String sCallerName)
	{
		nReadCounter--;
		if (nReadCounter == 0)
		{
			System.out.println(sCallerName + " notifyAll()");
			notifyAll();
		}
	}

	@Override
	public int read(String sCallerName)
	{
		startRead(sCallerName);
		int val = super.read(sCallerName);
		endRead(sCallerName);
		return val;
	}

	@Override
	public synchronized int write(String sCallerName)
	{
		if (nReadCounter > 0)
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return super.write(sCallerName);
	}

}
