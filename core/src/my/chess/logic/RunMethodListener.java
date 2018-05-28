package my.chess.logic;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


/**
 * Runs method 'run' when an object of type T is received through listener
 */
public abstract class RunMethodListener<T> extends Listener {

	private Class<T> myClass;
	
	public RunMethodListener(Class<T> c)
	{
		myClass = c;
	}
	
	@Override
	public void received(Connection c, Object obj)
	{
		if(myClass.isInstance(obj))
		{
			run(c, myClass.cast(obj));
		}
	}
	
	public abstract void run(Connection con, T object);
}
