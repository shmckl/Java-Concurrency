public class Application {
	public static void main(String[] args) {
		Client client = new Client();
		Client client2 = new Client();
		Client client3 = new Client();
		StockExchange stex = new StockExchange();
		StockExchange stex2 = new StockExchange();
		Company com = new Company("com", 5000, 10);
		Company com2 = new Company("com2", 4612, 7);
		Company com3 = new Company("com3", 785, 45);
		Company com4 = new Company("com4", 1000, 20);
		Company com5 = new Company("com5", 100, 50);
		stex.addClient(client);
		stex.addClient(client2);
		stex2.addClient(client3);
		stex.registerCompany(com, 5000);
		stex.registerCompany(com2, 4612);
		stex.registerCompany(com3, 785);
		stex.registerCompany(com4, 1000);
		stex.registerCompany(com5, 100);

		Thread t1 = new Thread(client);
		Thread t2 = new Thread(client2);
		Thread t3 = new Thread(client3);

		System.out.println(com.getPrice());
		System.out.println(stex.getClients().toString());
		System.out.println(stex.getCompanies().toString());

		client.deposit(600);
		stex.getClients().get(0).buy(com, 50);

		System.out.println(stex.getCompanies().toString());
	}
}