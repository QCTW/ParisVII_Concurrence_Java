import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Pont
{
	private static final int CAPACITY = 3;
	volatile Vers currentDirection = Vers.VIDE;
	private final Semaphore semEnter = new Semaphore(CAPACITY);

	public static void main(String[] args)
	{
		Pont pToPass = new Pont();
		Vector<Voiture> vCarList = new Vector<Voiture>();
		for (int n = 0; n < 10; n++)
		{
			Voiture vNord = new Voiture(Vers.NORD, pToPass);
			Voiture vSud = new Voiture(Vers.SUD, pToPass);
			vCarList.add(vNord);
			vCarList.add(vSud);
		}

		for (Voiture v : vCarList)
		{
			v.start();
		}

	}

	public void tryToPass(Voiture v)
	{
		try
		{
			while (currentDirection != v.getVers())
			{
				synchronized (this)
				{
					if (currentDirection == Vers.VIDE)
					{
						currentDirection = v.getVers();
						System.out.println(v.getId() + "(" + v.getVers() + ") is the first car in the bridge(" + currentDirection + ")");
						break;
					}
				}
			}

			SectionCritic(v);

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	private void SectionCritic(Voiture v) throws InterruptedException
	{
		semEnter.acquire();
		System.out.println(v.getId() + "(" + v.getVers() + ") entered the bridge(" + currentDirection + ")");
		v.driving();
		System.out.println(v.getId() + "(" + v.getVers() + ") leaving the bridge(" + currentDirection + ")");
		semEnter.release();
		System.out.println(v.getId() + "(" + v.getVers() + ") leaved the bridge(" + currentDirection + "). Capacity:" + semEnter.availablePermits());
		synchronized (this)
		{
			if (semEnter.availablePermits() == CAPACITY)
			{
				currentDirection = Vers.VIDE;
				System.out.println(v.getId() + "(" + v.getVers() + ") is the last car left the bridge(" + currentDirection + ")");
			}
		}

	}

}

enum Vers
{
	NORD, SUD, VIDE
}

class Voiture extends Thread
{

	private final Pont bridge2Pass;
	private final Vers vers;

	public Voiture(Vers v, Pont p)
	{
		bridge2Pass = p;
		vers = v;
	}

	public Vers getVers()
	{
		return vers;
	}

	@Override
	public void run()
	{
		bridge2Pass.tryToPass(this);
	}

	public void driving()
	{
		try
		{
			Thread.sleep(500);
			System.out.println(this.getId() + "(" + vers + ") is driving");
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}