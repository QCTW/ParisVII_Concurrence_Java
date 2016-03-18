import java.util.Vector;
import java.util.concurrent.Semaphore;

public class SalleDeBain
{
	private final static int CAPACITY = 4;
	private final Semaphore semUsing = new Semaphore(CAPACITY);

	public static void main(String[] args)
	{
		Vector<Personne> v = new Vector<Personne>();
		SalleDeBain sdb = new SalleDeBain();
		for (int i = 0; i < 6; i++)
		{
			Personne pF = new Personne(Personne.Sex.FEMME, sdb);
			Personne pH = new Personne(Personne.Sex.HOMME, sdb);
			v.add(pF);
			v.add(pH);
		}

		for (Personne p : v)
		{
			p.start();
		}
	}

	public void tryToEnter(Personne p)
	{
		try
		{
			safePrint(p.getSex() + " " + p.getId() + " try to get in " + getStatus());
			semUsing.acquire();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// while (!semUsing.tryAcquire())
		// {
		// safePrint(p.getSex() + " " + p.getId() + " waits to get in " + getStatus());
		// p.wait(500);
		// }
		safePrint(p.getSex() + " " + p.getId() + " got in " + getStatus());
	}

	public void exit(Personne p)
	{
		semUsing.release();
		safePrint(p.getSex() + " " + p.getId() + " exited " + getStatus());
	}

	public void safePrint(String strMsg)
	{
		synchronized (System.out)
		{
			System.out.println(strMsg);
		}
	}

	public String getStatus()
	{
		return "(" + semUsing.getQueueLength() + "/" + semUsing.availablePermits() + ")";
	}
}
