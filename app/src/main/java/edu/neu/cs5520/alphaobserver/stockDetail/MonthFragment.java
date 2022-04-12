package edu.neu.cs5520.alphaobserver.stockDetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import edu.neu.cs5520.alphaobserver.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {

    LineChart chart;

    TimePeriod timePeriod = TimePeriod.ONE_MONTH;

    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        chart = (LineChart) view.findViewById(R.id.month_chart);
        ChartUtil.setChartAxis(chart);
        ChartUtil.setChartData(chart, StockModel.getData().subList(0, timePeriod.getNumberOfDays()), getContext());

        return view;
    }
}