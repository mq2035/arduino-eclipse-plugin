package jUnit;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.model.ICModelMarker;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import io.sloeber.core.api.BoardDescriptor;
import io.sloeber.core.api.BoardsManager;
import io.sloeber.core.api.CodeDescriptor;
import io.sloeber.core.api.ConfigurationDescriptor;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class CreateAndCompile {
    private String mBoardID;
    private Map<String, String> myOptions = new HashMap<>();
    private String mPackageName;
    private String mPlatform;
    private String mJsonFileName;
    private static int mCounter = 0;
    private static String teensyInstall = "D:/arduino/arduino-1.6.9 - Teensy 1.29/hardware";
    private static String teensyBoards_txt = teensyInstall + "/teensy/avr/boards.txt";

    public CreateAndCompile(String jsonFileName, String packageName, String platform, String boardID, String options) {
	this.mBoardID = boardID;
	this.mPackageName = packageName;
	this.mPlatform = platform;
	this.mJsonFileName = jsonFileName;
	String[] lines = options.split("\n"); //$NON-NLS-1$
	for (String curLine : lines) {
	    String[] values = curLine.split("=", 2); //$NON-NLS-1$
	    if (values.length == 2) {
		this.myOptions.put(values[0], values[1]);
	    }
	}

    }

    @SuppressWarnings("rawtypes")
    @Parameterized.Parameters
    public static Collection boards() {
	return Arrays.asList(new Object[][] {
		// chipKIT
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot32mx4", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot32mx7", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot_mx3ck", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_mx3", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot_mx3ck_512", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot_mx4ck", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_pro_mx4", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cerebot_mx7ck", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_pro_mx7", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_Pi", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_Pi_USB_Serial", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cmod", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "CUI32stem", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "ubw32_mx460", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "ubw32_mx795", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "cui32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "usbono_pic32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_DP32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "fubarino_mini_dev", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "fubarino_mini", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "fubarino_sd_seeed", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "fubarino_sd", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "Fubarino_SDZ", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "mega_pic32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "mega_usb_pic32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "Olimex_Pinguino32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "picadillo_35t", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "quick240_usb_pic32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_uc32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "uc32_pmod", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "uno_pic32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "uno_pmod", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_WF32", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_WiFire", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_WiFire_AB", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "chipkit_WiFire_80MHz", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "OpenScope", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "openbci", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "lenny", "" }, //
		{ "package_chipkit_index.json", "chipKIT", "chipKIT", "clicker2", "" }, //

		// Teensy
		{ "local", teensyBoards_txt, "", "teensy31",
			"USB Type=Serial\nCPU Speed=96 MHz optimized (overclock)\nKeyboard Layout=US English" }, //
		{ "local", teensyBoards_txt, "", "teensy30",
			"USB Type=Serial\nCPU Speed=96 MHz (overclock)\nKeyboard Layout=US English" }, //
		{ "local", teensyBoards_txt, "", "teensyLC",
			"USB Type=Serial\nl\nCPU Speed=48 MHz\nKeyboard Layout=US English" }, //
		{ "local", teensyBoards_txt, "", "teensypp2",
			"USB Type=Serial\nCPU Speed=16 MHz\nKeyboard Layout=US English" }, //
		{ "local", teensyBoards_txt, "", "teensy2",
			"USB Type=Serial\nCPU Speed=16 MHz\nKeyboard Layout=US English" }, //

		// Adafruit AVR
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "flora8", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "bluefruitmicro", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "gemma", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "feather32u4", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "trinket3", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "trinket5", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "protrinket5", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "protrinket3", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "protrinket5ftdi", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "protrinket3ftdi", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "adafruit32u4", "" }, //
		{ "package_adafruit_index.json", "adafruit", "Adafruit AVR Boards", "circuitplay32u4cat", "" }, //
		// Adafruit SAMD
		{ "package_adafruit_index.json", "adafruit", "Adafruit SAMD Boards", "adafruit_feather_m0", "" }, //

		// amel samd
		{ "package_index.json", "AMEL", "AMEL-Tech Boards", "AMEL_SmartEverything_atmel_ice", "" }, //
		{ "package_index.json", "AMEL", "AMEL-Tech Boards", "AMEL_SmartEverything_sam_ice", "" }, //
		{ "package_index.json", "AMEL", "AMEL-Tech Boards", "AMEL_SmartEverything_native", "" }, //
		{ "package_index.json", "AMEL", "AMEL-Tech Boards", "AMEL_SmartEverything_native", "" }, //

		// acore avr
		{ "package_adafruit_index.json", "arcore", "Leonardo & Micro MIDI-USB (arcore)", "leonardo", "" }, //
		{ "package_adafruit_index.json", "arcore", "Leonardo & Micro MIDI-USB (arcore)", "leonardo2", "" }, //
		{ "package_adafruit_index.json", "arcore", "Leonardo & Micro MIDI-USB (arcore)", "micro", "" }, //

		// Arduino.cc AVR boards
		{ "package_index.json", "arduino", "Arduino AVR Boards", "yun", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "uno", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "diecimila", "Processor=ATMega328" },
		{ "package_index.json", "arduino", "Arduino AVR Boards", "nano", "Processor=ATMega328" },
		{ "package_index.json", "arduino", "Arduino AVR Boards", "mega", "Processor=ATMega2560 (Mega 2560)" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "megaADK", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "leonardo", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "micro", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "esplora", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "mini", "Processor=ATMega328" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "ethernet", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "fio", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "bt", "Processor=ATMega328" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "LilyPadUSB", "" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "lilypad", "Processor=ATMega328" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "pro", "Processor=ATMega328 (3.3V, 8 MHz)" }, // comment
		{ "package_index.json", "arduino", "Arduino AVR Boards", "atmegang", "Processor=ATMega8" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "robotControl", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "robotMotor", "" }, //
		{ "package_index.json", "arduino", "Arduino AVR Boards", "gemma", "" },

		// Arduino SAM
		{ "package_index.json", "arduino", "Arduino SAM Boards (32-bits ARM Cortex-M3)", "arduino_due_x_dbg",
			"" }, //
		{ "package_index.json", "arduino", "Arduino SAM Boards (32-bits ARM Cortex-M3)", "arduino_due_x", "" }, //

		// Arduino SAMD
		{ "package_index.json", "arduino", "Arduino SAMD Boards (32-bits ARM Cortex-M0+)", "arduino_zero_edbg",
			"" }, //
		{ "package_index.json", "arduino", "Arduino SAMD Boards (32-bits ARM Cortex-M0+)",
			"arduino_zero_native", "" }, //
		{ "package_index.json", "arduino", "Arduino SAMD Boards (32-bits ARM Cortex-M0+)", "mkr1000", "" }, //

		// arrow SAMD
		{ "package_index.json", "Arrow", "Arrow Boards", "SmartEverything_Fox_atmel_ice", "" }, //
		{ "package_index.json", "Arrow", "Arrow Boards", "SmartEverything_Fox_sam_ice", "" }, //
		{ "package_index.json", "Arrow", "Arrow Boards", "SmartEverything_Fox_native", "" }, //
		{ "package_index.json", "Arrow", "Arrow Boards", "NetTrotter_atmel_ice", "" }, //
		{ "package_index.json", "Arrow", "Arrow Boards", "NetTrotter_sam_ice", "" }, //
		{ "package_index.json", "Arrow", "Arrow Boards", "NetTrotter_native", "" }, //

		// atmel-avr-xminis
		{ "package_index.json", "atmel-avr-xminis", "Atmel AVR Xplained-minis", "atmega328p_xplained_mini",
			"" }, //
		{ "package_index.json", "atmel-avr-xminis", "Atmel AVR Xplained-minis", "atmega168pb_xplained_mini",
			"" }, //
		{ "package_index.json", "emoro", "EMORO 2560", "emoro2560", "" }, //

		// Cosa
		{ "package_cosa_index.json", "Cosa", "Cosa", "diecimila", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "duemilanove", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "leonardo", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "mega1280", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "mega2560", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "micro", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "nano", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "pro-micro", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "pro-micro-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "pro-mini", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "pro-mini-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "uno", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "attiny84-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "attiny85-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "attiny861-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "atmega328-8", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "mighty", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "mighty-opt", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "lilypad", "" }, //
		{ "package_cosa_index.json", "Cosa", "Cosa", "lilypad-usb", "" }, //

		// emoro AVR
		{ "package_index.json", "emoro", "EMORO 2560", "emoro2560", "" }, //

		// ESP8266 boards
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "generic",
			".CPU Frequency==80 MHz\nFlash Frequency=40MHz\nFlash Mode=DIO\nUpload Speed=115200\nFlash Size=512K (64K SPIFFS)\nReset Method=ck\nDebug port=Disabled\nDebug Level=None" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "esp8285",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=1M (512K SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "espduino",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "huzzah",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "espresso_lite_v1",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "espresso_lite_v2",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "phoenix_v1",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "phoenix_v2",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "nodemcu",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "nodemcuv2",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "modwifi",
			"CPU Frequency=80 MHz\nUpload Speed=115200" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "thing",
			"CPU Frequency=80 MHz\nUpload Speed=115200" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "thingdev",
			"CPU Frequency=80 MHz\nUpload Speed=115200" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "esp210",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "d1_mini",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "d1",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "espino",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nFlash Mode=DIO\nReset Method=ck" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "espinotee",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "wifinfo",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nModule=ESP07 (1M/192K SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None\nFlash Frequency=40MHz\nFlash Mode=QIO" },
		{ "package_esp8266com_index.json", "esp8266", "esp8266", "coredev",
			"CPU Frequency=80 MHz\nUpload Speed=115200\nFlash Size=4M (3M SPIFFS)\nReset Method=nodemcu\nDebug port=Disabled\nDebug Level=None\nFlash Frequency=40MHz\nFlash Mode=QIO\nlwIP Variant=Espressif (xcc)" },

		// intel
		{ "package_index.json", "Intel", "Intel Curie Boards", "arduino_101", "" }, //
		{ "package_index.json", "Intel", "Intel i586 Boards", "izmir_fd", "" }, //
		{ "package_index.json", "Intel", "Intel i586 Boards", "izmir_fg", "" }, //
		{ "package_index.json", "Intel", "Intel i686 Boards", "izmir_ec", "" }, //

		// littleBits AVR
		{ "package_index.json", "littleBits", "littleBits Arduino AVR Modules", "w6_arduino", "" }, //
		// Microsoft win10
		// { "package_index.json", "Microsoft", "Windows 10 Iot Core",
		// "w10iotcore", "Processor=arm" }, //

		// mighty core
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "1284",
			"Clock=16MHz external\nPinout=Bobuino\nB.O.D=2.7v\nVariant=1284P" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "1284",
			"Clock=16MHz external\nPinout=Standard\nB.O.D=2.7v\nVariant=1284P" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "644",
			"Clock=20MHz external\nPinout=Standard\nB.O.D=2.7v\nVariant=644P / 644PA" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "324",
			"Clock=12MHz external\nPinout=Bobuino\nB.O.D=1.8v\nVariant=324P" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "164",
			"Clock=8MHz external\nPinout=Bobuino\nB.O.D=Disabled\nVariant=164A" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "32",
			"Clock=8MHz external  (BOD 2.7v)\nPinout=Standard" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "16",
			"Clock=16MHz external (BOD 2.7v)\nPinout=Bobuino" }, //
		{ "package_MCUdude_MightyCore_index.json", "MightyCore", "MightyCore", "8535",
			"Clock=20MHz external (BOD 4.0v)\nPinout=Bobuino" }, //

		// RedBearLab
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab AVR Boards", "blend", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab AVR Boards", "blendmicro8", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab AVR Boards", "blendmicro16", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab nRF51822 Boards (32-bits ARM Cortex-M0)",
			"nRF51822", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab nRF51822 Boards (32-bits ARM Cortex-M0)",
			"nRF51822_NANO", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab nRF51822 Boards (32-bits ARM Cortex-M0)",
			"nRF51822_32KB", "" }, //
		{ "package_redbearlab_index.json", "RedBearLab", "RedBearLab nRF51822 Boards (32-bits ARM Cortex-M0)",
			"nRF51822_NANO_32KB", "" }, //

		// Sparkfun AVR
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "RedBoard", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "makeymakey", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "promicro",
			"Processor=ATmega32U4 (3.3V, 8 MHz)" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "fiov3", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "qduinomini", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "digitalsandbox", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "megapro",
			"Processor=ATmega2560 (3.3V / 8 MHz)" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "RedBot", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "Serial7Seg", "" }, //
		{ "package_sparkfun_index.json", "SparkFun", "SparkFun AVR Boards", "atmega128rfa1", "" }, //

		// TeeOnArdu avr

		{ "package_adafruit_index.json", "TeeOnArdu", "Adafruit TeeOnArdu", "TeeOnArdu",
			"USB Type=Serial\nKeyboard Layout=US English" }, //
		{ "package_adafruit_index.json", "TeeOnArdu", "Adafruit TeeOnArdu", "FloraTeensyCore",
			"USB Type=Serial\nKeyboard Layout=US English" }, //
		{ "package_adafruit_index.json", "TeeOnArdu", "Adafruit TeeOnArdu", "CirPlayTeensyCore",
			"USB Type=Serial\nKeyboard Layout=US English" }, //

	});
    }

    /*
     * In new new installations (of the Sloeber development environment) the
     * installer job will trigger downloads These mmust have finished before we
     * can start testing
     */
    @BeforeClass
    public static void WaitForInstallerToFinish() {
	installAdditionalBoards();
	waitForAllJobsToFinish();
    }

    public static void waitForAllJobsToFinish() {
	try {
	    Thread.sleep(10000);

	    IJobManager jobMan = Job.getJobManager();

	    while (!jobMan.isIdle()) {
		Thread.sleep(5000);
	    }
	    // As nothing is running now we can start installing

	} catch (InterruptedException e) {
	    e.printStackTrace();
	    fail("can not find installerjob");
	}
    }

    public static void installAdditionalBoards() {
	String packageUrlsToAdd[] = { "https://adafruit.github.io/arduino-board-index/package_adafruit_index.json",
		"https://mcudude.github.io/MightyCore/package_MCUdude_MightyCore_index.json",
		"https://raw.githubusercontent.com/mikaelpatel/Cosa/master/package_cosa_index.json",
		"https://raw.githubusercontent.com/sparkfun/Arduino_Boards/master/IDE_Board_Manager/package_sparkfun_index.json",
		"https://redbearlab.github.io/arduino/package_redbearlab_index.json",
		"https://github.com/chipKIT32/chipKIT-core/raw/master/package_chipkit_index.json" };
	BoardsManager.addPackageURLs(packageUrlsToAdd);
	BoardsManager.installAllLatestPlatforms();
	BoardsManager.referenceLocallInstallation(teensyInstall);
    }

    @Test
    public void testBoard() {
	BoardDescriptor boardid = BoardsManager.getBoardID(this.mJsonFileName, this.mPackageName, this.mPlatform,
		this.mBoardID, this.myOptions);
	if (boardid == null) {
	    fail("Board " + this.mJsonFileName + " " + " " + this.mPackageName + " " + this.mPlatform + " "
		    + this.mBoardID + " not found");
	    return;
	}
	BuildAndVerify(boardid);

    }

    @SuppressWarnings("static-method")
    private void BuildAndVerify(BoardDescriptor boardid) {

	IProject theTestProject = null;
	CodeDescriptor codeDescriptor = CodeDescriptor.createDefaultIno();
	NullProgressMonitor monitor = new NullProgressMonitor();
	String projectName = String.format("%03d_", new Integer(mCounter++)) + boardid.getBoardID();
	try {

	    theTestProject = boardid.createProject(projectName, null, ConfigurationDescriptor.getDefaultDescriptors(),
		    codeDescriptor, monitor);
	    waitForAllJobsToFinish(); // for the indexer
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Failed to create the project:" + projectName);
	    return;
	}
	try {
	    theTestProject.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
	    if (hasBuildErrors(theTestProject)) {
		fail("Failed to compile the project:" + projectName + " build errors");
	    }
	} catch (CoreException e) {
	    e.printStackTrace();
	    fail("Failed to compile the project:" + boardid.getBoardName() + " exception");
	}
    }

    private static boolean hasBuildErrors(IProject project) throws CoreException {
	IMarker[] markers = project.findMarkers(ICModelMarker.C_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
	for (IMarker marker : markers) {
	    if (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) {
		return true;
	    }
	}
	return false;
    }
}
