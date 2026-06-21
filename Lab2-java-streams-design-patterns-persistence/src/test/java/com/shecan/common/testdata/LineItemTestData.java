package com.shecan.common.testdata;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;

import java.util.Arrays;
import java.util.List;

import static com.shecan.common.testdata.ProductTestData.*;

public class LineItemTestData {

    // ===== Individual Line Items =====
    
    public static LineItem laptopQty1() {
        return new LineItem(laptop(), 1);
    }
    
    public static LineItem laptopQty2() {
        return new LineItem(laptop(), 2);
    }
    
    public static LineItem laptopQty3() {
        return new LineItem(laptop(), 3);
    }
    
    public static LineItem smartphoneQty1() {
        return new LineItem(phone(), 1);
    }
    
    public static LineItem smartphoneQty10() {
        return new LineItem(phone(), 10);
    }
    
    public static LineItem bookQty3() {
        return new LineItem(book(), 3);
    }
    
    public static LineItem bookQty8() {
        return new LineItem(book(), 8);
    }
    
    public static LineItem headphonesQty7() {
        return new LineItem(headphones(), 7);
    }
    
    public static LineItem shirtQty5() {
        return new LineItem(shirt(), 5);
    }
    
    public static LineItem shirtQty12() {
        return new LineItem(shirt(), 12);
    }

    // ===== Lists of Line Items =====
    
    public static List<LineItem> order1LineItems() {
        return Arrays.asList(
                laptopQty2(),
                smartphoneQty10(),
                bookQty3()
        );
    }
    
    public static List<LineItem> order2LineItems() {
        return Arrays.asList(
                smartphoneQty1(),
                headphonesQty7(),
                shirtQty12()
        );
    }
    
    public static List<LineItem> order3LineItems() {
        return Arrays.asList(
                laptopQty3(),
                bookQty8(),
                shirtQty5()
        );
    }
    
    public static List<LineItem> allLineItems() {
        return Arrays.asList(
                laptopQty2(),
                smartphoneQty10(),
                bookQty3(),
                smartphoneQty1(),
                headphonesQty7(),
                shirtQty12(),
                laptopQty3(),
                bookQty8(),
                shirtQty5()
        );
    }

    // ===== Items with Quantity > 5 (for filtering tests) =====
    
    public static List<LineItem> largeQuantityItems() {
        return Arrays.asList(
                smartphoneQty10(),    // 10 > 5
                headphonesQty7(),     // 7 > 5
                shirtQty12(),         // 12 > 5
                bookQty8()            // 8 > 5
        );
    }
    
    public static List<LineItem> smallQuantityItems() {
        return Arrays.asList(
                laptopQty2(),         // 2 <= 5
                laptopQty3(),         // 3 <= 5
                bookQty3(),           // 3 <= 5
                smartphoneQty1(),     // 1 <= 5
                shirtQty5()           // 5 <= 5
        );
    }
}