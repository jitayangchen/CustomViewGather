package com.qiuzhping.achart;

import java.text.NumberFormat;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @项目名称：AChart
 * @类名称：PieChartBuilder
 * @作者：Qiuzhping
 * @时间：2014-1-15下午11:20:48
 * @作用 ：构建饼图，并产生与用户交互，点击对应的区域能正确显示信息
 */
public class PieChartBuilder extends Activity {

	private static int[] COLORS = new int[] { Color.RED, Color.GREEN,
			Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.DKGRAY };
	double data[] = new double[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};

	private CategorySeries mSeries = new CategorySeries("");// PieChart的DataSet
															// 其实就是一些键值对，跟Map使用方法差不多

	private DefaultRenderer mRenderer = new DefaultRenderer();// PieChart的主要描绘器

	private GraphicalView mChartView;// 用来显示PieChart 需要在配置文件Manifest中添加
										// <activity
										// android:name="org.achartengine.GraphicalActivity"
										// />

	private LinearLayout mLinear;

	private static double VALUE = 0;// 总数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_chart);
		mLinear = (LinearLayout) findViewById(R.id.chart);
		mLinear.setBackgroundColor(Color.BLACK);

//		mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
		mRenderer.setStartAngle(180);// 设置为水平开始
		mRenderer.setDisplayValues(true);// 显示数据
//		mRenderer.setFitLegend(true);// 设置是否显示图例
//		mRenderer.setLegendTextSize(10);// 设置图例字体大小
//		mRenderer.setLegendHeight(10);// 设置图例高度
		mRenderer.setChartTitle("饼图示例");// 设置饼图标题

		for (int i = 0; i < data.length; i++)
			VALUE += data[i];
		for (int i = 0; i < data.length; i++) {
			mSeries.add("示例 " + (i + 1), data[i] / VALUE);// 设置种类名称和对应的数值，前面是（key,value）键值对
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			if (i < COLORS.length) {
				renderer.setColor(COLORS[i]);// 设置描绘器的颜色
			} else {
				renderer.setColor(getRandomColor());// 设置描绘器的颜色
			}
			renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
			mRenderer.setChartTitleTextSize(60);// 设置饼图标题大小
			mRenderer.addSeriesRenderer(renderer);// 将最新的描绘器添加到DefaultRenderer中
		}

		if (mChartView == null) {// 为空需要从ChartFactory获取PieChartView
			mChartView = ChartFactory.getPieChartView(getApplicationContext(),
					mSeries, mRenderer);// 构建mChartView
			mRenderer.setClickEnabled(true);// 允许点击事件
			mChartView.setOnClickListener(new View.OnClickListener() {// 具体内容
						@Override
						public void onClick(View v) {
							SeriesSelection seriesSelection = mChartView
									.getCurrentSeriesAndPoint();// 获取当前的类别和指针
							if (seriesSelection == null) {
								Toast.makeText(getApplicationContext(),
										"您未选择数据", Toast.LENGTH_SHORT).show();
							} else {
								for (int i = 0; i < mSeries.getItemCount(); i++) {
									mRenderer.getSeriesRendererAt(i)
											.setHighlighted(
													i == seriesSelection
															.getPointIndex());
								}
								mChartView.repaint();
								Toast.makeText(
										getApplicationContext(),
										"您选择的是第"
												+ (seriesSelection
														.getPointIndex() + 1)
												+ " 项 "
												+ " 百分比为  "
												+ NumberFormat
														.getPercentInstance()
														.format(seriesSelection
																.getValue()),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			mLinear.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	private int getRandomColor() {// 分别产生RBG数值
		Random random = new Random();
		int R = random.nextInt(255);
		int G = random.nextInt(255);
		int B = random.nextInt(255);
		return Color.rgb(R, G, B);
	}
}
