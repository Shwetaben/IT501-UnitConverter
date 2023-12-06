package com.unitconverter.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.Test;

import com.unitconverter.system.constant.Unit;

public class MainFrameTest {

	private MainFrame mainFrame;
	private Robot robot;

	@Before
	public void setUp() throws AWTException {
		mainFrame = new MainFrame();
		robot = new Robot();
	}

	@Test
	public void testGUIInitialization() {
		// Verify that the frame is visible
		assertTrue(mainFrame.frame.isVisible());

		// Verify the default selection of the temperature radio button
		assertTrue(mainFrame.radioButtonTemp.isSelected());
		assertFalse(mainFrame.radioButtonMass.isSelected());
		assertFalse(mainFrame.radioButtonLength.isSelected());

		// Verify the initial values of components
		assertEquals("From:", mainFrame.lFrom.getText());
		assertEquals("To:", mainFrame.lTo.getText());
		assertEquals("", mainFrame.lErrorMsg.getText());
		assertEquals("", mainFrame.lOutput.getText());

		// Verify the default values of combo boxes
		assertEquals(Unit.Celsius, mainFrame.fromComboBox.getSelectedItem());
		assertEquals(Unit.Select, mainFrame.toComboBox.getSelectedItem());

	}

	@Test
	public void testInputValidation_MaximumAllowedNumber() {
		mainFrame.inputTextField.setText("");
		mainFrame.inputTextField.requestFocus();

		LongStream.rangeClosed(0, 9).forEach(l -> {
			robot.keyPress('1');
			robot.keyRelease('1');
		});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("Input can not be greater than: " + mainFrame.df.format(mainFrame.maxLimit),
				mainFrame.lErrorMsg.getText());
	}
	
	@Test
	public void testInputValidation_MinimumAllowedNegativeNumber() {
		mainFrame.inputTextField.setText("");
		mainFrame.inputTextField.requestFocus();

		LongStream.rangeClosed(0, 10).forEach(l -> {
			if(l == 0l) {
				robot.keyPress('-');
				robot.keyRelease('-');
			} else {
				robot.keyPress('1');
				robot.keyRelease('1');
			}
			
			
		});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("Input can not be less than: -" + mainFrame.df.format(mainFrame.maxLimit),
				mainFrame.lErrorMsg.getText());
	}


	@Test
	public void testInputValidation_InvalidString() {
		mainFrame.inputTextField.setText("");
		mainFrame.inputTextField.requestFocus();

		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("Invalid Input: a", mainFrame.lErrorMsg.getText());
	}

	@Test
	public void testInputValidation_NegativeInput() {
		mainFrame.radioButtonMass.setSelected(true);
		mainFrame.validateAndConvertInput("-0.1");
		assertEquals("Negative input is not allowed", mainFrame.lErrorMsg.getText());
	}

	@Test
	public void testTemperatureUnitConversation() {
		mainFrame.inputTextField.setText("1");
		mainFrame.fromComboBox.setSelectedItem(Unit.Celsius);
		mainFrame.toComboBox.setSelectedItem(Unit.Kelvin);
		mainFrame.performUnitConversion();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("", mainFrame.lErrorMsg.getText());
		assertEquals("274.15", mainFrame.lOutput.getText());

		assertEquals(new Double(34.7), mainFrame.performTempUnitConversation(Unit.Celsius, Unit.Fahrenheit, 1.5));
		assertEquals(new Double(1.5), mainFrame.performTempUnitConversation(Unit.Celsius, Unit.Celsius, 1.5));

		assertEquals(new Double(1), mainFrame.performTempUnitConversation(Unit.Fahrenheit, Unit.Fahrenheit, 1.0));
		assertEquals(new Double(-17.2222), mainFrame.performTempUnitConversation(Unit.Fahrenheit, Unit.Celsius, 1.0),
				0.001);
		assertEquals(new Double(256.09444), mainFrame.performTempUnitConversation(Unit.Fahrenheit, Unit.Kelvin, 1.3),
				0.001);

		assertEquals(new Double(-457.87), mainFrame.performTempUnitConversation(Unit.Kelvin, Unit.Fahrenheit, 1.0));
		assertEquals(new Double(-270.95), mainFrame.performTempUnitConversation(Unit.Kelvin, Unit.Celsius, 2.2));
		assertEquals(new Double(1.3), mainFrame.performTempUnitConversation(Unit.Kelvin, Unit.Kelvin, 1.3));

	}

	@Test
	public void testTemperatureUnitConversation_ToUnitSelect() {
		mainFrame.inputTextField.setText("1");
		mainFrame.fromComboBox.setSelectedItem(Unit.Celsius);
		mainFrame.toComboBox.setSelectedItem(Unit.Select);
		mainFrame.performUnitConversion();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("", mainFrame.lErrorMsg.getText());
		assertEquals("", mainFrame.lOutput.getText());

	}

	@Test
	public void testMassUnitConversation() {

		assertEquals(new Double(1400), mainFrame.performMassUnitConversation(Unit.Kilogram, Unit.Gram, 1.4));
		assertEquals(new Double(3.08647), mainFrame.performMassUnitConversation(Unit.Kilogram, Unit.Pound, 1.4), 0.001);
		assertEquals(new Double(49.3835), mainFrame.performMassUnitConversation(Unit.Kilogram, Unit.Ounce, 1.4), 0.001);
		assertEquals(new Double(1.4), mainFrame.performMassUnitConversation(Unit.Kilogram, Unit.Kilogram, 1.4));

		assertEquals(new Double(1.5), mainFrame.performMassUnitConversation(Unit.Gram, Unit.Gram, 1.5));
		assertEquals(new Double(0.00330693), mainFrame.performMassUnitConversation(Unit.Gram, Unit.Pound, 1.5), 0.001);
		assertEquals(new Double(0.0529109), mainFrame.performMassUnitConversation(Unit.Gram, Unit.Ounce, 1.5), 0.001);
		assertEquals(new Double(.0015), mainFrame.performMassUnitConversation(Unit.Gram, Unit.Kilogram, 1.5));

		assertEquals(new Double(1088.4356), mainFrame.performMassUnitConversation(Unit.Pound, Unit.Gram, 2.4), 0.001);
		assertEquals(new Double(2.4), mainFrame.performMassUnitConversation(Unit.Pound, Unit.Pound, 2.4));
		assertEquals(new Double(38.4), mainFrame.performMassUnitConversation(Unit.Pound, Unit.Ounce, 2.4), 0.001);
		assertEquals(new Double(1.08862), mainFrame.performMassUnitConversation(Unit.Pound, Unit.Kilogram, 2.4), 0.001);

		assertEquals(new Double(116.233), mainFrame.performMassUnitConversation(Unit.Ounce, Unit.Gram, 4.1), 0.001);
		assertEquals(new Double(.25625), mainFrame.performMassUnitConversation(Unit.Ounce, Unit.Pound, 4.1));
		assertEquals(new Double(4.1), mainFrame.performMassUnitConversation(Unit.Ounce, Unit.Ounce, 4.1), 0.001);
		assertEquals(new Double(0.116233), mainFrame.performMassUnitConversation(Unit.Ounce, Unit.Kilogram, 4.1),
				0.001);

	}

	@Test
	public void testLengthUnitConversation() {

		assertEquals(new Double(5.1), mainFrame.performLengthUnitConversation(Unit.Metre, Unit.Metre, 5.1));
		assertEquals(new Double(0.00316899), mainFrame.performLengthUnitConversation(Unit.Metre, Unit.Miles, 5.1),
				0.001);
		assertEquals(new Double(16.7323), mainFrame.performLengthUnitConversation(Unit.Metre, Unit.Foot, 5.1), 0.001);
		assertEquals(new Double(200.787), mainFrame.performLengthUnitConversation(Unit.Metre, Unit.Inch, 5.1), 0.001);

		assertEquals(new Double(8368.568), mainFrame.performLengthUnitConversation(Unit.Miles, Unit.Metre, 5.2));
		assertEquals(new Double(5.2), mainFrame.performLengthUnitConversation(Unit.Miles, Unit.Miles, 5.2));
		assertEquals(new Double(27456), mainFrame.performLengthUnitConversation(Unit.Miles, Unit.Foot, 5.2));
		assertEquals(new Double(329472), mainFrame.performLengthUnitConversation(Unit.Miles, Unit.Inch, 5.2));

		assertEquals(new Double(1.61544), mainFrame.performLengthUnitConversation(Unit.Foot, Unit.Metre, 5.3));
		assertEquals(new Double(0.00100379), mainFrame.performLengthUnitConversation(Unit.Foot, Unit.Miles, 5.3),
				0.001);
		assertEquals(new Double(5.3), mainFrame.performLengthUnitConversation(Unit.Foot, Unit.Foot, 5.3));
		assertEquals(new Double(63.59999999), mainFrame.performLengthUnitConversation(Unit.Foot, Unit.Inch, 5.3),
				0.001);

		assertEquals(new Double(0.13716), mainFrame.performLengthUnitConversation(Unit.Inch, Unit.Metre, 5.4), 0.001);
		assertEquals(new Double(8.5227e-5), mainFrame.performLengthUnitConversation(Unit.Inch, Unit.Miles, 5.4), 0.001);
		assertEquals(new Double(0.45), mainFrame.performLengthUnitConversation(Unit.Inch, Unit.Foot, 5.4));
		assertEquals(new Double(5.4), mainFrame.performLengthUnitConversation(Unit.Inch, Unit.Inch, 5.4));

	}

}
