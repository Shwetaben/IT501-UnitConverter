package com.unitconverter.system.util;

import static com.unitconverter.system.constant.Unit.Celsius;
import static com.unitconverter.system.constant.Unit.Fahrenheit;
import static com.unitconverter.system.constant.Unit.Foot;
import static com.unitconverter.system.constant.Unit.Gram;
import static com.unitconverter.system.constant.Unit.Inch;
import static com.unitconverter.system.constant.Unit.Kelvin;
import static com.unitconverter.system.constant.Unit.Kilogram;
import static com.unitconverter.system.constant.Unit.Metre;
import static com.unitconverter.system.constant.Unit.Miles;
import static com.unitconverter.system.constant.Unit.Ounce;
import static com.unitconverter.system.constant.Unit.Pound;
import static com.unitconverter.system.constant.Unit.Select;

import com.unitconverter.system.constant.Unit;

public class UnitConstantUtil {

	public static final Unit fromTempUnits[] = { Celsius, Fahrenheit, Kelvin };
	public static final Unit toTempUnits[] = { Select, Celsius, Fahrenheit, Kelvin };

	public static final Unit fromMassUnits[] = { Kilogram, Gram, Pound, Ounce };
	public static final Unit toMassUnits[] = { Select, Kilogram, Gram, Pound, Ounce };

	public static final Unit fromLengthUnits[] = { Metre, Miles, Foot, Inch };
	public static final Unit toLengthUnits[] = { Select, Metre, Miles, Foot, Inch };
}
