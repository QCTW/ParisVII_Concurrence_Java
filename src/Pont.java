import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Pont
{
	private static final int CAPACITY = 3;
	volatile int nSwitchCount = 0;
	volatile Vers currentDirection = Vers.VIDE;
	private final Semaphore semEnter = new Semaphore(CAPACITY);

	public static void main(String[] args)
	{
		Pont pToPass = new Pont();
		Vector<Voiture> vCarList = new Vector<Voiture>();
		for (int n = 0; n < 5; n++)
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
			// Here we use Pont(this) as our lock object, to protect the variable currentDirection
			// Also, forcing cars that do not have the same direction as the bridge to wait with the lock
			synchronized (this)
			{
				// If the direction of the car is not the same as the bridge, block in the while loop
				while (currentDirection != v.getVers())
				{
					// If the bridge is empty -- means this car is the first car get into the bridge
					// Set the direction of the bridge as the car's
					if (currentDirection == Vers.VIDE)
					{
						currentDirection = v.getVers();
						System.out.println(v.getId() + "(" + v.getVers() + ") is the first car in the bridge(" + currentDirection + ")");
						break;
					}

					this.wait();
				}

				// If it can exist the while loop above, means that the direction of the bridge is the same as the car
				// Try to get a semaphore -- note that only the car who has the lock can get a semaphore
				semEnter.acquire();
				System.out.println(v.getId() + "(" + v.getVers() + ") entered the bridge(" + currentDirection + "). Capacity:" + semEnter.availablePermits());
				// After got a semaphore, check if this car is the last one on the bridge of the total capacity
				if (semEnter.availablePermits() == 0)
				{
					// If it is the last one on the bridge of the total capacity, reverse the direction of the bridge
					currentDirection = reverse(v.getVers());
					System.out.println(v.getId() + "(" + v.getVers() + ") reversed the direction of the bridge(" + currentDirection + ")");
				}
			}

			v.driving();
			semEnter.release();
			System.out.println(v.getId() + "(" + v.getVers() + ") leaved the bridge(" + currentDirection + "). Capacity:" + semEnter.availablePermits());

			// Use lock again because we are going to change the value of currentDirection again.
			synchronized (this)
			{
				// If this car is the last one who left the bridge, set the direction to empty(VIDE) and notify all the other car who is waiting for the same lock
				if (semEnter.availablePermits() == CAPACITY)
				{
					currentDirection = Vers.VIDE;
					System.out.println(v.getId() + "(" + v.getVers() + ") is the last car left the bridge(" + currentDirection + ")");
					this.notifyAll();
				}
			}

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	private Vers reverse(Vers vers)
	{
		if (vers == Vers.NORD)
			return Vers.SUD;
		return Vers.NORD;
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