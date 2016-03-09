
public class RWBasic
{
	private int data = 0;

	public int read(String sCallerName)
	{
		System.out.println(sCallerName + " read data = " + data);
		return data;
	}

	public int write(String sCallerName)
	{
		data++;
		System.out.println(sCallerName + " wrote data = " + data);
		return data;
	}
}
