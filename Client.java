import java.util.HashMap;

public class Client implements Runnable {
	private HashMap<Company, Float> shares;
	private float balance;
	private StockExchange stockExchange;

	public Client() {
		this.shares = new HashMap<Company, Float>();
	}

	public HashMap<Company, Float> getStocks() {
		return this.shares;
	}

	public void setStocks(Company company, float numberOfShares) {
		this.shares.put(company, numberOfShares);
	}

	public boolean buy(Company company, float numberOfShares) {
		try {
			company.comLock.writeLock().lock();
			float cost = company.getPrice() * numberOfShares;
			System.out.println(cost);
			if (stockExchange != null) {
				if (cost <= this.balance) {
					if (company.getAvailableShares() >= numberOfShares) {
						if (this.shares.containsKey(company)) {
							this.shares.replace(company, this.shares.get(company) + numberOfShares);
						} else {
							setStocks(company, numberOfShares);
						}
						this.stockExchange.changeAvailableSharesBy(company, -numberOfShares);
						this.balance -= cost;
						System.out.println("Bought " + numberOfShares + " shares of " + company.getName() + ". Remaining balance: " + balance);
						return true;
					}
				}
			}
			System.out.println("This client is not on any stockExchange!");
			return false;
		} finally {
			company.comLock.writeLock().unlock();
		}
	}

	public boolean sell(Company company, float numberOfShares) {
		try {
			company.comLock.writeLock().lock();
			float cost = company.getPrice() * numberOfShares;
			if (stockExchange != null) {
				if (this.shares.containsKey(company)) {
					if (this.shares.get(company) >= numberOfShares) {
						this.shares.replace(company, this.shares.get(company) - numberOfShares);
						this.stockExchange.changeAvailableSharesBy(company, numberOfShares);
						this.balance += cost;
						System.out.println("Sold " + numberOfShares + " shares of " + company.getName() + ". Remaining balance: " + balance);
						return  true;
					}
				}
			}
			System.out.println("This client is not on any stockExchange!");
			return false;
		} finally {
			company.comLock.writeLock().unlock();
		}
	}

	public boolean buyLow(Company company, float numberOfShares, float limit) throws InterruptedException {
		try {
			while (company.getPrice() >= limit) {
				Thread.sleep(100);

			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return buy(company, numberOfShares);
	}

	public boolean sellHigh(Company company, float numberOfShares, float limit) {
		try {
			while (company.getPrice() <= limit) {
				Thread.sleep(100);

			}
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
		return sell(company, numberOfShares);
	}

	public synchronized boolean deposit(float amount) {
		this.balance += amount;
		System.out.println("Balance after deposit: " + balance);
		return true;
	}

	public synchronized boolean withdraw(float amount) {
		float temp = this.balance + amount;
		if (temp >= 0) {
			this.balance = temp;
			System.out.println("Balance after withdraw: " + balance);
		}
		return false;
	}

	public void setStockExchange(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
	}

	@Override
	public void run() {
		try {
			Company com5 = new Company("com5", 100, 50);
			this.stockExchange.registerCompany(com5, 100);
			deposit((float)Math.random()*1000);
			Thread.sleep((long) 10);
			buy(com5, (float)Math.random()*10);

			withdraw((float)Math.random()*100);
		} catch (InterruptedException exception) {
			System.out.println("Interrupted!");
			return;
		}
	}
}