
public class VolatileTest
{

	// Example of usage of only volatile
	public static void main(String[] args)
	{
		VolatileObject vo = new VolatileObject();
		Thread t1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int counter = 10;
				while (counter > 0)
				{
					while (vo.isNew)
					{
					}
					;
					Object o = vo.newObject();
					// synchronized (vo)
					// {
					System.out.println("New object:" + o.toString());
					counter--;
					// }
				}
			}
		});

		Thread t2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int counter = 10;
				while (counter > 0)
				{
					while (!vo.isNew)
					{
					}
					;
					Object o = vo.takeObject();
					// synchronized (vo)
					// {
					System.out.println("Take objet:" + o.toString());
					counter--;
					// }
				}
			}
		});

		t1.start();
		t2.start();
	}

}
