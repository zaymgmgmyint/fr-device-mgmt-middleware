package com.eighti.onebkk.utils.device.common;

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

		FACE_0("face_0 ", "Face recognition, and the person is within passtime"),
		FACE_1("face_0", "Face recognition, and the person is outside the passtime"),
		FACE_2("face_2", "Face recognition/mask detection, and recognition failed/mask detection failed"),

		CARD_0("card_0", "swipe card identification, and the person is within the passtime authority time"),
		CARD_1("card_1", "swipe card identification, and the person is outside the passtime"),
		CARD_2("card_2", "swipe card recognition, and recognition failed"),

		UNKNOW("000", "Unknown");

		private String code;
		private String desc;

		private ComparisonTypeEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDescriptionByCode(String code) {
			for (ComparisonTypeEnum type : ComparisonTypeEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return ComparisonTypeEnum.UNKNOW.getDesc();
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
	}

	@Getter
	public enum IdentifyPatternEnum {

		NO_0("0", "Portrait verification"), NO_1("1", "Card & portrait verification (person card in one)"),
		NO_2("2", "Witnesses comparison"), NO_3("3", "Card verification"),
		NO_4("4", "Button to open the door (signal to open the door)"),

		UNKNOW("000", "Unknown");

		private String code;
		private String desc;

		private IdentifyPatternEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDescriptionByCode(String code) {
			for (IdentifyPatternEnum type : IdentifyPatternEnum.values()) {
				if (code.equals(type.getCode())) {
					return type.getDesc();
				}
			}
			return IdentifyPatternEnum.UNKNOW.getDesc();
		}
	}

}
