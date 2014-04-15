import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class CASLock implements Lock {

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	AtomicInteger lockVar;
	
	public CASLock() {
		this.lockVar.set(0);
	}
	
	@Override
	public void lock() {
		// TODO Auto-generated method stub
		this.lockVar.set(1);;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		this.lockVar.set(0);
	}

}
