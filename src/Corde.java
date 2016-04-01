import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Corde
{
	private static final int CAPACITY = 3;
	private Direction currentMoveDir = Direction.LEFT;
	private final Lock lock = new ReentrantLock();
	private final Condition cSameDirection = lock.newCondition();
	private final Condition cFull = lock.newCondition();
	private final Semaphore semPassing = new Semaphore(CAPACITY);

	public static void main(String[] args)
	{
		Corde corde = new Corde();
		Vector<Babouin> v = new Vector<Babouin>();
		for (int i = 0; i < 10; i++)
		{
			Babouin bLeft = new Babouin(corde, Direction.LEFT);
			Babouin bRight = new Babouin(corde, Direction.RIGHT);
			v.add(bLeft);
			v.add(bRight);
		}

		for (Babouin b : v)
		{
			b.start();
		}
	}

	public void traverse(Babouin b)
	{
		try
		{
			lock.lock();
			int nWaitCount = 0;
			System.out.println(b.getId() + "(" + b.getDirection() + ") tries to pass the canyon");
			if (currentMoveDir != b.getDirection())
			{
				nWaitCount++;
				System.out.println(b.getId() + "(" + b.getDirection() + ") waits(" + nWaitCount + ") because the direction of the rope(" + currentMoveDir + ") is not the same. " + getStatus());
				cSameDirection.await();
			}
			while (semPassing.availablePermits() == 0)
			{
				nWaitCount++;
				System.out.println(b.getId() + "(" + b.getDirection() + ") waits(" + nWaitCount + ") because that the rope(" + currentMoveDir + ") is full. " + getStatus());
				cFull.await();
				while (currentMoveDir != b.getDirection())
				{
					nWaitCount++;
					System.out.println(b.getId() + "(" + b.getDirection() + ") waits(" + nWaitCount + ") because the direction of the rope(" + currentMoveDir + ") is not the same. " + getStatus());
					cSameDirection.await();
				}
			}
			semPassing.acquire();
			lock.unlock();
			// System.out.println(b.getId() + "(" + b.getDirection() + ") waits because the rope(" + currentMoveDir + ") is full. " + getStatus());
			System.out.println(b.getId() + "(" + b.getDirection() + ") jumps on the rope(" + currentMoveDir + ") " + getStatus());
			b.moving();

			lock.lock();
			semPassing.release();
			System.out.println(b.getId() + "(" + b.getDirection() + ") left on the rope(" + currentMoveDir + ") " + getStatus());
			if (semPassing.availablePermits() == CAPACITY)
			{
				currentMoveDir = reverse(b.getDirection());
				System.out.println(b.getId() + "(" + b.getDirection() + ") is the last one left the rope. Reverse the rope(" + currentMoveDir + ") " + getStatus());
				cSameDirection.signalAll();
				cFull.signalAll();
			}
			lock.unlock();

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	private Direction reverse(Direction d)
	{
		if (d == Direction.LEFT)
			return Direction.RIGHT;
		return Direction.LEFT;
	}

	private String getStatus()
	{
		return "Available: " + semPassing.availablePermits();
	}

}

enum Direction
{
	LEFT, RIGHT;
}

class Babouin extends Thread
{
	private final Corde cordeToUse;
	private final Direction passDirection;

	Babouin(Corde c, Direction d)
	{
		cordeToUse = c;
		passDirection = d;
	}

	Direction getDirection()
	{
		return passDirection;
	}

	public void moving() throws InterruptedException
	{
		System.out.println(this.getId() + "(" + getDirection() + ") is passing the canyon");
		Babouin.sleep(500);
	}

	@Override
	public void run()
	{
		cordeToUse.traverse(this);
	}
}
