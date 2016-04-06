import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum Status
{
	SLEEPING, HELP_ELFES, HELP_RENES;
}

public class PereNoel
{
	private static int NUM_ELFES = 3;
	private static int NUM_RENES = 6;
	private final Lock lock = new ReentrantLock();
	private final Condition cAllReneArrived = lock.newCondition();
	private final Condition cCollect3Questions = lock.newCondition();
	private final Semaphore semElfes = new Semaphore(NUM_ELFES);
	private final Semaphore semRenes = new Semaphore(NUM_RENES);
	private Status statusOfPereNoel = Status.SLEEPING;

	public void reportArrive(Rene r) throws InterruptedException
	{
		lock.lock();
		while (statusOfPereNoel != Status.HELP_RENES)
		{
			semRenes.acquire();
			if (semRenes.availablePermits() == 0) // Last rene should wake up the Pere Noel.
			{
				statusOfPereNoel = Status.HELP_RENES;
				System.out.println("Rene_" + r.getId() + " woke up le Pere Noel.");
				cAllReneArrived.signalAll();
			} else
			{
				System.out.println("Rene_" + r.getId() + " found le Pere Noel is sleeping. Waiting...");
				cAllReneArrived.await();
			}
			System.out.println("Rene_" + r.getId() + " is back to le Pere Noel.");
			semRenes.release();
		}
		lock.unlock();
	}

	public void reportProblem(Elf e) throws InterruptedException
	{
		lock.lock();
		while (statusOfPereNoel != Status.HELP_ELFES)
		{
			semElfes.acquire();
			if (semElfes.availablePermits() == 0)
			{
				statusOfPereNoel = Status.HELP_ELFES;
				System.out.println("Elf_" + e.getId() + " woke up le Pere Noel.");
				cCollect3Questions.signalAll();
			} else
			{
				System.out.println("Elf_" + e.getId() + " found le Pere Noel is sleeping. Waiting...");
				cCollect3Questions.await();
			}
			System.out.println("Elf_" + e.getId() + " reported problem to le Pere Noel.");
			semElfes.release();
		}
		lock.unlock();
	}

	public static void main(String[] args)
	{
		Vector<Thread> vThreads = new Vector<Thread>();
		PereNoel pere = new PereNoel();
		for (int i = 0; i < 8; i++)
		{
			Elf e = new Elf(pere);
			Rene r = new Rene(pere);
			vThreads.add(e);
			vThreads.add(r);
		}

		for (Thread t : vThreads)
		{
			t.start();
		}
	}

}

class Rene extends Thread
{
	PereNoel pere;

	Rene(PereNoel p)
	{
		pere = p;
	}

	@Override
	public void run()
	{
		int count = 3;
		while (count > 0)
		{
			try
			{
				Thread.sleep(1000);
				pere.reportArrive(this);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			count--;
		}
	}
}

class Elf extends Thread
{
	PereNoel pere;

	Elf(PereNoel p)
	{
		pere = p;
	}

	@Override
	public void run()
	{
		int count = 3;
		while (count > 0)
		{
			try
			{
				Thread.sleep(500);
				pere.reportProblem(this);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			count--;
		}
	}
}