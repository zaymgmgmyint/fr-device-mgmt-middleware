package com.eighti.onebkk.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eighti.onebkk.service.DeviceService;
import com.eighti.onebkk.service.UserService;

@Component
public class SchedulerTask {

	public static final Logger LOG = LoggerFactory.getLogger(SchedulerTask.class);

	private static final long usersToFRDevicesFixedRate = 300000; // 5 minutes
	private static final long checkDeviceStatusFixedRate = 60000; // 1 minutes
	private static final long fetchIdentifyLogFixedRate = 60000; // 1 minutes

	private final UserService userService;
	private final DeviceService deviceService;

	public SchedulerTask(UserService userService, DeviceService deviceService) {
		this.userService = userService;
		this.deviceService = deviceService;
	}

	// @Scheduled(fixedRate = usersToFRDevicesFixedRate, initialDelay = 10000)
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

	// @Scheduled(fixedRate = checkDeviceStatusFixedRate, initialDelay = 10000)
	public void checkdeviceOnlineOrOfflineTask() {
		LOG.info("\n===== START Perform Device Checking =====");

		try {
			deviceService.updateDeviceStatusOnlineOrOffline();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("checkdeviceOnlineOrOfflineTask() >>> ERROR: " + e.getMessage());
		}

		LOG.info("\n===== END Perform Device Checking =====\n");
	}

	@Scheduled(fixedRate = fetchIdentifyLogFixedRate)
	public void fetchIdentifyLogFromDeviceTask() {
		LOG.info("\n===== START Perform Fetch Identify Log =====");

		try {
			deviceService.fetchIdentifyLogFromDevices();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fetchIdentifyLogFromDeviceTask() >>> ERROR: " + e.getMessage());
		}

		LOG.info("\n===== END Perform Fetch Identify Log =====\n");

	}

}
