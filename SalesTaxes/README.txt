Melissa Pangilinan
ThoughtWorks Coding Challenge for Entry Level Developers 2015
Problem 2 (SalesTaxes), Java

README

MAIN IDEA
Given a .txt file of items bought, I parsed the text file into lists, 
depending on which items were grouped together by "Input #". For example,

	Input 1
	1 book at 12.49
	1 music CD at 14.99
	1 chocolate bar at 0.85
	
was considered as one "Input #" (shopping) list. For each shopping list, I 
parsed each string ("1 book at 
$12.49") and created SalesItem objects so I 
could retrieve the quantity, name, and price of each item in a shopping list. 

Next I created a class called TaxLogic to calculate the taxes and new prices 
of each product and each shopping list using the rules stated in this problem.



CAVEATS
While I programmed my tax logic to specifically follow the rules stated 

	1. 10% basic tax on all goods EXCEPT books, food, and medical products
	2. 5% additional tax on all imported goods, NO exceptions
	3. Shelf price p of an item taxed at n% is np/100 rounded up to the nearest
		0.05 for sales tax
		
I still had discrepancies with the given output and what the output should be
for TWO products: 

INPUT (Input 2, item 2): imported bottle of perfume: 47.50
ThoughtWorks output: 54.65 (15.05% tax)
Melissa's output: 54.63 (15.01% tax)
Result: Off by $0.02

INPUT (Input 3, item 4): imported chocolate : 11.25
ThoughtWorks output: 11.85 (5.3% tax)
I get: 11.81 (4.99% tax)
Result: Off by $0.04

I tried as close as possible round to the nearest 15% and 5%, which resulted in
slightly different taxes.



HOW TO RUN CODE (Eclipse or Terminal)
1. Eclipse
	Unzip my project and import it into Eclipse by right clicking in the Package
	Explorer and selecting "Import..." to open my project in Eclipse. Open the 
	TaxLogic.java file and inside the file, right click "Run As..." => 
	"Java Application". The main() method in TaxLogic will print both the input 
	and output, as stated in the original problem. 
	
2. Terminal
	Unzip my project in terminal ("unzip ProblemTwoJava.zip")
	and enter the bin directory to run my project.
		~$ cd ProblemTwoJava
		~$ cd bin
		~$ java salesTaxes/TaxLogic



ADDITIONAL DESIGN NOTES AND CHOICE OF DATA TYPES
SalesItem.java
	-Used a BufferedReader to parse the .txt file and separate Input numbers
		from items purchased
	-SalesItem objects have 3 parameters each, quantity, name, and price, which
		are created from parsing the String in each line of INPUT.txt.
	-Price is represented with BigDecimal (as opposed to double) in order to have
		greater accuracy with computing and expressing money.
		
TaxLogic.java
	-Created a fixed array of tax exemptions (book, chocolate, chocolates, headache)
		to quickly check whether logic needs to apply 10% tax or not.
	-Variables such as salesTax and totalExpense are tracked throughout the program
		keep track of ALL taxes calculated within a shopping list.
	-calculateAllTaxes() works by calling all other methods in the class, so that 
		the user only needs to call one method for calculating the taxes of a shopping
		list.