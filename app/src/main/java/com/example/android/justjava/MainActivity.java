package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 0;
    String shownPrice;
    final int standardCoffeePrice = 5;
    final int whippedCreamPrice = 1;
    final int chocolateToppingPrice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get user's name
        EditText usersName = findViewById(R.id.name_edit_text_view);
        String name = usersName.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Count the total price of the order
        int totalPrice = calculateCost(hasWhippedCream, hasChocolate);

        // Make shownPrice with currency that depends on Locale
        Locale currentLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(currentLocale);
        if (currentLocale.equals(Locale.US)){
            shownPrice = " " + currency.getSymbol() + totalPrice;
        } else {
            shownPrice = " " + totalPrice + " " + currency.getSymbol();
        }

        // Shows current price to user
        displayMessage(shownPrice);

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "nataliia.privezentseva@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.coffee_order));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(name, hasWhippedCream, hasChocolate));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method creates an order summary based on given total price of the order.
     */
    private String createOrderSummary(String usersName, boolean hasWhippedCream, boolean hasChocolate) {
        String toppings;
        if (hasWhippedCream && hasChocolate) {
            toppings = getString(R.string.with_two_toppings);
        } else if (!hasWhippedCream && !hasChocolate) {
            toppings = getString(R.string.without_toppings);
        } else if (!hasWhippedCream) {
            toppings = getString(R.string.with_chocolate);
        } else {
            toppings = getString(R.string.with_whipped_cream);
        }
        return getString(R.string.name) + ": " + usersName + "\n" + getString(R.string.quantity) +
                ": " + quantity + "\n" + toppings + "\n" + getString(R.string.total_price) +
                shownPrice + "\n" + getString(R.string.thank_you);
    }

    /**
     * Calculates the cost of the order based on the current quantity and given standardCoffeePrice.
     *
     * @return the total price of order
     */
    private int calculateCost(boolean hasWhippedCream, boolean hasChocolate) {
        int coffeePrice = standardCoffeePrice;
        if (hasWhippedCream){
            coffeePrice = coffeePrice + whippedCreamPrice;
        }
        if (hasChocolate){
            coffeePrice = coffeePrice + chocolateToppingPrice;
        }
        return quantity * coffeePrice;
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity < 100) {
            quantity++;
        } else {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.too_big_order);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
        } else {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.too_small_order);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format(Locale.US,"%d", number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}