package com.example.dhanify;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.Random;

public class SimulationActivity extends AppCompatActivity {

    private TextView balanceTextView, priceTextView;
    private Button upButton, downButton;
    private CandleStickChart candleChart;

    private int currentPrice = 100;
    private int coins;
    private Random random = new Random();
    private Handler handler = new Handler();
    private ArrayList<CandleEntry> priceEntries = new ArrayList<>();
    private int timeIndex = 0; // To track the x-values for the chart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        balanceTextView = findViewById(R.id.balance_text);
        priceTextView = findViewById(R.id.price_text);
        upButton = findViewById(R.id.up_button);
        downButton = findViewById(R.id.down_button);
        candleChart = findViewById(R.id.candlestick_chart);

        loadCoins();
        updatePrice();
        setupChart();

        upButton.setOnClickListener(v -> placeBet(true));
        downButton.setOnClickListener(v -> placeBet(false));

        startPriceSimulation();
    }

    private void loadCoins() {
        SharedPreferences prefs = getSharedPreferences("DhanifyPrefs", MODE_PRIVATE);
        coins = prefs.getInt("coins", 0);
        balanceTextView.setText("Coins: " + coins);
    }

    private void saveCoins(int newBalance) {
        SharedPreferences prefs = getSharedPreferences("DhanifyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("coins", newBalance);
        editor.apply();
        coins = newBalance;
        balanceTextView.setText("Coins: " + coins);
    }

    private void updatePrice() {
        priceTextView.setText("Market Price: " + currentPrice);
    }

    private void placeBet(boolean betUp) {
        if (coins < 10) {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
            return;
        }

        int previousPrice = currentPrice;
        simulateMarket();

        boolean won = (betUp && currentPrice > previousPrice) || (!betUp && currentPrice < previousPrice);

        if (won) {
            saveCoins(coins + 20);
            Toast.makeText(this, "Correct! You won 20 coins!", Toast.LENGTH_SHORT).show();
        } else {
            saveCoins(coins - 10);
            Toast.makeText(this, "Wrong! You lost 10 coins.", Toast.LENGTH_SHORT).show();
        }
    }

    private void simulateMarket() {
        int oldPrice = currentPrice;
        int high = oldPrice + random.nextInt(6);
        int low = oldPrice - random.nextInt(6);
        int open = oldPrice;
        currentPrice += random.nextInt(11) - 5; // Simulate price change
        int close = currentPrice;

        priceEntries.add(new CandleEntry(timeIndex++, high, low, open, close));
        if (priceEntries.size() > 20) { // Keep last 20 entries
            priceEntries.remove(0);
        }

        updateChart();
        updatePrice();
    }

    private void startPriceSimulation() {
        handler.postDelayed(() -> {
            simulateMarket();
            startPriceSimulation();
        }, 5000);
    }

    private void setupChart() {
        if (priceEntries != null && !priceEntries.isEmpty()) { // ✅ Prevents crash
            CandleDataSet dataSet = new CandleDataSet(priceEntries, "Market Data");
            dataSet.setColor(Color.rgb(80, 80, 80));
            dataSet.setShadowColor(Color.DKGRAY);
            dataSet.setShadowWidth(0.7f);
            dataSet.setDecreasingColor(Color.RED);
            dataSet.setIncreasingColor(Color.GREEN);
            dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            dataSet.setIncreasingPaintStyle(Paint.Style.FILL);

            CandleData data = new CandleData(dataSet);
            candleChart.setData(data);
            candleChart.invalidate(); // Refresh chart
        } else {
            Log.e("ChartError", "No data available for chart.");
            Toast.makeText(this, "No data available for simulation", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateChart() {
        if (priceEntries != null && !priceEntries.isEmpty()) { // ✅ Prevents crash
            CandleDataSet dataSet = new CandleDataSet(priceEntries, "Market Data");
            dataSet.setColor(Color.rgb(80, 80, 80));
            dataSet.setShadowColor(Color.DKGRAY);
            dataSet.setShadowWidth(0.7f);
            dataSet.setDecreasingColor(Color.RED);
            dataSet.setIncreasingColor(Color.GREEN);
            dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            dataSet.setIncreasingPaintStyle(Paint.Style.FILL);

            CandleData data = new CandleData(dataSet);
            candleChart.setData(data);
            candleChart.invalidate(); // Refresh chart
        } else {
            Log.e("ChartError", "No data available for chart.");
            Toast.makeText(this, "No data available for simulation", Toast.LENGTH_SHORT).show();
        }
    }
}
