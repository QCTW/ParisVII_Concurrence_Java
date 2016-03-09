
public class TP5
{
	public static void main(String[] args)
	{
		// RWBasic lock = new RWBasic();
		// RWExclusive lock = new RWExclusive();
		ReadersWriters lock = new ReadersWriters();
		Reader tRead = new Reader(10, lock);
		Reader tRead2 = new Reader(10, lock);
		Writer tWrite = new Writer(10, lock);
		Writer tWrite2 = new Writer(10, lock);
		tRead.start();
		tWrite.start();
		tRead2.start();
		tWrite2.start();
	}
}

/**
 * Result with RWBasic:
 * Write data =1
 * Read data =0
 * Read data =2
 * Write data =2
 * Write data =3
 * Read data =3
 * Write data =4
 * Read data =4
 * Write data =5
 * Read data =5
 * Write data =6
 * Read data =6
 * Write data =7
 * Read data =7
 * Read data =8
 * Write data =8
 * Read data =8
 * Write data =9
 * Write data =10
 * Read data =10
 * 
 * =======================
 * Result with:
 * Read data = 0
 * Write data = 1
 * Read data = 1
 * Write data = 2
 * Read data = 2
 * Write data = 3
 * Read data = 3
 * Write data = 4
 * Read data = 4
 * Write data = 5
 * Write data = 6
 * Read data = 6
 * Write data = 7
 * Read data = 7
 * Write data = 8
 * Read data = 8
 * Write data = 9
 * Read data = 9
 * Write data = 10
 * Read data = 10
 * 
 * =======================
 * Thread10 read data = 0
 * Thread9 read data = 0
 * Thread11 wrote data = 1
 * Thread9 read data = 1
 * Thread10 read data = 1
 * Thread11 wrote data = 2
 * Thread10 read data = 2
 * Thread9 read data = 2
 * Thread11 wrote data = 3
 * Thread9 read data = 3
 * Thread10 read data = 3
 * Thread11 wrote data = 4
 * Thread10 read data = 4
 * Thread9 read data = 4
 * Thread11 wrote data = 5
 * Thread10 read data = 5
 * Thread9 read data = 5
 * Thread11 wrote data = 6
 * Thread10 read data = 6
 * Thread11 wrote data = 7
 * Thread9 read data = 7
 * Thread10 read data = 7
 * Thread9 read data = 7
 * Thread11 wrote data = 8
 * Thread10 read data = 8
 * Thread11 wrote data = 9
 * Thread9 read data = 9
 * Thread10 read data = 9
 * Thread9 read data = 9
 * Thread11 wrote data = 10
 * 
 **/
