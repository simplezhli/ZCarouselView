# ZCarouselView

现在还是有一些bug，比如滑动时有闪动现象。希望有牛帮忙解决问题！

使用方法：


1.代码中进行初始化

CarouselView carouselView=(CarouselView) findViewById(R.id.cv);
//		carouselView.setImageDrawable_one(R.drawable.aa);
//		carouselView.setImageDrawable_two(R.drawable.bb);
//		carouselView.setImageDrawable_three(R.drawable.cc);
		carouselView.setOnImageClickListener(new ImageOnClickListener() {
			
		@Override
			public void image_left_Click() {
				Toast.makeText(MainActivity.this, "点击了1", Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void image_middle_Click() {
				Toast.makeText(MainActivity.this, "点击了2", Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void image_right_Click() {
				Toast.makeText(MainActivity.this, "点击了3", Toast.LENGTH_LONG).show();
				
			}
		});
		
		
		
		
		
		2.布局中添加
		
		
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:cv="http://schemas.android.com/apk/res-auto"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <com.zl.carouselview.CarouselView
        android:id="@+id/cv"
        android:layout_width="fill_parent"
        android:layout_height="140dp"
        cv:image_left="@drawable/aa"
        cv:image_middle="@drawable/bb"
        cv:image_right="@drawable/cc" />

</LinearLayout>
