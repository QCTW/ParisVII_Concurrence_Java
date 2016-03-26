import java.util.Vector;
import java.util.concurrent.Semaphore;

public class PontPeterson
{
	private static final int CAPACITY = 3;
	volatile int turn = 0;
	volatile boolean isEmpty = false;
	private final Semaphore[] semEnter = { new Semaphore(CAPACITY), new Semaphore(CAPACITY) };

	public static void main(String[] args)
	{
		PontPeterson pToPass = new PontPeterson();
		Vector<VoiturePeterson> vCarList = new Vector<VoiturePeterson>();
		for (int n = 0; n < 5; n++)
		{
			VoiturePeterson vNord = new VoiturePeterson(Vers.NORD, pToPass);
			VoiturePeterson vSud = new VoiturePeterson(Vers.SUD, pToPass);
			vCarList.add(vNord);
			vCarList.add(vSud);
		}

		for (VoiturePeterson v : vCarList)
		{
			v.start();
		}

	}

	public void tryToPass(VoiturePeterson v)
	{
		try
		{
			int myTurn = turn;
			semEnter[myTurn].acquire();
			if (turn == myTurn)
			{
				int otherTurn = 1 - myTurn;
				semEnter[otherTurn].acquire();
				turn = otherTurn;
				while (isEmpty == false)
				{
					isEmpty = true;
					semEnter[myTurn].release();
					semEnter[myTurn].acquire();
				}

				SectionCritic(v);

				semEnter[otherTurn].release();

			} else
			{
				isEmpty = false;
				SectionCritic(v);
			}
			semEnter[myTurn].release();

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void SectionCritic(VoiturePeterson v)
	{
		System.out.println(v.getId() + "(" + v.getVers() + ") entered the bridge");
		v.driving();
		System.out.println(v.getId() + "(" + v.getVers() + ") leaved the bridge");
	}

}

/**
 * enum Vers
 * {
 * NORD, SUD
 * }
 **/

class VoiturePeterson extends Thread
{
	private final PontPeterson bridge2Pass;
	private final Vers vers;

	public VoiturePeterson(Vers v, PontPeterson p)
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