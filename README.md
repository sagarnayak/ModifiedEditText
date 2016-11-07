# ModifiedEditText
This is a more modified version of edittext. in conventional EditText in android we get the default functionalities. but one of the most important functionality is missing in the default EditText. as we know we must have to validate the data before taking it as an input in edittext. so this functionality is provided in this mod.

# Functionality Avilable

## Normal Validations
1. Number of Characters limit Validation
2. First BlankSpace Validation
3. Intermediate BlankSpace Validation
4. SQL Injaction Character Validation
5. Take Letters Only
6. Take Numbers Only
7. Receive Validation Failed and Success Broadcast

## Specific Validation
1. Phone Number Validation
2. Email Validation
3. Name Validation

## How To Use
### Add it in your root build.gradle at the end of repositories:

    appprojects {
        repositories {
    	    maven {
        	    url "https://jitpack.io"
            }
        }
    }

### Add the dependency

    dependencies {
	        compile 'com.github.sagarnayak:ModifiedEditText:1.0'
	}

### To Use In Layout
    <com.csmpl.androidlib.edittextmod.EditTextMod xmlns:edittextmod="http://schemas.android.com/apk/res-auto"
        android:id="@+id/edittextmod_demo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        edittextmod:FailBroadcast="FAILED"
        edittextmod:ValidationType="PHONENUMBER" />
        
## 1. Number of Characters limit Validation
Number of Characters limit Validation can be applied when yoou want to limit the number of characters that you want the user to enter. if you want only 10 characters to be entered then just meke a limit of 10.
### How To Use Number of Characters limit Validation
    edittextmod:LimitInput="4"
    
## 2. First BlankSpace Validation
Setting this value as true will not allow a blank space to be entered at first place.
### How To Use First BlankSpace Validation
    edittextmod:BlockFirstSpace="true"
    
## 3. Intermediate BlankSpace Validation
Setting this value as true will not allow a blank space to be entered at any position.
### How To Use Intermediate BlankSpace Validation
    edittextmod:BlockIntermediateSpace="true"
    
## 4. SQL Injaction Character Validation
Setting this value as true will not allow a sql injection characters ( < , > , = , ~ , # , ^ , | , $ , % , " , & , * , ! , ' ) to be entered at any position.
### How To Use SQL Injaction Character Validation
    edittextmod:BlockSqlInjection="true"
    
## 5. Take Letters Only
Setting this value as true will not allow characters other then letters at any position.
### How To Use Take Letters Only Validation
    edittextmod:TakeOnlyLetters="true"
    
## 6. Take Numbers Only
Setting this value as true will not allow characters other then numbers at any position.
### How To Use Take Numbers Only Validation
    edittextmod:TakeOnlyNumbers="true"
    
## 7. Receive Validation Failed and Success Broadcast
This parameter will require you to provide a string. this string will be broadcasted whenever there is a success or a fail in validtion as per what you have used this for. Register for broadcast with this name you have provided in the activity to receive a failed or success broadcast.
### How To Use Receive Validation Failed and Success Broadcast
    edittextmod:FailBroadcast="FAILED_BORADCAST"
    edittextmod:SuccessBroadcast="SUCCESS_BROADCAST"