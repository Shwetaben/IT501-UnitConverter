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
import java.awt.Font;
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
import com.unitconverter.system.util.UnitConstantUtil;

public class MainFrame {

	JFrame frame;
	JTextField inputTextField;
	JLabel lOutput, lFrom, lTo;
	JRadioButton radioButtonTemp, radioButtonMass, radioButtonLength;
	JComboBox<Unit> fromComboBox, toComboBox;
	JLabel lErrorMsg = new JLabel();
	double maxLimit = 99999999;
	DecimalFormat df = new DecimalFormat("#.######");

	MainFrame() {

		// Creating instance of JFrame
		frame = new JFrame();

		radioButtonTemp = new JRadioButton("Temperature");
		radioButtonTemp.setBounds(30, 50, 170, 25);
		frame.add(radioButtonTemp);
		radioButtonTemp.setFont(new Font(Font.DIALOG, Font.BOLD, 18));

		radioButtonMass = new JRadioButton("Mass");
		radioButtonMass.setBounds(210, 50, 110, 25);
		frame.add(radioButtonMass);
		radioButtonMass.setFont(new Font(Font.DIALOG, Font.BOLD, 18));

		radioButtonLength = new JRadioButton("Length");
		radioButtonLength.setBounds(320, 50, 110, 25);
		frame.add(radioButtonLength);
		radioButtonLength.setFont(new Font(Font.DIALOG, Font.BOLD, 18));

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
		lFrom.setBounds(50, 120, 90, 25);
		frame.add(lFrom);
		lFrom.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));

		inputTextField = new JTextField();
		inputTextField.setBounds(100, 120, 140, 25);
		inputTextField.addCaretListener(e -> performUnitConversion());
		inputTextField.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		frame.add(inputTextField);

		lTo = new JLabel("To:");
		lTo.setBounds(50, 180, 90, 25);
		lTo.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		frame.add(lTo);

		fromComboBox = new JComboBox<Unit>(UnitConstantUtil.fromTempUnits);
		fromComboBox.setBounds(250, 120, 150, 25);
		fromComboBox.addItemListener(this::comboBoxitemStateChanged);
		fromComboBox.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		frame.add(fromComboBox);

		lErrorMsg.setBounds(50, 145, 400, 25);
		lErrorMsg.setForeground(Color.red);
		lErrorMsg.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		frame.add(lErrorMsg);

		lOutput = new JLabel();
		lOutput.setBounds(100, 180, 140, 25);
		lOutput.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		frame.add(lOutput);

		toComboBox = new JComboBox<Unit>(UnitConstantUtil.toTempUnits);
		toComboBox.setBounds(250, 180, 150, 25);
		toComboBox.addItemListener(this::comboBoxitemStateChanged);
		toComboBox.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		frame.add(toComboBox);

		frame.setSize(500, 400);
		frame.setLayout(null);

		// Making the frame visible
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Unit converter");
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Terminate the application when frame is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/*
	 * Update combo box drop down menu items based on radio button selection
	 */
	public void radioButtonSelectionChange(ActionEvent e) {
		fromComboBox.removeAllItems();
		toComboBox.removeAllItems();

		if (radioButtonTemp.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.fromTempUnits));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.toTempUnits));

		} else if (radioButtonMass.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.fromMassUnits));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.toMassUnits));

		} else if (radioButtonLength.isSelected()) {
			fromComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.fromLengthUnits));
			toComboBox.setModel(new DefaultComboBoxModel<Unit>(UnitConstantUtil.toLengthUnits));
		}
		validateAndConvertInput(inputTextField.getText());
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

			outputDbl = Double.parseDouble(inputStr);

			if (outputDbl > maxLimit) {
				throw new ValidationException("Input can not be greater than: " + df.format(maxLimit));
			}
			
			if (outputDbl < -maxLimit) {
				throw new ValidationException("Input can not be less than: -" + df.format(maxLimit));
			}

			if (!radioButtonTemp.isSelected() && outputDbl < 0d) {
				throw new ValidationException("Negative input is not allowed");
			}

			lErrorMsg.setText("");
		} catch (ValidationException ce) {
			lErrorMsg.setText(ce.getErrorMessage());
			lOutput.setText("");
			outputDbl = null;
		} catch (Exception ex) {
			lErrorMsg.setText("Invalid Input: " + inputStr);
			lOutput.setText("");
			outputDbl = null;
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
