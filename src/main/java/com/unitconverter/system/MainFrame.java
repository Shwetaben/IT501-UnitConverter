package com.unitconverter.system;

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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.unitconverter.system.constant.Unit;
import com.unitconverter.system.exception.ValidationException;

public class MainFrame {

	JFrame f;
	JTextField inputTextField;
	JLabel lOutput, lFrom, lTo;
	JRadioButton radioButtonTemp, radioButtonMass, radioButtonLength;
	JComboBox<Unit> fromComboBox, toComboBox;
	JLabel lErrorMsg = new JLabel();
	int maximumLength = 8;

	Unit tempUnit1[] = { Celsius, Fahrenheit, Kelvin };
	Unit tempUnit2[] = { Select, Celsius, Fahrenheit, Kelvin };

	Unit massUnit1[] = { Kilogram, Gram, Pound, Ounce };
	Unit massUnit2[] = { Select, Kilogram, Gram, Pound, Ounce };

	Unit lengthUnit1[] = { Metre, Miles, Foot, Inch };
	Unit lengthUnit2[] = { Select, Metre, Miles, Foot, Inch };

	MainFrame() {

		// Creating instance of JFrame
		f = new JFrame();

		radioButtonTemp = new JRadioButton("Temperature");
		radioButtonTemp.setBounds(30, 50, 105, 25);
		f.add(radioButtonTemp);

		radioButtonMass = new JRadioButton("Mass");
		radioButtonMass.setBounds(163, 50, 90, 25);
		f.add(radioButtonMass);

		radioButtonLength = new JRadioButton("Length");
		radioButtonLength.setBounds(270, 50, 90, 25);
		f.add(radioButtonLength);

		// Grouping radio buttons
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioButtonTemp);
		bg.add(radioButtonMass);
		bg.add(radioButtonLength);

		radioButtonTemp.addActionListener(this::radioButtonSelectionChange);
		radioButtonMass.addActionListener(this::radioButtonSelectionChange);
		radioButtonLength.addActionListener(this::radioButtonSelectionChange);

		// By default set temperature radio button selected
		radioButtonTemp.setSelected(true);

		lFrom = new JLabel("From:");
		lFrom.setBounds(90, 100, 90, 25);
		f.add(lFrom);

		inputTextField = new JTextField();
		inputTextField.setBounds(130, 100, 110, 25);
		inputTextField.addCaretListener(e -> performUnitConversion());
		f.add(inputTextField);

		lTo = new JLabel("To:");
		lTo.setBounds(90, 150, 90, 25);
		f.add(lTo);

		fromComboBox = new JComboBox<Unit>(tempUnit1);
		fromComboBox.setBounds(250, 100, 90, 25);
		fromComboBox.addItemListener(this::comboBoxitemStateChanged);
		f.add(fromComboBox);

		lErrorMsg.setBounds(50, 130, 300, 25);
		lErrorMsg.setForeground(Color.red);
		f.add(lErrorMsg);

		lOutput = new JLabel();
		lOutput.setBounds(130, 150, 110, 25);
		f.add(lOutput);

		toComboBox = new JComboBox<Unit>(tempUnit2);
		toComboBox.setBounds(250, 150, 90, 25);
		toComboBox.addItemListener(this::comboBoxitemStateChanged);
		f.add(toComboBox);

		f.setSize(400, 300);
		f.setLayout(null);

		// Making the frame visible
		f.setVisible(true);
		f.setResizable(false);
		f.setTitle("Unit converter");
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Terminate the application when frame is closed
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/*
	 * Update combo box drop down menu items based on radio button selection
	 */
	public void radioButtonSelectionChange(ActionEvent e) {
		fromComboBox.removeAllItems();
		toComboBox.removeAllItems();

		if (radioButtonTemp.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(tempUnit1));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(tempUnit2));
		} else if (radioButtonMass.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(massUnit1));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(massUnit2));
		} else if (radioButtonLength.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(lengthUnit1));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(lengthUnit2));
		}
		lOutput.setText("");
	}

	/*
	 * Calculate output whenever there is change in dropdown menu iteam selection
	 */
	public void comboBoxitemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			performUnitConversion();
		}
	}

	/*
	 * Perform unit conversation based on conditions
	 */
	public void performUnitConversion() {

		// Fetch current selected values of dropdown menus
		Unit fromUnit = (Unit) fromComboBox.getSelectedItem();
		Unit toUnit = (Unit) toComboBox.getSelectedItem();

		String inputStr = inputTextField.getText();

		Double inputDbl = null, outputDbl = null;
		inputDbl = validateAndConvertInput(inputStr);

		/*
		 * No calculation required if input is null or invalid
		 */
		if (inputDbl == null) {
			return;
		}

		/*
		 * No calculation required if unit to be converted is not selected
		 */

		if (toUnit == Select) {
			lOutput.setText("");
			return;
		}

		if (radioButtonTemp.isSelected()) {
			outputDbl = performTempUnitConversation(fromUnit, toUnit, inputDbl);
		} else if (radioButtonMass.isSelected()) {
			outputDbl = performMassUnitConversation(fromUnit, toUnit, inputDbl);
		} else if (radioButtonLength.isSelected()) {
			outputDbl = performLengthUnitConversation(fromUnit, toUnit, inputDbl);
		}

		if (outputDbl == null) {
			lOutput.setText("");
		} else {
			DecimalFormat df = new DecimalFormat("#.########");
			lOutput.setText(df.format(outputDbl == 0d ? 0d : outputDbl));
		}

	}

	public Double validateAndConvertInput(String inputStr) {
		// Clear the output and error message if input is empty string
		if (inputStr.isEmpty()) {
			lErrorMsg.setText("");
			lOutput.setText("");
			return null;
		}

		Double outputDbl = null;
		try {
			if (inputStr.contains("d") || inputStr.contains("f")) {
				throw new Exception();
			}
			if (inputStr.length() > maximumLength) {
				throw new ValidationException("Maximum allowed length for input is: " + maximumLength);
			}
			outputDbl = Double.parseDouble(inputStr);
			lErrorMsg.setText("");
		} catch (ValidationException ce) {
			lErrorMsg.setText(ce.getErrorMessage());
			lOutput.setText("");
		} catch (Exception ex) {
			lErrorMsg.setText("Invalid Input: " + inputStr);
			lOutput.setText("");
		}
		return outputDbl;
	}

	public Double performTempUnitConversation(Unit fromUnit, Unit toUnit, Double inputDbl) {
		Double outputDbl = null;
		if (fromUnit == Celsius) {
			switch (toUnit) {
			case Celsius:
				outputDbl = inputDbl;
				break;
			case Kelvin:
				outputDbl = inputDbl + 273.15;
				break;
			case Fahrenheit:
				outputDbl = (inputDbl * 9 / 5) + 32;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Kelvin) {
			switch (toUnit) {
			case Celsius:
				outputDbl = inputDbl - 273.15;
				break;
			case Kelvin:
				outputDbl = inputDbl;
				break;
			case Fahrenheit:
				outputDbl = ((inputDbl - 273.15) * 9 / 5) + 32;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Fahrenheit) {
			switch (toUnit) {
			case Celsius:
				outputDbl = (inputDbl - 32) * 5 / 9;
				break;
			case Kelvin:
				outputDbl = (inputDbl + 459.67) * 5 / 9;
				break;
			case Fahrenheit:
				outputDbl = inputDbl;
				break;
			default:
				outputDbl = null;
				break;
			}

		}

		return outputDbl;

	}

	public Double performMassUnitConversation(Unit fromUnit, Unit toUnit, Double inputDbl) {
		Double outputDbl = null;
		if (fromUnit == Kilogram) {
			switch (toUnit) {
			case Gram:
				outputDbl = inputDbl * 1000;
				break;
			case Pound:
				outputDbl = inputDbl * 2.205;
				break;
			case Kilogram:
				outputDbl = inputDbl;
				break;
			case Ounce:
				outputDbl = inputDbl * 35.274;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Gram) {
			switch (toUnit) {
			case Gram:
				outputDbl = inputDbl;
				break;
			case Pound:
				outputDbl = (inputDbl / 1000) * 2.205;
				break;
			case Kilogram:
				outputDbl = inputDbl / 1000;
				break;
			case Ounce:
				outputDbl = (inputDbl / 1000) * 35.274;
				break;
			default:
				outputDbl = null;
				break;
			}
		} else if (fromUnit == Pound) {
			switch (toUnit) {
			case Gram:
				outputDbl = (inputDbl * 1000) / 2.205;
				break;
			case Pound:
				outputDbl = inputDbl;
				break;
			case Kilogram:
				outputDbl = inputDbl / 2.205;
				break;
			case Ounce:
				outputDbl = inputDbl * 16;
				break;
			default:
				outputDbl = null;
				break;
			}
		} else if (fromUnit == Ounce) {
			switch (toUnit) {
			case Gram:
				outputDbl = inputDbl * 28.3495;
				break;
			case Pound:
				outputDbl = inputDbl / 16;
				break;
			case Kilogram:
				outputDbl = inputDbl / 35.274;
				break;
			case Ounce:
				outputDbl = inputDbl;
				break;
			default:
				outputDbl = null;
				break;
			}
		}

		return outputDbl;

	}

	public Double performLengthUnitConversation(Unit fromUnit, Unit toUnit, Double inputDbl) {
		Double outputDbl = null;
		if (fromUnit == Metre) {
			switch (toUnit) {
			case Metre:
				outputDbl = inputDbl;
				break;
			case Miles:
				outputDbl = inputDbl * 0.00062137;
				break;
			case Foot:
				outputDbl = inputDbl * 3.28084;
				break;
			case Inch:
				outputDbl = inputDbl * 3.28084 * 12;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Miles) {
			switch (toUnit) {
			case Metre:
				outputDbl = inputDbl * 1609.34;
				break;
			case Miles:
				outputDbl = inputDbl;
				break;
			case Foot:
				outputDbl = inputDbl * 5280;
				break;
			case Inch:
				outputDbl = inputDbl * 5280 * 12;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Foot) {
			switch (toUnit) {
			case Metre:
				outputDbl = inputDbl * 0.3048;
				break;
			case Miles:
				outputDbl = inputDbl / 5280;
				break;
			case Foot:
				outputDbl = inputDbl;
				break;
			case Inch:
				outputDbl = inputDbl * 12;
				break;
			default:
				outputDbl = null;
				break;
			}

		} else if (fromUnit == Inch) {
			switch (toUnit) {
			case Metre:
				outputDbl = (inputDbl * 0.3048) / 12;
				break;
			case Miles:
				outputDbl = (inputDbl / 5280) / 12;
				break;
			case Foot:
				outputDbl = inputDbl / 12;
				break;
			case Inch:
				outputDbl = inputDbl;
				break;
			default:
				outputDbl = null;
				break;
			}

		}

		return outputDbl;
	}

}
