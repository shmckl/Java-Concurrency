import java.util.RandomAccess;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Company {
	private String name;
	private float totalNumberOfShares;
	private float availableNumberOfShares;
	private float price;
	public final ReadWriteLock comLock = new ReentrantReadWriteLock();

	public Company() {

	}

	public Company(String name, float totalNumberOfShares, float price) {
		setName(name);
		setTotalShares(totalNumberOfShares);
		setPrice(price);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTotalShares(float number) {
		this.totalNumberOfShares = number;
		this.availableNumberOfShares = number - this.availableNumberOfShares;
		if (availableNumberOfShares < 0) {
			System.out.println("The share price of " + name + " is negative!");
		}
	}

	public float getTotalShares() {
		return totalNumberOfShares;
	}

	public void setAvailableShares(float number) {
		this.availableNumberOfShares = number;
	}

	public float getAvailableShares() {
		return availableNumberOfShares;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float number) {
		this.price = number;
	}
}