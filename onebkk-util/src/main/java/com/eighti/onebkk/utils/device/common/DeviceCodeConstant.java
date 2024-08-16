package com.eighti.onebkk.utils.device.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class DeviceCodeConstant {

	@Getter
	public enum AliveTypeEnum {

		NO_1("1", "Living body judgment successful"), NO_2("2", "Living body judgment successful"),
		NO_3("3", "No in vivo judgment has been carried out"), UNKNOW("000", "Unknown");

		private String code;
		private String desc;

		private AliveTypeEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDescriptionByCode(String code) {
			for (AliveTypeEnum type : AliveTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return AliveTypeEnum.UNKNOW.getDesc();
		}

	}

	@Getter
	public enum ComparisonTypeEnum {

		FACE_0("face_0 ", "Face recognition, and the person is within passtime", "FACE"),
		FACE_1("face_1", "Face recognition, and the person is outside the passtime", "FACE"),
		FACE_2("face_2", "Face recognition/mask detection, and recognition failed/mask detection failed", "FACE"),

		CARD_0("card_0", "swipe card identification, and the person is within the passtime authority time", "CARD"),
		CARD_1("card_1", "swipe card identification, and the person is outside the passtime", "CARD"),
		CARD_2("card_2", "swipe card recognition, and recognition failed", "CARD"), UNKNOW("000", "Unknown", "Unknown");

		private String code;
		private String desc;
		private String msg;

		private ComparisonTypeEnum(String code, String desc, String msg) {
			this.code = code;
			this.desc = desc;
			this.msg = msg;
		}

		public static String getDescriptionByCode(String code) {
			for (ComparisonTypeEnum type : ComparisonTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return ComparisonTypeEnum.UNKNOW.getDesc();
		}

		public static String getMsgByCode(String code) {
			for (ComparisonTypeEnum type : ComparisonTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getMsg();
				}
			}
			return ComparisonTypeEnum.UNKNOW.getMsg();
		}
	}
	

	@Getter
	public enum IdentifyTypeEnum {

		NO_1("1", "Comparison successful"), NO_2("2", "Comparison failed"), NO_3("3", "No comparison is made"),

		UNKNOW("000", "Unknown");

		private String code;
		private String desc;

		private IdentifyTypeEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDescriptionByCode(String code) {
			for (IdentifyTypeEnum type : IdentifyTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return IdentifyTypeEnum.UNKNOW.getDesc();
		}

		public static List<String> getIdentifyTypes(String code) {
			List<String> identifyTypes = new ArrayList<String>();

			if (IdentifyTypeEnum.NO_1.getCode().equals(code)) {
				identifyTypes.addAll(Arrays.asList(IdentifyTypeEnum.NO_1.getCode()));
			} else if (IdentifyTypeEnum.NO_2.getCode().equals(code)) {
				identifyTypes.addAll(Arrays.asList(IdentifyTypeEnum.NO_2.getCode()));
			} else {
				identifyTypes.addAll(Arrays.asList(IdentifyTypeEnum.NO_1.getCode(), IdentifyTypeEnum.NO_2.getCode(),
						IdentifyTypeEnum.NO_3.getCode()));
			}

			return identifyTypes;
		}
	}

	@Getter
	public enum IdentifyPatternEnum {

		NO_0("0", "Portrait verification", "FACE"), NO_1("1", "Card & portrait verification (person card in one)", "CARD & FACE"),
		NO_2("2", "Witnesses comparison", "FACE"), NO_3("3", "Card verification", "CARD"),
		NO_4("4", "Button to open the door (signal to open the door)", "OPEN DOOR"),

		UNKNOW("000", "Unknown", "Unknown");

		private String code;
		private String desc;
		private String msg;

		private IdentifyPatternEnum(String code, String desc, String msg) {
			this.code = code;
			this.desc = desc;
			this.msg = msg;
		}

		public static String getDescriptionByCode(String code) {
			for (IdentifyPatternEnum type : IdentifyPatternEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return IdentifyPatternEnum.UNKNOW.getDesc();
		}
		
		public static String getMsgByCode(String code) {
			for (IdentifyPatternEnum type : IdentifyPatternEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getMsg();
				}
			}
			return IdentifyPatternEnum.UNKNOW.getMsg();
		}
	}

	@Getter
	public enum AuthTypeEnum {

		FACE("FACE", "Face"), CARD("CARD", "Card"), QR("QR", "QR"), FINGER_PRINT("FB", "Fingerprint"),
		ALL("ALL", "All");

		private String code;
		private String desc;

		private AuthTypeEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDescriptionByCode(String code) {
			for (AuthTypeEnum type : AuthTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return AuthTypeEnum.ALL.getDesc();
		}

		public static List<String> getIdentifyPatterns(String code) {
			List<String> identifyPatterns = new ArrayList<String>();

			if (AuthTypeEnum.FACE.getCode().equals(code)) {
				identifyPatterns
						.addAll(Arrays.asList(IdentifyPatternEnum.NO_0.getCode(), IdentifyPatternEnum.NO_1.getCode()));
			} else if (AuthTypeEnum.CARD.getCode().equals(code)) {
				identifyPatterns.addAll(Arrays.asList(IdentifyPatternEnum.NO_3.getCode()));
			} else {
				identifyPatterns
						.addAll(Arrays.asList(IdentifyPatternEnum.NO_0.getCode(), IdentifyPatternEnum.NO_1.getCode(),
								IdentifyPatternEnum.NO_2.getCode(), IdentifyPatternEnum.NO_3.getCode()));
			}

			return identifyPatterns;
		}
	}

}
