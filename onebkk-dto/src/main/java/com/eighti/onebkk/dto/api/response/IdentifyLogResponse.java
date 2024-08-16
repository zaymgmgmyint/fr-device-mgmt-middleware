package com.eighti.onebkk.dto.api.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifyLogResponse implements Serializable {

	private static final long serialVersionUID = 7539427490853270251L;

	private String code;
	private String msg;
	private Integer result;
	private Boolean success;
	
	private DataContainer data;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DataContainer implements Serializable {

		private static final long serialVersionUID = -78827787558108692L;

		private PageInfo pageInfo;
		private List<Record> records;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PageInfo implements Serializable {

		private static final long serialVersionUID = -78827787558108692L;

		private Integer index;
		private Integer length;
		private Integer size;
		private Integer total;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Record implements Serializable {

		private static final long serialVersionUID = -7778405689612844856L;

		private Integer aliveType;
		private String attendance;
		private Integer dstOffset;
		private String id;
		private String idcardNum;
		private Integer identifyType;
		private Integer isImgDeleted;
		private Boolean isPass;
		private Integer maskState;
		private Integer model;
		private String name;
		private Integer passTimeType;
		private String passbackTriggerType;
		private String path;
		private Integer permissionTimeType;
		private String personId;
		private Integer recModeType;
		private Integer recType;
		private Integer state;
		private Long time;
		private Integer type;
		private String workCode;
		private String workCode2;
	}
}
