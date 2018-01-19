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
    CheckBox whippedCreamCheckBox;
    CheckBox chocolateCheckBox;
    EditText usersName;

    int quantity;

    final int standardCoffeePrice = 5;
    final int whippedCreamPrice = 1;
    final int chocolateToppingPrice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialUISetup();
    }

    /**
     * This method initializes listeners for checkboxes that shows user's choice of toppings.
     */
    public void initialUISetup() {
        whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        chocolateCheckBox = findViewById(R.id.chocolate_checkbox);

        whippedCreamCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coffeeIsOrdered()) {
                    return;
                }
                displayMessage(constructTotalPrice());
            }
        });

        chocolateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coffeeIsOrdered()) {
                    return;
                }
                displayMessage(constructTotalPrice());
            }
        });
    }

    private boolean coffeeIsOrdered(){
        if (quantity == 0 && (whippedCreamCheckBox.isChecked() || chocolateCheckBox.isChecked())) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.only_toppings);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void sendOrder(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "nataliia.privezentseva@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.coffee_order));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method constructs and returns total price with currency sign, which depends on Locale.
     *
     * @return String that is a total price of the order with currency sign
     */
    private String constructTotalPrice(){
        String totalPrice;
        Locale currentLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(currentLocale);
        if (currentLocale.equals(Locale.US)){
            totalPrice = " " + currency.getSymbol() + calculateCost();
        } else {
            totalPrice = " " + calculateCost() + " " + currency.getSymbol();
        }
        return totalPrice;
    }

    /**
     * This method creates an order summary based on given total price of the order.
     *
     * @return String that contains all order details
     */
    private String createOrderSummary() {
        // Get user's name
        usersName = findViewById(R.id.name_edit_text_view);

        String toppings;
        if (whippedCreamCheckBox.isChecked() && chocolateCheckBox.isChecked()) {
            toppings = getString(R.string.with_two_toppings);
        } else if (!whippedCreamCheckBox.isChecked() && !chocolateCheckBox.isChecked()) {
            toppings = getString(R.string.without_toppings);
        } else if (!whippedCreamCheckBox.isChecked()) {
            toppings = getString(R.string.with_chocolate);
        } else {
            toppings = getString(R.string.with_whipped_cream);
        }
        return getString(R.string.order_summary) +
                "\n" + getString(R.string.name) + ": " + usersName.getText().toString() +
                "\n" + getString(R.string.quantity) + ": " + quantity +
                "\n" + toppings +
                "\n" + getString(R.string.total_price) + constructTotalPrice() +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * Calculates the cost of the order based on the current quantity and given standardCoffeePrice.
     *
     * @return the total price of order
     */
    private int calculateCost() {
        int coffeePrice = standardCoffeePrice;
        if (whippedCreamCheckBox.isChecked()){
            coffeePrice = coffeePrice + whippedCreamPrice;
        }
        if (chocolateCheckBox.isChecked()){
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
        displayMessage(constructTotalPrice());
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
        displayMessage(constructTotalPrice());
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