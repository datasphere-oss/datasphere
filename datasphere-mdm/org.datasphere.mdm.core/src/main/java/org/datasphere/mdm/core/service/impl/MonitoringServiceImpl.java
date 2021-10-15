/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 * 
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.prometheus.client.Counter;

import org.datasphere.mdm.core.service.MonitoringService;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {
	private static final Map<String, Counter> COUNTERS = new ConcurrentHashMap<>();

	@Override
	public void registerCounter(String name, String description) {
		if(COUNTERS.containsKey(name)) {
			return;
		}
		Counter counter = Counter.build().name(name).help(description).create().register();
		COUNTERS.put(name, counter);

	}

	@Override
	public void incrementСounter(String name) {
		COUNTERS.get(name).inc();

	}

	@Override
	public void incrementCounter(String name, long value) {
		COUNTERS.get(name).inc(value);

	}

}
