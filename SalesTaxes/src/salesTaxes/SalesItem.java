package salesTaxes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SalesItem {
	
	private BigDecimal quantity;
	private String itemName;
	private BigDecimal price;
	private BigDecimal tax = BigDecimal.ONE;
	private NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
    private List<List<SalesItem>> allSalesLists = new ArrayList<List<SalesItem>>();

	/*
	 * Parse the INPUT.txt file and create java objects to carry out tax logic.
	 * 
	 * If first.length >2, then the line is an item. Add it to a list of inputItems.
	 * Otherwise if it's a blank space, create a new list of shopping items.
	 * Add all shopping lists to a grand list of allInputStrings and then
	 * create shopping lists with SalesItem objects instead of strings.
	 */
	public List<List<SalesItem>> read(boolean print, String filePath) throws IOException {
		List<String> inputItems = new ArrayList<String>();
        List<List<String>> allInputStrings = new ArrayList<List<String>>();

		try {
            FileInputStream fstream = new FileInputStream(new File(filePath));
            DataInputStream dis = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String str;
            
            while ((str = br.readLine()) != null) {
            	String[] first = str.split("\\s");
    	
            	if (first.length > 2) {
            		inputItems.add(str);
            		
            	} else if (first[0].equals("")) {
            		
            		if (!inputItems.isEmpty()) {
            			allInputStrings.add(inputItems);          			
                    	List<SalesItem> stringToItemObject = setAllSalesItems(inputItems);
                    	allSalesLists.add(stringToItemObject);
            			
            		}
            		inputItems.clear();
            	} 
            }
                 
            //for testing purposes
            if (print) {
	            System.out.println("INPUT");            
	            for (int i = 1; i < allSalesLists.size()+1; i++) {
	            	System.out.println();
	            	System.out.println("Input "+ i);
	            	for (SalesItem c : allSalesLists.get(i-1)) 
	    				System.out.println(c.getQuantity().intValue()+" "+c.getItemName()+" at "+n.format(c.getPrice()));
	            }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return allSalesLists;
	}	
	
	/*
	 * SalesItem constructors. I created SalesItem objects in order to retrieve
	 * quantities, names, and prices of each item.
	 */
	public SalesItem() {}
	
	public SalesItem(BigDecimal q, String n, BigDecimal p) {
		this.quantity = q;
		this.itemName = n;		
		this.price = p;
	}
	
	public List<SalesItem> setAllSalesItems(List<String> args) {
		List<SalesItem> salesObjects = new ArrayList<SalesItem>();
		for (String arg : args) {
			salesObjects.add(createSalesItem(arg));
		}
		return salesObjects;		
	}
	
	/*
	 * createSalesItem() rules:
	 * 
	 * 1. The first character of an input item is always the quantity.
	 * 2. All strings after the quantity and before "at 00.00" are assumed
	 * to be the name of the product.
	 * 3. Strings after "at" are the price of the product.
	 * 
	 * return: new SalesItem.
	 */
	public SalesItem createSalesItem(String unparsedItem) {
		String[] input = unparsedItem.split("\\s");
		quantity = BigDecimal.valueOf(Double.parseDouble(input[0]));
		
		input = unparsedItem.split("\\d | at");
		itemName = input[1];
		price = BigDecimal.valueOf(Double.parseDouble(input[2]));
		
		return new SalesItem(quantity, itemName, price);
	}
		
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	/*
	 * I made sure prices and taxes never exceeded more than 2 decimal places.
	 * I used the BigDecimal class to express prices and perform calculations,
	 * but when I print INPUT/OUTPUT, I format all BigDecimal objects to money
	 * ($00.00). 
	 */
	public BigDecimal getPrice() {		
		return price.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getTax() {
		return tax.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
		
	public static void main(String[] args) {
		SalesItem s = new SalesItem();

		try {
			s.read(true, "/Users/melissapangilinan/Documents/eclipse workspace/ProblemTwoJava/src/salesTaxes/INPUT.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
