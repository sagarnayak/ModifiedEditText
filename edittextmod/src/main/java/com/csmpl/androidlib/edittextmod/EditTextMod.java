package com.csmpl.androidlib.edittextmod;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.csmpl.androidlib.edittextmod.util.ConstsAndKw;
import com.csmpl.androidlib.edittextmod.util.Displaylog;

/**
 * Created by sagar on 11/1/2016.
 * <p>
 * modified edittext. this will contain the new style of edittext code where it will be applied for validation.
 * this is a lib for making the validatio nin edittxt more easier.
 * this will just take some attributes at the xml widget and do all the background task for you.
 * you can get the following features here-
 * <p>
 * character limit
 * first blank space validation
 * intermediate blank space validation
 * sql injection chracter validation
 * take letters only
 * take numbers only
 * do phone validation
 * do email validation
 * do name validation
 */

public class EditTextMod extends EditText {

    /*
    the number of characters to be taken.
     */
    int intCharacterLimit;

    /*
    block the first space
     */
    boolean boolBlockFirstSpace;

    /*
    block intermediate space
     */
    boolean boolBlockIntermediateSpace;

    /*
    block sql injection characters
     */
    boolean boolBlockSqlInjection;

    /*
    take only letters as input
     */
    boolean boolTakeLettersOnly;

    /*
    take only numbers for input
     */
    boolean boolTakeNumbersOnly;

    /*
    we can assign any specific validation type here like phone number, email address, name etc.
     */
    int intValidationType;

    /*
    string name for success validation
     */
    String strSuccessBroadcast = "";

    /*
    string name for failed validation
     */
    String strFailBroadcast = "";

    /*
    textwatcher for monitoring the changes in the edittext
     */
    TextWatcher textWatcher;

    /*
    context for future reference
     */
    Context context;

    /*
    string which saves the previous text
     */
    String strPreviusText = "";

    /*
    digits string
     */
    String strDigits = "";

    public EditTextMod(Context context) {
        super(context);
        Displaylog.Log("default constructor.");

        //if default constructor is called then we can just leave it as it is
    }

    public EditTextMod(Context context, AttributeSet attrs) {
        super(context, attrs);
        Displaylog.Log("constructor with attrs is called.");

        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextMod);

        //get the values assigned and then save it locally
        intCharacterLimit = ta.getInteger(R.styleable.EditTextMod_LimitInput, -1);
        boolBlockFirstSpace = ta.getBoolean(R.styleable.EditTextMod_BlockFirstSpace, false);
        boolBlockIntermediateSpace = ta.getBoolean(R.styleable.EditTextMod_BlockIntermediateSpace, false);
        boolBlockSqlInjection = ta.getBoolean(R.styleable.EditTextMod_BlockSqlInjection, false);
        boolTakeNumbersOnly = ta.getBoolean(R.styleable.EditTextMod_TakeOnlyNumbers, false);
        boolTakeLettersOnly = ta.getBoolean(R.styleable.EditTextMod_TakeOnlyLetters, false);
        intValidationType = ta.getInteger(R.styleable.EditTextMod_ValidationType, -1);
        strFailBroadcast = ta.getString(R.styleable.EditTextMod_FailBroadcast);
        strSuccessBroadcast = ta.getString(R.styleable.EditTextMod_SuccessBroadcast);

        //display a log
        Displaylog.Log("the datas that are assigned as attrs are : \n" +
                "char limit : " + intCharacterLimit + "\n" +
                "block first space : " + boolBlockFirstSpace + "\n" +
                "block intermediate space : " + boolBlockIntermediateSpace + "\n" +
                "block sql injection : " + boolBlockSqlInjection + "\n" +
                "take numbers only : " + boolTakeNumbersOnly + "\n" +
                "take letters only : " + boolTakeLettersOnly + "\n" +
                "validation type : " + intValidationType + "\n"
        );

        /*
        set the initial params. like number of chars, specific input type etc
         */
        setInitialParams();

        /*
        set the text watcher for the edittext. this will do most of the validation work on the fly.
        the sql injection chars and blank space validation will be done here.
         */
        prepareTextWatcher();

        /*
        this is the function which assigns a focus change listenr for the edittext. here the validation will
        be done for email, phone or name. of any thing is wrong then the apropriate message will be shown
        here.
         */
        setFocusChangedListener();
    }

    public EditTextMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Displaylog.Log("constructor with attrs and defstylesattrs.");
    }

    public EditTextMod(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Displaylog.Log("constructor with attrs and defstylesattrs and defstyleres.");
    }

    /*
    function to set initial validation params
     */
    private void setInitialParams() {

        //by default the char limit for phone validation is 10
        if (intValidationType == ConstsAndKw.PHONENUMBER) {
            intCharacterLimit = 10;
            setInputType(InputType.TYPE_CLASS_PHONE);
        }

        //do number validation
        if (intCharacterLimit != -1) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(intCharacterLimit)});
        }

        //take numbers only
        if (boolTakeNumbersOnly || intValidationType == ConstsAndKw.PHONENUMBER) {
            strDigits = "0123456789";
            setKeyListener(DigitsKeyListener.getInstance(strDigits));
        }

        //take letters only
        if (boolTakeLettersOnly) {
            strDigits += "abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMONPQRSTUVWXYZ";
            setKeyListener(DigitsKeyListener.getInstance(strDigits));
        }
    }

    /*
    function to prepare the textwatcher for the edittext
     */
    private void prepareTextWatcher() {

        //provide the def for textwatcher
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Displaylog.Log("on text changed : " + s.toString());

                //remove the error message if any
                setError(null);

                //un register the text watcher
                EditTextMod.super.removeTextChangedListener(textWatcher);
                Displaylog.Log("removed text watcher");

                //do first blank space validation
                if (boolBlockFirstSpace) {
                    if (getText().length() == 1) {
                        if (getText().toString() == " ") {
                            Displaylog.Log("first blank space validation fired up.");
                            setTheText("");
                        }
                    }
                }

                //do intermediate space validation
                if (boolBlockIntermediateSpace) {
                    if (getText().length() > 1) {
                        char[] tempAry = getText().toString().toCharArray();
                        if (Character.isSpaceChar(tempAry[tempAry.length - 1])) {
                            Displaylog.Log("intermediate space validation fired up.");
                            setTheText(strPreviusText);
                        }
                    }
                }

                //do sql injection validation
                if (boolBlockSqlInjection) {
                    if (getText().length() > 1) {
                        char[] tempAry = getText().toString().toCharArray();
                        String str = (String.valueOf(tempAry[tempAry.length - 1]));
                        if (str.equals("<") ||
                                str.equals(">") ||
                                str.equals("=") ||
                                str.equals("~") ||
                                str.equals("#") ||
                                str.equals("^") ||
                                str.equals("|") ||
                                str.equals("$") ||
                                str.equals("%") ||
                                str.equals("&") ||
                                str.equals("*") ||
                                str.equals("!") ||
                                str.equals("'")) {
                            Displaylog.Log("sql injection character validation fired up.");
                            setTheText(strPreviusText);
                        }
                    }
                }

                //re-assign the text watcher
                EditTextMod.super.addTextChangedListener(textWatcher);
                Displaylog.Log("assigned the text watcher");

                //save current text as previous text
                strPreviusText = getText().toString();
                Displaylog.Log("the text saved is : " + strPreviusText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //assign the text watcher to the edittext
        this.addTextChangedListener(textWatcher);
        Displaylog.Log("assigned the text watcher fot first time");
    }

    /*
    set a focus changed listener
     */
    private void setFocusChangedListener() {
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Displaylog.Log("focus changed . hasfocus : " + hasFocus);
                if (!hasFocus && doValidation()) {
                    //send a failed broadcast
                    sendFailedBroadcast();
                    EditTextMod.super.post(new Runnable() {
                        @Override
                        public void run() {
                            EditTextMod.super.requestFocus();
                        }
                    });
                    Displaylog.Log("requested for focus.");
                } else {
                    //send success broadcast
                    sendSuccessBroadcst();
                }
            }
        });
    }

    /*
    set text to with a set selection
     */
    private void setTheText(String text) {
        setText(text);
        setSelection(getText().toString().length());
    }

    /*
    do the validation according to the validation type provided
     */
    private boolean doValidation() {
        switch (intValidationType) {
            case ConstsAndKw.PHONENUMBER:
                if (isPhoneNumberAlright()) {
                    return false;
                } else {
                    setError("Please enter a valid Phone Number");
                    return true;
                }
            case ConstsAndKw.EMAILADDRESS:
                if (isEmailAlright()) {
                    return false;
                } else {
                    setError("Please enter a valid Email Address");
                    return true;
                }
            case ConstsAndKw.NAME:
                if (isNameAlright()) {
                    return false;
                } else {
                    setError("Please enter a valid Name");
                    return true;
                }
            default:
                return false;
        }
    }

    /*
    validation for phone number
     */
    private boolean isPhoneNumberAlright() {
        if (getText().toString().length() == intCharacterLimit) {
            char[] ary = getText().toString().toCharArray();
            for (char c : ary) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
    validate email address
     */
    private boolean isEmailAlright() {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(getText().toString()).matches()) {
            return true;
        }
        return false;
    }

    /*
    validate name
     */
    private boolean isNameAlright() {
        String regx = "^[\\p{L} .'-]+$";
        if (getText().toString().matches(regx)) {
            return true;
        }
        return false;
    }

    /*
    success broadcast
     */
    private void sendSuccessBroadcst() {
        if (strSuccessBroadcast != "") {
            //send a success broadcast
            Displaylog.Log("success broadcast.");
            context.sendBroadcast(new Intent().setAction(strSuccessBroadcast));
        }
    }

    /*
    send a fail broadcast
     */
    private void sendFailedBroadcast() {
        if (strFailBroadcast != "") {
            Displaylog.Log("failed broadcast.");
            context.sendBroadcast(new Intent().setAction(strFailBroadcast));
        }
    }
}
