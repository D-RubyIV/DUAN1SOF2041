package com.raven.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;

public class Chart extends javax.swing.JPanel {

    private List<ModelLegend> legends = new ArrayList<>();
    private List<ModelChart> model = new ArrayList<>();
    private final int seriesSize = 12;
    private final int seriesSpace = 6;

    public Chart() {
        initComponents();
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                Insets insets = getInsets();
                FontMetrics ft = g2.getFontMetrics();
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;
                for (int i = 0; i < legends.size(); i++) {
                    ModelLegend legend = legends.get(i);
                    g2.setColor(legend.getColor());
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight());
                    g2.fillRect((int) (size.getX() + x), (int) (size.getY() + size.getHeight() - seriesValues), seriesSize, (int) seriesValues);
                    Rectangle2D r2 = ft.getStringBounds(String.valueOf((int) model.get(index).getValues()[i]), g2);
                    String textValue = String.valueOf((int) model.get(index).getValues()[i]);
                    int spaceLui = textValue.length() <= 2?0: (textValue.length()/2) * 4;
                    g2.drawString(textValue, (int) (size.getX() + x - spaceLui ), (int) (size.getY() + size.getHeight() - seriesValues - 2)); //Cuuuuuuuuuuuuuuuuuuuuuuuuuu
                    x += seriesSpace + seriesSize;
                }
            }
        });
    }

    public void addLegend(String name, Color color) {
        ModelLegend data = new ModelLegend(name, color);
        legends.add(data);
        panelLegend.add(new LegendItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }

    public void setWidth(int width) {
        Dimension newSize = new Dimension(width, blankPlotChart.getHeight());
        blankPlotChart.setPreferredSize(newSize);
        System.out.println("MAX BLANK SIZE: " + blankPlotChart.getMaximumSize());
        jScrollPane1.getViewport().revalidate();
        jScrollPane1.getViewport().repaint();
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    }

    public void clear() {
        panelLegend.removeAll();
        legends.clear();
        model.clear();
    }

    public void scrollToBottomWithAnimation() {
        JScrollPane scrollPane = jScrollPane1;
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();

        int animationDuration = 1000; // in milliseconds
        int steps = 100;
        int delay = animationDuration / steps;

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                horizontalScrollBar.setValue(10);
                horizontalScrollBar.setValue(0);
                ((Timer) e.getSource()).stop();
                return;
            }
        });

        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLegend = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        blankPlotChart = new com.raven.chart.BlankPlotChart();

        setBackground(new java.awt.Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        blankPlotChart.setMaximumSize(new java.awt.Dimension(999999999, 999999999));
        jScrollPane1.setViewportView(blankPlotChart);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.chart.BlankPlotChart blankPlotChart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
