
public class Personne extends Thread
{
	enum Sex
	{
		FEMME, HOMME
	}

	private Sex sexe = null;
	private SalleDeBain salleDeBain = null;

	public Personne(Sex s, SalleDeBain sdb)
	{
		super();
		sexe = s;
		salleDeBain = sdb;
		salleDeBain.safePrint(sexe + " " + getId() + " created " + salleDeBain.getStatus());
	}

	public Sex getSex()
	{
		return sexe;
	}

	public void wait(int nWaitTime)
	{
		try
		{
			sleep(nWaitTime);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		salleDeBain.tryToEnter(this);
		salleDeBain.safePrint(sexe + " " + getId() + " uses toillette " + salleDeBain.getStatus());
		wait(2000);
		salleDeBain.exit(this);
	}
}
