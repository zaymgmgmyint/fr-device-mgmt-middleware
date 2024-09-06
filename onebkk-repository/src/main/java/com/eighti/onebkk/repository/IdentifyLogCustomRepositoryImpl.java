package com.eighti.onebkk.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eighti.onebkk.dto.PaginatedResponse;
import com.eighti.onebkk.entity.IdentifyLog;
import com.eighti.onebkk.utils.CommonUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
@SuppressWarnings("unchecked")
public class IdentifyLogCustomRepositoryImpl implements IdentifyLogCustomRepository {

	private static final Logger LOG = LoggerFactory.getLogger(IdentifyLogCustomRepositoryImpl.class);

	private static final int BASE_PAGE_SIZE = 100;
	private static final int PAGE_NO = 1;

	@Autowired
	private EntityManager entityManager;

	@Override
	public PaginatedResponse<IdentifyLog> searchIdentifyLogs(LocalDateTime fromDate, LocalDateTime toDate, String role,
			String company, List<String> deviceKeys, List<String> models, List<String> identifyTypes,
			List<String> deviceIps, Integer page) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"SELECT iLog.id, iLog.alive_type, iLog.base64, iLog.device_key, iLog.id_card_num, iLog.identify_type, iLog.ip, iLog.model, iLog.pass_time_type, iLog.path, iLog.permission_time_type, iLog.person_id,");
		sqlBuilder.append(
				" iLog.rec_type, iLog.time, iLog.dst_offset, iLog.passback_trigger_type, iLog.mask_state, iLog.work_code, iLog.attendance, iLog.type, iLog.log_datetime, iLog.log_time, iLog.ip");

		sqlBuilder.append(" FROM identify_log iLog");
		sqlBuilder.append(" LEFT JOIN user iUser ON iUser.user_id=iLog.person_id");
		sqlBuilder.append(" WHERE 1=1");

		StringBuilder countSqlBuilder = new StringBuilder();
		countSqlBuilder.append("SELECT COUNT(iLog.id) FROM identify_log iLog");
		countSqlBuilder.append(" LEFT JOIN user iUser ON iUser.user_id=iLog.person_id");
		countSqlBuilder.append(" WHERE 1=1");

		if (fromDate != null) {
			sqlBuilder.append(" AND iLog.log_datetime >= :fromDate");
			countSqlBuilder.append(" AND iLog.log_datetime >= :fromDate");
		}

		if (toDate != null) {
			sqlBuilder.append(" AND iLog.log_datetime <= :toDate");
			countSqlBuilder.append(" AND iLog.log_datetime <= :toDate");
		}

		if (CommonUtil.validList(deviceKeys)) {
			sqlBuilder.append(" AND iLog.device_key IN (:deviceKeys)");
			countSqlBuilder.append(" AND iLog.device_key IN (:deviceKeys)");
		}

		if (CommonUtil.validList(deviceIps)) {
			StringBuilder likeConditions = new StringBuilder();
			for (int i = 0; i < deviceIps.size(); i++) {
				if (i > 0) {
					likeConditions.append(" OR ");
				}
				likeConditions.append("iLog.ip LIKE :deviceIp" + i);
			}

			sqlBuilder.append(" AND (").append(likeConditions).append(")");
			countSqlBuilder.append(" AND (").append(likeConditions).append(")");
		}

		if (CommonUtil.validList(models)) {
			sqlBuilder.append(" AND iLog.model IN (:models)");
			countSqlBuilder.append(" AND iLog.model IN (:models)");
		}

		if (CommonUtil.validList(identifyTypes)) {
			sqlBuilder.append(" AND iLog.identify_type IN (:identifyTypes)");
			countSqlBuilder.append(" AND iLog.identify_type IN (:identifyTypes)");
		}

		if (CommonUtil.validString(role)) {
			sqlBuilder.append(" AND iUser.user_tag LIKE :role");
			countSqlBuilder.append(" AND iUser.user_tag LIKE :role");
		}

		if (CommonUtil.validString(company)) {
			sqlBuilder.append(" AND iUser.company LIKE :company");
			countSqlBuilder.append(" AND iUser.company LIKE :company");
		}

		sqlBuilder.append(" ORDER BY iLog.id DESC");
		countSqlBuilder.append(" ORDER BY iLog.id DESC");

		Query query = entityManager.createNativeQuery(sqlBuilder.toString());
		Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());

		if (fromDate != null) {
			query.setParameter("fromDate", fromDate);
			countQuery.setParameter("fromDate", fromDate);
		}

		if (toDate != null) {
			query.setParameter("toDate", toDate);
			countQuery.setParameter("toDate", toDate);
		}

		if (CommonUtil.validList(deviceKeys)) {
			query.setParameter("deviceKeys", deviceKeys);
			countQuery.setParameter("deviceKeys", deviceKeys);
		}

		if (CommonUtil.validList(deviceIps)) {
			for (int i = 0; i < deviceIps.size(); i++) {
				query.setParameter("deviceIp" + i, "%" + deviceIps.get(i) + "%");
				countQuery.setParameter("deviceIp" + i, "%" + deviceIps.get(i) + "%");
			}
		}

		if (CommonUtil.validList(models)) {
			query.setParameter("models", models);
			countQuery.setParameter("models", models);
		}

		if (CommonUtil.validList(identifyTypes)) {
			query.setParameter("identifyTypes", identifyTypes);
			countQuery.setParameter("identifyTypes", identifyTypes);
		}

		if (CommonUtil.validString(role)) {
			query.setParameter("role", "%" + role + "%");
			countQuery.setParameter("role", "%" + role + "%");
		}

		if (CommonUtil.validString(company)) {
			query.setParameter("company", "%" + company + "%");
			countQuery.setParameter("company", "%" + company + "%");
		}

		// Set pagination parameters
		if (page > 0) {
			query.setFirstResult((page - 1) * BASE_PAGE_SIZE);
		} else {
			query.setFirstResult((PAGE_NO - 1) * BASE_PAGE_SIZE);
		}

		query.setMaxResults(BASE_PAGE_SIZE);

		List<Object[]> results = query.getResultList();
		long totalRecords = (long) countQuery.getSingleResult();

		List<IdentifyLog> entityList = new ArrayList<IdentifyLog>();

		for (Object[] result : results) {
			IdentifyLog log = new IdentifyLog();
			log.setId(((Long) result[0]).longValue());
			log.setAliveType((String) result[1]);
			log.setBase64((String) result[2]);
			log.setDeviceKey((String) result[3]);
			log.setIdcardNum((String) result[4]);
			log.setIdentifyType((String) result[5]);
			log.setIp((String) result[6]);
			log.setModel((String) result[7]);
			log.setPassTimeType((String) result[8]);
			log.setPath((String) result[9]);
			log.setPermissionTimeType((String) result[10]);
			log.setPersonId((String) result[11]);
			log.setRecType((Integer) result[12]);
			log.setTime((String) result[13]);
			log.setDstOffset((Integer) result[14]);
			log.setPassbackTriggerType((Integer) result[15]);
			log.setMaskState((Integer) result[16]);
			log.setWorkCode((String) result[17]);
			log.setAttendance((String) result[18]);
			log.setType((String) result[19]);

			if (result[20] != null) {
				if (result[20] instanceof java.sql.Timestamp) {
					log.setLogDateTime(((java.sql.Timestamp) result[20]).toLocalDateTime());
				} else {
					LOG.error("Unexpected type for logDateTime: " + result[20].getClass());
					throw new IllegalArgumentException("Unexpected type for logDateTime: " + result[20].getClass());
				}
			} else {
				log.setLogDateTime(null);
			}

			if (result[21] != null) {
				if (result[21] instanceof java.sql.Date) {
					log.setLogTime(((java.sql.Date) result[21]).toLocalDate());
				} else {
					LOG.error("Unexpected type for logTime: " + result[21].getClass());
					throw new IllegalArgumentException("Unexpected type for logTime: " + result[21].getClass());
				}
			} else {
				log.setLogTime(null);
			}

			log.setIp((String) result[22]);

			entityList.add(log);
		}

		return new PaginatedResponse<IdentifyLog>(totalRecords, entityList);

	}

}
