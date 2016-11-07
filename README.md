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

## Specific Validation
1. Phone Number Validation
2. Email Validation
3. Name Validation

## How To Use
### Add it in your root build.gradle at the end of repositories:

appprojects {<br />
<tab/> repositories {<br />
    	maven {<br />
        	url "https://jitpack.io"<br />
            }<br />
    }<br />
 }

### Add the dependency

dependencies {
	        compile 'com.github.sagarnayak:ModifiedEditText:1.0'
	}

## 1. Number of Characters limit Validation
Number of Characters limit Validation can be applied when yoou want to limit the number of characters that you want the user to enter. if you want only 10 characters to be entered then just meke a limit of 10.
### How To Use