package com.dar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String COMMON_PATTERN =
			"^[a-zA-Z0-9_-]{3,15}$";
	
	private static final String NAME_PATTERN =
			"^[a-zA-Z]{1,30}$";

	public Validator() {}
	
	private boolean matches(final String s) {
		matcher = pattern.matcher(s);
		return matcher.matches();
	}
	
	public boolean email(final String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		return matches(email);
	}
	
	public boolean username(final String username) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		return matches(username);
	}
	
	public boolean firstname(final String firstname) {
		pattern = Pattern.compile(NAME_PATTERN);
		return matches(firstname);
	}
	
	public boolean lastname(final String lastname) {
		pattern = Pattern.compile(NAME_PATTERN);
		return matches(lastname);
	}
}