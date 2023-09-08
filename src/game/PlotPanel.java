package game;

import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class PlotPanel extends ApplicationFrame {

    private final XYSeries trainingSeries;
    private final XYSeries benchmarkSeries;

    public PlotPanel(final String title, final int epochs) {
        super(title);
        trainingSeries = new XYSeries("Training");
        benchmarkSeries = new XYSeries("Benchmark");

        final XYSeriesCollection data = new XYSeriesCollection();
        data.addSeries(trainingSeries);
        data.addSeries(benchmarkSeries);

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Training & benchmark",
                "epoch",
                "win/game",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        ValueAxis yAxis = plot.getRangeAxis();
        xAxis.setRange(0, epochs);
        yAxis.setRange(0, 100);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public void addTrainingData(double x, double y) {
        trainingSeries.add(x, y);
    }

    public void addBenchmarkData(double x, double y) {
        benchmarkSeries.add(x, y);
    }
}
