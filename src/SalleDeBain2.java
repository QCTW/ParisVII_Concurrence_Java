import java.util.Vector;
import java.util.concurrent.Semaphore;

public class SalleDeBain2
{
	private final static int CAPACITY = 4;
	private final Semaphore semUsing = new Semaphore(CAPACITY);
	private volatile Personne2.Sex currentAllowedSex = Personne2.Sex.FEMME; // At the beginning allowing only women

	public static void main(String[] args)
	{
		Vector<Personne2> v = new Vector<Personne2>();
		SalleDeBain2 sdb = new SalleDeBain2();
		for (int i = 0; i < 6; i++)
		{
			Personne2 pF = new Personne2(Personne2.Sex.FEMME, sdb);
			Personne2 pH = new Personne2(Personne2.Sex.HOMME, sdb);
			v.add(pF);
			v.add(pH);
		}

		for (Personne2 p : v)
		{
			p.start();
		}
	}

	public void useSalleDeBain(Personne2 p)
	{
		try
		{
			synchronized (semUsing)
			{
				while (currentAllowedSex != p.getSex())
				{
					semUsing.wait();
				}

				semUsing.acquire();
				if (semUsing.availablePermits() == 0)
				{
					currentAllowedSex = Personne2.Sex.PLEIN;
					safePrint(p.getSex() + " " + p.getId() + " is the last using the bathroom. Reverse allowed sex. " + getStatus());
				}
			}

			usingSalleDeBain(p);

			synchronized (semUsing)
			{
				semUsing.release();
				safePrint(p.getSex() + " " + p.getId() + " left the bathroom. " + getStatus());
				if (semUsing.availablePermits() == CAPACITY)
				{
					currentAllowedSex = reverse(p.getSex());
					semUsing.notifyAll();
					safePrint(p.getSex() + " " + p.getId() + " is the last one left the bathroom. Notify waiting threads." + getStatus());
				}
			}

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private Personne2.Sex reverse(Personne2.Sex sex)
	{
		if (sex == Personne2.Sex.HOMME)
			return Personne2.Sex.FEMME;
		return Personne2.Sex.HOMME;
	}

	private void usingSalleDeBain(Personne2 p)
	{
		safePrint(p.getSex() + " " + p.getId() + " uses the bathroom! " + getStatus());
		p.pipi();
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
		return "Status:" + currentAllowedSex + " (Available:" + semUsing.availablePermits() + "/Waiting:" + semUsing.getQueueLength() + ")";
	}
}
