
public class VolatileObject
{
	private Object o = null;
	volatile public boolean isNew = false;

	public Object newObject()
	{
		// isNew = true; //
		o = new Object();
		isNew = true; // <happens-before guarantee> guarantees "o = new Object()" has executed
		return o;
	}

	public Object takeObject()
	{
		isNew = false;
		return o;
	}

	public boolean isNew()
	{
		return isNew;
	}
}
