package com.zpxt.zhyd.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.zpxt.zhyd.R;


/**
 * 带清除功能的输入框
 * 
 * @author meng
 * 
 */

/*
 * 果我们能为右边的图片设置监听，点击右边的图片清除输入框的内容并隐藏删除图标，这样子这个小功能就迎刃而解了，
 * 可是Android并没有给允许我们给右边小图标加监听的功能
 * ，这时候你是不是发现这条路走不通呢，其实不是，我们可能模拟点击事件，用输入框的的onTouchEvent()方法来模拟，
 * 当我们触摸抬起（就是ACTION_UP的时候）的范围
 * 大于输入框左侧到清除图标左侧的距离，小与输入框左侧到清除图片右侧的距离，我们则认为是点击清除图片，当然我这里没有考虑竖直方向，只要给清除小图标就上了监听
 */
public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {

	/**
	 * 删除按钮的引用
	 */
	public Drawable mClearDrawable;
	/**
	 * 控件是否有焦点
	 */
	public boolean hasFoucs;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
		//this(context,attrs,R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			// throw new
			// NullPointerException("You can add drawableRight attribute in XML");

			mClearDrawable = getResources().getDrawable(R.mipmap.sapi_clear_btn_normal);
		}

		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	/*
	 * 设置隐藏和显示清除图标的方法，我们这里不是调用setVisibility()方法，setVisibility()这个方法是针对View的，
	 * 我们可以调用setCompoundDrawables(Drawable left, Drawable top, Drawable right,
	 * Drawable bottom)来设置上下左右的图标
	 */
	public void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		// this.setAnimation(shakeAnimation(5));
		this.startAnimation(shakeAnimation(10));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
//
// setClearIconVisible()方法，设置隐藏和显示清除图标的方法，我们这里不是调用setVisibility()方法，setVisibility()这个方法是针对View的，我们可以调用setCompoundDrawables(Drawable
// left, Drawable top, Drawable right, Drawable bottom)来设置上下左右的图标
// setOnFocusChangeListener(this)
// 为输入框设置焦点改变监听，如果输入框有焦点，我们判断输入框的值是否为空，为空就隐藏清除图标，否则就显示
// addTextChangedListener(this)
// 为输入框设置内容改变监听，其实很简单呢，当输入框里面的内容发生改变的时候，我们需要处理显示和隐藏清除小图标，里面的内容长度不为0我们就显示，否是就隐藏，但这个需要输入框有焦点我们才改变显示或者隐藏，为什么要需要焦点，比如我们一个登陆界面，我们保存了用户名和密码，在登陆界面onCreate()的时候，我们把我们保存的密码显示在用户名输入框和密码输入框里面，输入框里面内容发生改变，导致用户名输入框和密码输入框里面的清除小图标都显示了，这显然不是我们想要的效果，所以加了一个是否有焦点的判断
// setShakeAnimation()，这个方法是输入框左右抖动的方法，之前我在某个应用看到过类似的功能，当用户名错误，输入框就在哪里抖动，感觉挺好玩的，其实主要是用到一个移动动画，然后设置动画的变化率为正弦曲线
