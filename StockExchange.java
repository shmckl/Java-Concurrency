import java.util.ArrayList;
import java.util.HashMap;

public class StockExchange {
	private HashMap<Company, Float> companies;
	private ArrayList<Client> clients;

	public StockExchange() {
		this.companies = new HashMap<Company, Float>();
		this.clients = new ArrayList<Client>();
	}

	public boolean registerCompany(Company company, float numberOfShares) {
		synchronized (companies) {
			if (company == null) {
				return false;
			}
			this.companies.put(company, numberOfShares);
			company.setTotalShares(numberOfShares);
			return true;
		}
	}

	public boolean deregisterCompany(Company company) {
		synchronized (companies) {
			if (this.companies.isEmpty()) {
				return false;
			}
			this.companies.remove(company);
			return true;
		}
	}

	public boolean addClient(Client client) {
		if (client != null) {
			this.clients.add(client);
			client.setStockExchange(this);
			return true;
		}
		return false;
	}

	public boolean removeClient(Client client) {
		if (this.clients.isEmpty()) {
			System.out.println("No clients to remove from the list!");
			return false;
		}
		this.clients.remove(client);
		client.setStockExchange(null);
		return true;
	}

	public ArrayList<Client> getClients() {
		return this.clients;
	}

	public HashMap<Company, Float> getCompanies() {
		return this.companies;
	}

	public void setPrice(Company company, float number) {
		synchronized (companies) {
			if (this.companies.containsKey(company)) {
				if (number >= 0) {
					company.setPrice(number);
				} else {
					System.out.println("Price cannot be negative!");
				}
			} else {
				System.out.println("The company is not registered!");
			}
		}
	}

	public void changePriceBy(Company company, float number) {
		synchronized (companies) {
			float temp = company.getPrice() + number;
			if (this.companies.containsKey(company)) {
				if (temp >= 0) {
					company.setPrice(temp);
					notifyAll();
				} else {
					System.out.println("Price cannot be negative!");
				}
			}
		}
	}

	public synchronized boolean changeAvailableSharesBy(Company company, Float amount) {
		synchronized (companies) {
			float newAvailable = company.getAvailableShares() + amount;
			if (newAvailable >= 0) {
				if (company.getTotalShares() >= newAvailable) {
					if (this.companies.containsKey(company)) {
						company.setAvailableShares(newAvailable);
						this.companies.replace(company, newAvailable);
						notifyAll();
						return true;
					}
				}
			}
			return false;
		}
	}

}