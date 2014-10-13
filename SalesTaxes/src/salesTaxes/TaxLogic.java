package salesTaxes;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import salesTaxes.SalesItem;

public class TaxLogic {

	private List<SalesItem> output = new ArrayList<SalesItem>();
	private String[] taxExemptions = new String[] {"book", "chocolate", "chocolates", "headache"};
	private BigDecimal salesTax = BigDecimal.ZERO;
	private BigDecimal totalExpense = BigDecimal.ZERO;
	private NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
		
	
	/*
	 * calculateAllTaxes() takes a list of sales items (input) and calls on other
	 * methods to calculate the sales tax and total cost of one set of items.
	 * 
	 * return: list of items with calculated tax.
	 */
	public List<SalesItem> calculateAllTaxes(List<SalesItem> input) {
		for (SalesItem i : input) {
			output.add(calculateBasicTax(i));
		}
		
		for (SalesItem outputItem: output) {
			System.out.println(outputItem.getQuantity().intValue()+" "+outputItem.getItemName()+" at "+n.format(outputItem.getPrice()));
		}
			System.out.println("Sales Taxes: "+n.format(salesTax));
			System.out.println("Total: "+n.format(totalExpense));	
			System.out.println();
		return output;
	}
	
	/*
	 * twoDecimals() is a helper method to round to the nearest 0.05 when
	 * expressing money.
	 */
	public BigDecimal twoDecimals(BigDecimal d) {
		return d.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/*
	 * calculateBasicTax() computes the standard 10% tax on all items except
	 * books, food, and medical products. It parses the name of the item to 
	 * check if standard 10% tax is applicable.
	 */
	public SalesItem calculateBasicTax(SalesItem untaxedBasic) {
		String[] parsedName = untaxedBasic.getItemName().split("\\s");
		BigDecimal itemTax = new BigDecimal("0.00");
		BigDecimal totalItemTax = new BigDecimal("0.00");
		
		for (String s : parsedName){
			if (Arrays.asList(taxExemptions).contains(s)) {
				return calculateImportTax(untaxedBasic, itemTax, parsedName);				
			} 
		}	

		BigDecimal bas = BigDecimal.valueOf(0.10);
		itemTax = twoDecimals(untaxedBasic.getPrice().multiply(bas));
		salesTax = twoDecimals(salesTax.add(itemTax));		
		totalItemTax = totalItemTax.add(itemTax);	

		return calculateImportTax(untaxedBasic, itemTax, parsedName); 
	}
	
	/*
	 * calculateImportTax() parses the name of the item to check if an 
	 * item is "imported", and then adds an additional 5% tax.
	 */
	public SalesItem calculateImportTax(SalesItem item, BigDecimal totalItemTax, String[] parsedDesc) {
		BigDecimal itemImportTax = new BigDecimal("0.00");
		
		for (int i = 0; i < parsedDesc.length; i++) {
			if (parsedDesc[i].equals("imported")) {	
				BigDecimal imp = BigDecimal.valueOf(0.05);
								
				itemImportTax = twoDecimals(item.getPrice().multiply(imp));				
				salesTax = twoDecimals(salesTax.add(itemImportTax));
				
				totalItemTax = totalItemTax.add(itemImportTax);
				return taxedPrice(item, totalItemTax);
			}
		}
		return taxedPrice(item, totalItemTax);
	}
	
	/*
	 * taxedPrice() calculates the new price of an item included with tax,
	 * and adds the new price of an item to the grand total of all expenses.
	 * 
	 * Despite the fact that all items in the input were a quantity of 1, it is
	 * important to consider the cases in which a buyer buys more than 1 of the same
	 * item. Thus, multiply newItemPrice by the item.getQuantity().
	 * 
	 * return: new SalesItem with an adjusted taxed price.
	 */
	public SalesItem taxedPrice(SalesItem subtotalItem, BigDecimal totalItemTax) {
		BigDecimal newItemPrice = subtotalItem.getQuantity().multiply(subtotalItem.getPrice().add(totalItemTax));
		SalesItem outputItem = new SalesItem(subtotalItem.getQuantity(), subtotalItem.getItemName(), newItemPrice);
		
		totalExpense = totalExpense.add(newItemPrice);
		
		return outputItem;
	}
	
	
	public static void main(String[] args) {
		SalesItem s = new SalesItem();
		List<List<SalesItem>> allInput = new ArrayList<List<SalesItem>>();
		
		System.out.println();
		try {
			allInput = s.read(true, "/Users/melissapangilinan/Documents/eclipse workspace/ProblemTwoJava/src/salesTaxes/INPUT.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("---------------------------------------");
		System.out.println();
		
		System.out.println("OUTPUT");
		System.out.println();
		System.out.println("Output 1");
		List<SalesItem> input1 = allInput.get(0);
		TaxLogic t1 = new TaxLogic();
		t1.calculateAllTaxes(input1);
		
		System.out.println("Output 2");
		List<SalesItem> input2 = allInput.get(1);
		TaxLogic t2 = new TaxLogic();
		t2.calculateAllTaxes(input2);
		
		System.out.println("Output 3");
		List<SalesItem> input3 = allInput.get(2);
		TaxLogic t3 = new TaxLogic();
		t3.calculateAllTaxes(input3);	
		
	}
}
