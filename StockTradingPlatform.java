import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;


public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TradingPlatform platform = new TradingPlatform(10000.00); // Starting with $10,000 virtual cash
        
        System.out.println("=============================================");
        System.out.println("   WELCOME TO THE CODEALPHA TRADING PLATFORM  ");
        System.out.println("=============================================");

        while (true) {
            platform.displayMarket();
            System.out.println("\n[1] Buy Stock  [2] Sell Stock  [3] View Portfolio  [4] Exit");
            System.out.print("Choose an option: ");
            
            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid option! Please enter a number.");
                scanner.next(); // clear invalid input
                continue;
            }
            
            if (choice == 4) {
                System.out.println("\nThank you for trading with CodeAlpha! Final portfolio value logged.");
                break;
            }
            
            switch (choice) {
                case 1: // BUY
                    System.out.print("Enter Stock Symbol (e.g., AAPL, GOOG, TSLA): ");
                    String buySymbol = scanner.next().toUpperCase();
                    System.out.print("Enter Quantity to Buy: ");
                    int buyQty = scanner.nextInt();
                    platform.buyStock(buySymbol, buyQty);
                    break;
                    
                case 2: // SELL
                    System.out.print("Enter Stock Symbol: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    System.out.print("Enter Quantity to Sell: ");
                    int sellQty = scanner.nextInt();
                    platform.sellStock(sellSymbol, sellQty);
                    break;
                    
                case 3: // PORTFOLIO
                    platform.displayPortfolio();
                    break;
                    
                default:
                    System.out.println("Invalid option selection! Try again.");
            }
            
            // Simulate market movement after every action to make it dynamic!
            platform.updateMarketPrices();
            System.out.println("\n--- Press Enter to continue ---");
            scanner.nextLine(); // catch leftover newline
            scanner.nextLine(); // wait for user input
        }
        scanner.close();
    }
}

// Class to represent a individual Stock
class Stock {
    private String symbol;
    private String name;
    private double currentPrice;

    public Stock(String symbol, String name, double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = initialPrice;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getCurrentPrice() { return currentPrice; }
    
    public void changePrice(double changePercent) {
        this.currentPrice += this.currentPrice * changePercent;
        if (this.currentPrice < 1.0) this.currentPrice = 1.0; // Prevent stock from hitting zero
    }
}

// Class handling platform operations, market data, and user funds
class TradingPlatform {
    private Map<String, Stock> marketStocks;
    private Map<String, Integer> userPortfolio; // Tracks Symbol -> Quantity Owned
    private double cashBalance;
    private Random random;

    public TradingPlatform(double startingCash) {
        this.cashBalance = startingCash;
        this.marketStocks = new HashMap<>();
        this.userPortfolio = new HashMap<>();
        this.random = new Random();
        
        // Initialize the platform with 4 major stocks
        marketStocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 175.50));
        marketStocks.put("GOOG", new Stock("GOOG", "Alphabet Inc.", 150.25));
        marketStocks.put("TSLA", new Stock("TSLA", "Tesla Motors", 185.10));
        marketStocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 178.00));
    }

    // Dynamic market engine: Fluctuate prices by up to +/- 3%
    public void updateMarketPrices() {
        for (Stock stock : marketStocks.values()) {
            double percentChange = (random.nextDouble() * 0.06) - 0.03; // value between -0.03 and +0.03
            stock.changePrice(percentChange);
        }
        System.out.println("\n[System Notification] Market just ticked! Prices updated updates.");
    }

    public void displayMarket() {
        System.out.println("\n--- LIVE MARKET DATA ---");
        System.out.printf("%-8s %-18s %-10s\n", "Symbol", "Company Name", "Live Price");
        System.out.println("----------------------------------------");
        for (Stock stock : marketStocks.values()) {
            System.out.printf("%-8s %-18s $%-10.2f\n", stock.getSymbol(), stock.getName(), stock.getCurrentPrice());
        }
    }

    public void buyStock(String symbol, int quantity) {
        if (!marketStocks.containsKey(symbol)) {
            System.out.println("Error: Stock symbol not found in our market database.");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Error: Buying quantity must be greater than zero.");
            return;
        }

        Stock stock = marketStocks.get(symbol);
        double totalCost = stock.getCurrentPrice() * quantity;

        if (totalCost > cashBalance) {
            System.out.printf("Transaction Denied: Insufficient cash funds. Needed: $%.2f, Available: $%.2f\n", totalCost, cashBalance);
        } else {
            cashBalance -= totalCost;
            userPortfolio.put(symbol, userPortfolio.getOrDefault(symbol, 0) + quantity);
            System.out.printf("Success! Purchased %d shares of %s for total of $%.2f\n", quantity, symbol, totalCost);
        }
    }

    public void sellStock(String symbol, int quantity) {
        if (!userPortfolio.containsKey(symbol) || userPortfolio.get(symbol) < quantity) {
            System.out.println("Transaction Denied: You don't own enough shares of this asset.");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Error: Selling quantity must be greater than zero.");
            return;
        }

        Stock stock = marketStocks.get(symbol);
        double saleProceeds = stock.getCurrentPrice() * quantity;

        cashBalance += saleProceeds;
        int remainingShares = userPortfolio.get(symbol) - quantity;
        
        if (remainingShares == 0) {
            userPortfolio.remove(symbol);
        } else {
            userPortfolio.put(symbol, remainingShares);
        }
        System.out.printf("Success! Sold %d shares of %s for total of $%.2f\n", quantity, symbol, saleProceeds);
    }

    public void displayPortfolio() {
        System.out.println("\n=============================================");
        System.out.println("             USER ASSET PORTFOLIO            ");
        System.out.println("=============================================");
        System.out.printf("Available Virtual Cash Balance: $%.2f\n\n", cashBalance);
        
        if (userPortfolio.isEmpty()) {
            System.out.println("Holdings Status: No active positions held at this moment.");
            System.out.println("Total Portfolio Net Assets Value: $" + String.format("%.2f", cashBalance));
            return;
        }

        System.out.printf("%-8s %-12s %-12s %-12s\n", "Symbol", "Shares Owned", "Current Price", "Asset Value");
        System.out.println("-------------------------------------------------");
        
        double totalAssetValue = 0;
        for (Map.Entry<String, Integer> entry : userPortfolio.entrySet()) {
            String symbol = entry.getKey();
            int shares = entry.getValue();
            double currentPrice = marketStocks.get(symbol).getCurrentPrice();
            double value = shares * currentPrice;
            totalAssetValue += value;
            
            System.out.printf("%-8s %-12d $%-11.2f $%-11.2f\n", symbol, shares, currentPrice, value);
        }
        
        double totalNetWorth = cashBalance + totalAssetValue;
        System.out.println("-------------------------------------------------");
        System.out.printf("Total Value of Equity Stock Holdings:  $%.2f\n", totalAssetValue);
        System.out.printf("Total Account Net Portfolio Valuation: $%.2f\n", totalNetWorth);
    }
}
