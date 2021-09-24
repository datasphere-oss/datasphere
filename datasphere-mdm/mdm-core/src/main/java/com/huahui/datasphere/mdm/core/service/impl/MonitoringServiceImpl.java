/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.core.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.prometheus.client.Counter;
import org.springframework.stereotype.Service;

import com.huahui.datasphere.mdm.core.service.MonitoringService;

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
