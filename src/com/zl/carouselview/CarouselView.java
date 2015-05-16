package com.zl.carouselview;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
 
public class CarouselView extends RelativeLayout {

	int width;// 屏幕的宽度（像素）
	int height;// 屏幕高度（像素） 、。
	
	// 三张轮播图片
	private ImageView iv_cv_left;
	private ImageView iv_cv_middle;
	private ImageView iv_cv_right;

	private Drawable image_left;
	private Drawable image_middle;
	private Drawable image_right;

	private Context context;

	int flag = 1;// 控制轮播顺序参数

	private ImageOnClickListener imageOnClickListener; // 轮播图点击监听

	int rightX = 0; // 3号轮播图最新值
	int rightXs = 0;// 3号轮播图上一保留值
	int middleX = 0;// 2号轮播图最新值
	int middleXs = 0;// 2号轮播图上一保留值

	private Status status;

	public enum Status {// 枚举出轮播图right状态，用于判断中间轮播图的操作
		Open, Close;
	}

	public boolean isOpen() {// 轮播图right是否打开
		return status == Status.Open;
	}

	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		@SuppressLint({ "ResourceAsColor", "NewApi" })
		@Override
		public void handleMessage(android.os.Message msg) {
			if (flag % 4 == 1) {//轮播图right向右移动（关闭）
				ObjectAnimator
						.ofFloat(iv_cv_right, "translationX", 0, width / 10 * 7)
						.setDuration(1000).start();
				status = Status.Close;

			}
			if (flag % 4 == 2) {//轮播图middle向右移动（关闭）
				ObjectAnimator
						.ofFloat(iv_cv_middle, "translationX", 0,
								width / 10 * 7).setDuration(1000).start();

				status = Status.Close;
			}
			if (flag % 4 == 3) {//轮播图middle向左移动（还原）
				ObjectAnimator
						.ofFloat(iv_cv_middle, "translationX", width / 10 * 7,
								0).setDuration(1000).start();
				status = Status.Close;
			}
			if (flag % 4 == 0) {//轮播图right向左移动（还原）
				ObjectAnimator
						.ofFloat(iv_cv_right, "translationX", width / 10 * 7, 0)
						.setDuration(1000).start();
				status = Status.Open;

			}
			flag++;
			mHandler.sendEmptyMessageDelayed(1, 4000);

		};
	};

	public CarouselView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.CarouselView);
		// 让自定义控件里的属性和attrs文件中的属性关联起来，这样就可以在xml文件中直接设置该控件的属性了

		this.image_left = ta.getDrawable(R.styleable.CarouselView_image_left);
		this.image_middle = ta
				.getDrawable(R.styleable.CarouselView_image_middle);
		this.image_right = ta.getDrawable(R.styleable.CarouselView_image_right);
		ta.recycle();
		initView(context);

	}

	/**
	 * 初始化方法
	 */
	@SuppressLint("ClickableViewAccessibility") private void initView(Context context) {
		this.context = context;

		iv_cv_left = new ImageView(context);
		iv_cv_middle = new ImageView(context);
		iv_cv_right = new ImageView(context);

		iv_cv_left.setScaleType(ScaleType.CENTER_CROP);
		iv_cv_middle.setScaleType(ScaleType.CENTER_CROP);
		iv_cv_right.setScaleType(ScaleType.CENTER_CROP);

		addView(iv_cv_left);
		addView(iv_cv_middle);
		addView(iv_cv_right);
		iv_cv_left.setImageDrawable(image_left);
		iv_cv_middle.setImageDrawable(image_middle);
		iv_cv_right.setImageDrawable(image_right);
		status = Status.Open;//初始打开状态
		mHandler.sendEmptyMessageDelayed(1, 1000);//开始自动轮播

		// 点击事件监听
		iv_cv_left.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_middle.getX() < width / 10 * 3) {
					ObjectAnimator
							.ofFloat(iv_cv_middle, "translationX", 0,
									width / 10 * 7).setDuration(1000).start();
					if (iv_cv_right.getX() < width / 10 * 3) {
						ObjectAnimator
								.ofFloat(iv_cv_right, "translationX", 0,
										width / 10 * 7).setDuration(1000)
								.start();
						status = Status.Close;
					}
				} else {
					imageOnClickListener.image_left_Click();

				}
				flag = 3;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});

		iv_cv_middle.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_right.getX() > width / 10 * 3
						&& iv_cv_middle.getX() < width / 10 * 3) {
					imageOnClickListener.image_middle_Click();

				} else {
					if (iv_cv_middle.getX() < width / 10 * 3) {

						ObjectAnimator
								.ofFloat(iv_cv_right, "translationX", 0,
										width / 10 * 7).setDuration(1000)
								.start();
						status = Status.Close;
					} else {
						ObjectAnimator
								.ofFloat(iv_cv_middle, "translationX",
										width / 10 * 7, 0).setDuration(1000)
								.start();
					}
				}
				flag = 2;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});

		iv_cv_right.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				mHandler.removeMessages(1);
				if (iv_cv_right.getX() < width / 10 * 3) {
					imageOnClickListener.image_right_Click();

				} else {

					ObjectAnimator
							.ofFloat(iv_cv_right, "translationX",
									width / 10 * 7, 0).setDuration(1000)
							.start();
					status = Status.Open;
					if (iv_cv_middle.getX() > width / 10 * 3) {

						ObjectAnimator
								.ofFloat(iv_cv_middle, "translationX",
										width / 10 * 7, 0).setDuration(1000)
								.start();
					}

				}
				flag = 1;
				mHandler.sendEmptyMessageDelayed(1, 4000);

			}
		});
		iv_cv_right.setOnTouchListener(new OnTouchListener() {

			@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (!isTestCompete) {
					getEventType(ev);
					return false;
				}

				if (isleftrightEvent) {

					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:

						break;
					case MotionEvent.ACTION_MOVE:

						mHandler.removeMessages(1);
						
						rightXs = rightX;
						rightX += (int) ev.getX() - point.x;

						if (rightX >= 0 && rightX <= width / 10 * 7) {

							ViewHelper.setTranslationX(iv_cv_right, rightX);

						} else {
							if (rightX > width / 10 * 7) {
								rightX = width / 10 * 7;
							} else {
								rightX = 0;
							}

							ViewHelper.setTranslationX(iv_cv_right, rightX);

						}

						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						int j = rightX - rightXs;
						if (j > 0) {
							if (rightX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, width / 10 * 7)
										.setDuration(400).start();
								status = Status.Close;
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, 0).setDuration(400)
										.start();
								status = Status.Open;
								flag = 1;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						} else if (j < 0) {
							if (rightX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, width / 10 * 7)
										.setDuration(400).start();
								status = Status.Close;
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_right, "translationX",
												rightX, 0).setDuration(400)
										.start();
								status = Status.Open;
								flag = 1;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						}

						isleftrightEvent = false;
						isTestCompete = false;
						break;
					}
				} else {
					switch (ev.getActionMasked()) {
					case MotionEvent.ACTION_UP:
						isleftrightEvent = false;
						isTestCompete = false;
						break;

					default:
						break;
					}
				}
				return false;
			}
		});
		iv_cv_middle.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if (isOpen()) {
					return false;
				}
				if (!isTestCompete) {
					getEventType(ev);
					return false;
				}

				if (isleftrightEvent) {

					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:

						break;
					case MotionEvent.ACTION_MOVE:

						mHandler.removeMessages(1);
						
						middleXs = middleX;
						middleX += (int) ev.getX() - point.x;

						if (middleX >= 0 && middleX <= width / 10 * 7) {

							ViewHelper.setTranslationX(iv_cv_middle, middleX);

						} else {
							if (middleX > width / 10 * 7) {
								middleX = width / 10 * 7;
							} else {
								middleX = 0;
							}

							ViewHelper.setTranslationX(iv_cv_middle, middleX);

						}

						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						int z = middleX - middleXs;
						if (z > 0) {
							if (middleX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, width / 10 * 7)
										.setDuration(400).start();
								flag = 3;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, 0).setDuration(400)
										.start();
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						} else if (z < 0) {
							if (middleX > width / 10 * 3.5) {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, width / 10 * 7)
										.setDuration(400).start();
								flag = 2;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							} else {
								ObjectAnimator
										.ofFloat(iv_cv_middle, "translationX",
												middleX, 0).setDuration(400)
										.start();
								flag = 3;
								mHandler.sendEmptyMessageDelayed(1, 4000);
							}

						}

						isleftrightEvent = false;
						isTestCompete = false;
						break;
					}
				} else {
					switch (ev.getActionMasked()) {
					case MotionEvent.ACTION_UP:
						isleftrightEvent = false;
						isTestCompete = false;
						break;

					default:
						break;
					}
				}
				return false;
			}
		});
	}

	/**
	 * 测量子view
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
				(int) (width * 0.8f), MeasureSpec.EXACTLY);
		iv_cv_middle.measure(tempWidthMeasure, heightMeasureSpec);
		iv_cv_left.measure(tempWidthMeasure, heightMeasureSpec);
		iv_cv_right.measure(tempWidthMeasure, heightMeasureSpec);
	}

	/**
	 * 布局
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// Log.e("11111", iv_cv_middle.getMeasuredWidth()+"");
		iv_cv_middle.layout(l + iv_cv_middle.getMeasuredWidth() / 8, t, r
				- iv_cv_middle.getMeasuredWidth() / 8, b);
		iv_cv_left.layout(l, t, r - iv_cv_middle.getMeasuredWidth() / 4, b);
		iv_cv_right.layout(l + iv_cv_middle.getMeasuredWidth() / 4, t, r, b);
	}

	private boolean isTestCompete;
	private boolean isleftrightEvent;

	private Point point = new Point();
	private static final int TEST_DIS = 20;// 移动精度

	@SuppressLint("NewApi")
	private void getEventType(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			point.x = (int) ev.getX();
			point.y = (int) ev.getY();
			// super.dispatchTouchEvent(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			int dX = Math.abs((int) ev.getX() - point.x);
			int dY = Math.abs((int) ev.getY() - point.y);
			if (dX >= TEST_DIS && dX > dY) { // 左右滑动
				isleftrightEvent = true;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			} else if (dY >= TEST_DIS && dY > dX) { // 上下滑动
				isleftrightEvent = false;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// super.dispatchTouchEvent(ev);
			isleftrightEvent = false;
			isTestCompete = false;
			break;
		}
	}
	
	
	
	/**
	 * 一些暴露方法，可根据需求自行添加
	 */
	public void setImageDrawable_left(int i) {

		// 加载图片
		Picasso.with(context).load(i).into(iv_cv_left);

	}

	public void setImageDrawable_middle(int i) {

		Picasso.with(context).load(i).into(iv_cv_middle);

	}

	public void setImageDrawable_right(int i) {

		Picasso.with(context).load(i).into(iv_cv_right);

	}

	public void setImageURL_left(String url, int holder, int error) {

		// 加载图片
		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_left);

	}

	public void setImageURL_middle(String url, int holder, int error) {

		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_middle);

	}

	public void setImageURL_right(String url, int holder, int error) {

		Picasso.with(context).load(url).placeholder(holder).error(error)
				.into(iv_cv_right);

	}

	/**
	 * 设置点击事件监听
	 * 
	 */
	public void setOnImageClickListener(
			ImageOnClickListener imageOnClickListener) {
		this.imageOnClickListener = imageOnClickListener;
	}

	public interface ImageOnClickListener {
		public void image_left_Click();

		public void image_middle_Click();

		public void image_right_Click();
	}
}
