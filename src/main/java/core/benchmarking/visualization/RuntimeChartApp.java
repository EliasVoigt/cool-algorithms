package main.java.core.benchmarking.visualization;

import main.java.core.Algorithm;
import main.java.core.benchmarking.results.ScalingResult;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class RuntimeChartApp {

    private static final AtomicBoolean toolkitStarted = new AtomicBoolean(false);

    private static void ensureToolkitStarted() {
        if (toolkitStarted.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Don't shut the toolkit down just because a window got closed.
            Platform.setImplicitExit(false);
        }
    }

    public static void showFigure(ScalingResult<?, ?> r, String chartTitle) {
        ensureToolkitStarted();
        Platform.runLater(() -> {
            LineChart<Number, Number> chart = buildChart(r, chartTitle);
            Scene scene = new Scene(chart, 800, 500);
            Stage stage = new Stage();
            stage.setTitle(chartTitle);
            stage.setScene(scene);
            stage.show();
        });
    }

    public static void saveFigure(ScalingResult<?, ?> r, String fileName) {
        ensureToolkitStarted();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                LineChart<Number, Number> chart = buildChart(r, fileName);
                new Scene(chart, 800, 500); // attach to a scene so CSS/layout resolve correctly
                chart.applyCss();
                chart.layout();
                chart.layout();

                Class<?> problemClass = r.averageTimingsByAlgorithm().keySet().iterator().next().getClass();
                Path dir = Path.of("src", problemClass.getPackageName().replace('.', File.separatorChar));
                Files.createDirectories(dir);

                ImageIO.write(
                        SwingFXUtils.fromFXImage(chart.snapshot(null, null), null),
                        "png",
                        dir.resolve(fileName + ".png").toFile()
                );
            } catch (IOException e) {
                throw new RuntimeException("Failed to save chart image: " + fileName, e);
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void shutdown() {
        if (toolkitStarted.get()) {
            Platform.exit();
        }
    }

    private static LineChart<Number, Number> buildChart(ScalingResult<?, ?> result, String title) {
        int[] sizes = result.inputSizes();

        LineChart<Number, Number> chart = new LineChart<>(new NumberAxis(), new NumberAxis());
        chart.setTitle(title);

        for (var entry : result.averageTimingsByAlgorithm().entrySet()) {
            Algorithm<?, ?> algorithm = entry.getKey();
            double[] timings = entry.getValue();

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(algorithm.toString());
            for (int i = 0; i < sizes.length; i++) {
                series.getData().add(new XYChart.Data<>(sizes[i], timings[i]));
            }
            chart.getData().add(series);
        }
        return chart;
    }
}