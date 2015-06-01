package mx.citydevs.denunciaelectoral.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.internal.widget.TintEditText;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import mx.citydevs.denunciaelectoral.R;
import mx.citydevs.denunciaelectoral.typeface.TypefaceFactory;
import mx.citydevs.denunciaelectoral.utils.RegularExpressions;

/**
 * Created by zace3d on 5/31/15.
 */
public class CustomEditText extends TintEditText {
	private int regexType;
	private boolean hasSyntaxError = false;

	public CustomEditText(Context context) {
		super(context);
		if (!isInEditMode())
			init(context);
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			init(context, attrs);
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode())
			init(context, attrs);
	}

	public void init(Context context) {
		setOnTouchListener(EditTextOnTouchListener);
	}
	
	public void init(Context context, AttributeSet attrs) {
		if (attrs == null || getContext() == null) {
            return;
        }
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);

		if (typedArray == null) {
            return;
        }
		
		final int N = typedArray.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CustomEditText_typeface:
				int type = Integer.parseInt(typedArray.getString(attr));
				
				Typeface typeface = TypefaceFactory.createTypeface(context, type);
				setTypeface(typeface);
				
				break;
			}
		}
		typedArray.recycle();

		setOnTouchListener(EditTextOnTouchListener);
	}

	protected OnTouchListener EditTextOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			v.getParent().requestDisallowInterceptTouchEvent(true);
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
			}
			return false;
		}
	};

	public void setRegexType(int regexType) {
		this.regexType = regexType;

		addTextChangedListener(new MyCurrencyTextWatcher());
	}

	/**
	 * Show an error message in the {@link TintEditText}
	 * 
	 * @param errorMessage
	 *            (String) error message
	 */
	public void setErrorMessage(String errorMessage) {
		// Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
		// startAnimation(shake);
			
		int errorColor = Color.WHITE;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);

		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorMessage);
		ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
		setError(ssbuilder);
	}

	public void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindowToken(), 0);
	}

	public boolean isEmpty() {
		return getText().toString().trim().length() == 0;
	}

	public class MyCurrencyTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}

		@Override
		public void afterTextChanged(Editable s) {
			String expression = s.toString();
			
			if (!expression.equals("")) {
				if (regexType == RegularExpressions.KEY_IS_STRING) {
					if (!RegularExpressions.isString(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_string));
						
						hasSyntaxError = true;
					} else {
						setError(null);
						
						hasSyntaxError = false;
					}
					
				} else if (regexType == RegularExpressions.KEY_IS_EMAIL) {
					if (!RegularExpressions.isEmail(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_email));
						
						hasSyntaxError = true;
					} else {
						setError(null);
						
						hasSyntaxError = false;
					}
					
				} else if (regexType == RegularExpressions.KEY_IS_NUMBER) {
					if (!RegularExpressions.isNumber(expression)) {
						setErrorMessage(getContext().getString(R.string.edittext_error_number));
						
						hasSyntaxError = true;
					} else {
						setError(null);
						
						hasSyntaxError = false;
					}
				}
			}
		}
	}
	
	public boolean hasSyntaxError() {
		return hasSyntaxError;
	}
}