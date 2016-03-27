
public class Personne2 extends Thread
{
	enum Sex
	{
		FEMME, HOMME, PLEIN;
	}

	private Sex sexe = null;
	private SalleDeBain2 salleDeBain = null;

	public Personne2(Sex s, SalleDeBain2 sdb)
	{
		super();
		sexe = s;
		salleDeBain = sdb;
	}

	public Sex getSex()
	{
		return sexe;
	}

	public void pipi()
	{
		try
		{
			sleep(500);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		salleDeBain.useSalleDeBain(this);
	}
}
