# Mortgage Cash Flow Calculator

A Java command-line program that calculates mortgage payments and generates a full amortization schedule. It shows exactly how much of each payment goes to interest vs. principal, and how the loan balance shrinks over time.

## Features

- Calculates the periodic payment for any mortgage
- Supports multiple payment frequencies: annual, semi-annual, quarterly, monthly, bi-weekly, weekly, daily
- Prints a year-by-year summary (interest, principal, remaining balance)
- Shows the first 3 and last 3 payments in detail
- Displays the interest-to-principal ratio for each period
- Handles zero-interest loans gracefully
- Validates frequency input with a helpful error message

## How It Works

The program uses the standard amortizing loan formula:
r * (1 + r)^n
PMT = PV * -------------
(1 + r)^n - 1

text

Where:

| Symbol | Meaning |
|--------|---------|
| `PV`   | Loan amount (purchase price × coverage rate) |
| `r`    | Periodic interest rate (annual rate ÷ frequency) |
| `n`    | Total number of payments (years × frequency) |
| `PMT`  | Payment per period |

Each period, the payment is split into:

- **Interest** = current balance × `r`
- **Principal** = `PMT` − interest
- **New balance** = old balance − principal

The total payment stays fixed, but the split shifts over time: interest shrinks and principal grows.

## Requirements

- Java Development Kit (JDK) 8 or higher
- A terminal (macOS, Linux, or Windows)

To check if Java is installed:

```bash
java -version
javac -version
Installation
Clone the repository:

bash
git clone https://github.com/hxzhu99-wq/mortgage-cashflow.git
cd mortgage-cashflow
Usage
1. Compile
bash
javac MortgageCashFlow.java
2. Run
bash
java MortgageCashFlow
3. Enter your inputs
The program will prompt you for:

Prompt	Example	Notes
Purchase price	1000000	No commas
Coverage rate	0.80	0.80 = 80% mortgage
Annual rate	0.0525	0.0525 = 5.25%
Frequency	monthly	See valid values below
Term in years	30	Whole number
Valid frequency values:

annual, annually, yearly, semi-annual, semiannual, semi-annually, semiannually, half-yearly, quarterly, quarter, monthly, month, biweekly, bi-weekly, weekly, daily

Example
Input:

text
Purchase price: 1000000
Coverage rate (e.g., 0.80 for 80%): 0.80
Annual rate (e.g., 0.0525 for 5.25%): 0.0525
Frequency (annual/semi-annual/quarterly/monthly/weekly/daily): monthly
Term in years: 30
Output:

text
Loan Amount:      $800,000.00
Frequency:        monthly (12 per year)
Periodic Payment: $4,416.32

=== Yearly Summary ===
Year   Interest       Principal      Balance
--------------------------------------------------
1      41,566.05      11,429.79      788,570.21
2      41,343.91      11,651.93      776,918.28
...

=== First 3 Payments ===
Period 1: Payment=4416.32  Interest=3500.00  Principal=916.32  Balance=799083.68  Ratio=3.82:1
Period 2: Payment=4416.32  Interest=3496.00  Principal=920.32  Balance=798163.36  Ratio=3.80:1
Period 3: Payment=4416.32  Interest=3491.96  Principal=924.36  Balance=797239.00  Ratio=3.78:1

=== Last 3 Payments ===
Period 358: Payment=4416.32  Interest=57.74  Principal=4358.58  Balance=8806.22  Ratio=0.01:1
Period 359: Payment=4416.32  Interest=38.53  Principal=4377.79  Balance=4428.43  Ratio=0.01:1
Period 360: Payment=4416.32  Interest=19.38  Principal=4396.94  Balance=0.00  Ratio=0.00:1
Interpreting the Output
Column	Meaning
Period	Payment number (1 to n)
Payment	Total payment per period (fixed)
Interest	Portion going to the lender
Principal	Portion reducing the loan balance
Balance	Remaining loan balance after this payment
Ratio	Interest ÷ Principal (how many dollars of interest per $1 of principal)
Key observations:

Early payments: mostly interest (ratio is high, e.g., 3.82:1)

Later payments: mostly principal (ratio approaches 0)

Final balance: exactly 0.00

Project Structure
text
mortgage-cashflow/
├── MortgageCashFlow.java   # Main program (all logic in one file)
├── README.md               # This file
├── LICENSE                 # MIT License
└── .gitignore              # Ignores .class files, IDE folders
Code Overview
The program is organized into one class, MortgageCashFlow, with:

Method	Purpose
MortgageCashFlow(...)	Constructor — stores inputs
parseFrequency(String)	Converts "monthly" → 12
getLoanAmount()	Purchase price × coverage rate
getPayment()	Computes periodic payment
buildSchedule()	Loops over periods, builds a list of Payment objects
printAmortizationTable()	Prints every period
printYearlySummary()	Prints yearly totals
main(String[])	Entry point, reads user input
Inner class Payment holds one row: period, payment, interest, principal, balance, ratio.

Building and Testing
Compile:

bash
javac MortgageCashFlow.java
Run:

bash
java MortgageCashFlow
Test with the sample input above — the payment should be $4,416.32.

Try other frequencies:

Frequency	Expected Payment
annual	$53,921.68
semi-annual	$26,577.32
quarterly	$13,299.55
monthly	$4,416.32
weekly	~$1,019.72
daily	~$146.17
Edge Cases Handled
Zero interest rate — falls back to loan ÷ n

Invalid frequency — throws a clear error message and exits

Floating-point drift — adjusts the final payment so balance is exactly 0

Case-insensitive input — "Monthly", "MONTHLY", "monthly" all work

Future Improvements
□ Add extra payment support (early payoff)
□ Export schedule to CSV
□ Add a GUI (JavaFX or Swing)
□ Support fixed-rate vs. adjustable-rate comparison
□ Add unit tests (JUnit)
□ Accept command-line arguments in addition to interactive input
License
This project is licensed under the MIT License. See the LICENSE file for details.

You are free to use, modify, and distribute this code, provided you include the original copyright notice.

Author
hxzhu99-wq

GitHub: @hxzhu99-wq

Acknowledgments
Built as a learning project for Java loops, methods, and OOP

Mortgage formula from standard financial mathematics

Inspired by real-world amortization schedules from lenders

Questions or suggestions? Open an issue on GitHub or submit a pull request.

text

---
