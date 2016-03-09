
public class VolatileObject
{
	private Object o = null;
	volatile public boolean isNew = false;

	public Object newObject()
	{
		// isNew = true; // Incorrect place since <happens-before guarantee> ensures that "o = new Object()" will execute AFTER and this will create race condition
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
