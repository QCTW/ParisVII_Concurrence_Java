
public class RWExclusive extends RWBasic
{

	@Override
	public synchronized int read(String sCallerName)
	{
		return super.read(sCallerName);
	}

	@Override
	public synchronized int write(String sCallerName)
	{
		return super.write(sCallerName);
	}

}
