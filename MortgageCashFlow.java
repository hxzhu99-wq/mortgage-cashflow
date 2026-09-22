// Purpose: The Mortgage Cash Flow Planner is for mortgage calculation per period.

// Input: Purchase Price (Full); Coverage Rate (%); Term (Years);
// Frequency; Annual Mortgage Rate (%)

// Output: A table shows loan owner payment over the term period & divide between
// principal payment vs. interest payment

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MortgageCashFlow {

    // ------- Field ------------
    private double purchasePrice;
    private double coverageRate;
    private double annualRate;
    private int frequency;
    private int years;

    // -------Constructor-------
    public MortgageCashFlow(double purchasePrice, double coverageRate,
                            double annualRate, int frequency, int years) {
        this.purchasePrice = purchasePrice;
        this.coverageRate = coverageRate;
        this.annualRate = annualRate;
        this.frequency = frequency;
        this.years = years;
    }

    //-------Frequency Parser-------
    public static int parseFrequency(String input) {
        switch (input.trim().toLowerCase()) {
            case "annual":
            case "annually":
            case "yearly":
                return 1;
            case "semi-annual":
            case "semiannual":
            case "semi-annually":
            case "semiannually":
            case "half-yearly":
                return 2;
            case "quarterly":
            case "quarter":
                return 4;
            case "monthly":
            case "month":
                return 12;
            case "biweekly":
            case "bi-weekly":
                return 26;
            case "weekly":
                return 52;
            case "daily":
                return 365;
            default:
                throw new IllegalArgumentException(
                        "Unknown frequency: '" + input + "'. " +
                                "Use: annual, semi-annual, quarterly, monthly, weekly, daily.");
        }
    }

    // -------Core: Loan Payment-------
    public double getLoanAmount() {
        return purchasePrice * coverageRate;
    }

    // -------Core: Periodic Payment-------
    public double getPayment() {
        double r = annualRate / frequency;
        int n = years * frequency;
        double loan = getLoanAmount();

        if (r == 0) return loan / n;
        double factor = Math.pow(1 + r, n);
        return loan * (r * factor) / (factor - 1);
    }

    //-------Amortization Schedule-------
    public List<Payment> buildSchedule() {
        List<Payment> schedule = new ArrayList<>();

        double balance = getLoanAmount();
        double payment = getPayment();
        double r = annualRate / frequency;
        int n = years * frequency;

        for (int period = 1; period <= n; period++) {
            double interest = balance * r;
            double principal = payment - interest;
            balance -= principal;

            if (period == n) {
                principal += balance;
                balance = 0;
            }

            double ratio = (principal == 0) ? 0 : interest / principal;
            schedule.add(new Payment(period, payment, interest, principal, balance, ratio));
        }
        return schedule;
    }

    //-------Printing-------
    public void printAmortizationTable() {
        List<Payment> schedule = buildSchedule();

        System.out.printf("%-8s %-12s %-12s %-12s %-14s %-10s%n",
                "Period", "Payment", "Interest", "Principal", "Balance", "Ratio");
        System.out.println("-".repeat(75));

        for (Payment p : schedule) {
            System.out.printf("%-8d %-12.2f %-12.2f %-12.2f %-14.2f %.2f:1%n",
                    p.period, p.payment, p.interest,
                    p.principal, p.balance, p.ratio);
        }
    }

    public void printYearlySummary() {
        List<Payment> schedule = buildSchedule();
        double yearlyInterest = 0;
        double yearlyPrincipal = 0;

        System.out.printf("%-6s %-14s %-14s %-14s%n",
                "Year", "Interest", "Principal", "Balance");
        System.out.println("-".repeat(50));

        for (int i = 0; i < schedule.size(); i++) {
            Payment p = schedule.get(i);
            yearlyInterest += p.interest;
            yearlyPrincipal += p.principal;

            if ((i + 1) % frequency == 0) {
                System.out.printf("%-6d %-14.2f %-14.2f %-14.2f%n",
                        (i + 1) / frequency,
                        yearlyInterest, yearlyPrincipal, p.balance);
                yearlyInterest = 0;
                yearlyPrincipal = 0;
            }
        }
    }

    //-------Inner Class-------
    public static class Payment {
        public int period;
        public double payment;
        public double interest;
        public double principal;
        public double balance;
        public double ratio;

        public Payment(int period, double payment, double interest,
                       double principal, double balance, double ratio) {
            this.period = period;
            this.payment = payment;
            this.interest = interest;
            this.principal = principal;
            this.balance = balance;
            this.ratio = ratio;
        }
    }

    //-------Main-------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Purchase price: ");
        double price = sc.nextDouble();

        System.out.print("Coverage rate (e.g., 0.80 for 80%): ");
        double coverage = sc.nextDouble();

        System.out.print("Annual rate (e.g., 0.0525 for 5.25%): ");
        double rate = sc.nextDouble();

        System.out.print("Frequency (annual/semi-annual/quarterly/monthly/weekly/daily): ");
        String freqText = sc.next();

        int frequency;
        try {
            frequency = parseFrequency(freqText);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            sc.close();
            return;
        }

        System.out.print("Term in years: ");
        int years = sc.nextInt();

        // Build calculator
        MortgageCashFlow mc = new MortgageCashFlow(
                price, coverage, rate, frequency, years);

        // Print summary
        System.out.printf("%nLoan Amount:      $%,.2f%n", mc.getLoanAmount());
        System.out.printf("Frequency:        %s (%d per year)%n",
                freqText, frequency);
        System.out.printf("Periodic Payment: $%,.2f%n%n", mc.getPayment());

        // Yearly summary
        System.out.println("=== Yearly Summary ===");
        mc.printYearlySummary();

        // First 3 + last 3 payments
        List<Payment> schedule = mc.buildSchedule();

        System.out.println("\n=== First 3 Payments ===");
        for (int i = 0; i < 3; i++) {
            Payment p = schedule.get(i);
            System.out.printf("Period %d: Payment=%.2f  Interest=%.2f  " +
                            "Principal=%.2f  Balance=%.2f  Ratio=%.2f:1%n",
                    p.period, p.payment, p.interest,
                    p.principal, p.balance, p.ratio);
        }

        System.out.println("\n=== Last 3 Payments ===");
        for (int i = schedule.size() - 3; i < schedule.size(); i++) {
            Payment p = schedule.get(i);
            System.out.printf("Period %d: Payment=%.2f  Interest=%.2f  " +
                            "Principal=%.2f  Balance=%.2f  Ratio=%.2f:1%n",
                    p.period, p.payment, p.interest,
                    p.principal, p.balance, p.ratio);
        }

        sc.close();
    }
}
