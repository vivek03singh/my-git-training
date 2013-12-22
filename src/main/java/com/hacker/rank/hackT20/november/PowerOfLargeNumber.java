package com.hacker.rank.hackT20.november;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class PowerOfLargeNumber {

	public static void main (String...strings) {
		BigInteger num = new BigInteger("34534985349875439875439875349875");
		BigInteger num2 = new BigInteger("93475349759384754395743975349573495");
		
		double number = Math.pow(num.floatValue(), num2.floatValue());
		
		double divider = (Math.pow(10, 9)) + 7;
		
		double value = num.floatValue() % divider;
		
		System.out.println(Math.pow(value, num2.floatValue()));
	}
}