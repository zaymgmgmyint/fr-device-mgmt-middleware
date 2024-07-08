package com.eighti.onebkk.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eighti.onebkk.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class SchedulerTask {

	public static final Logger LOG = LoggerFactory.getLogger(SchedulerTask.class);

	private static final long usersToFRDevicesFixedRate = 600000; // 10 minutes

	private final UserService userService;

	public SchedulerTask(UserService userService) {
		this.userService = userService;
	}

	@PostConstruct
	public void onStartUp() {
	}

	@Scheduled(fixedRate = usersToFRDevicesFixedRate)
	public void syncUsersToFRDevicesTask() {
		LOG.info("\n===== START Perform Data Synchronizing =====");

		try {
			userService.syncUsersToFRDevices();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("syncUsersToFRDevicesTask() >>> ERROR: " + e.getMessage());
		}

		LOG.info("\n===== END Perform Data Synchronizing =====\n");

	}

}
