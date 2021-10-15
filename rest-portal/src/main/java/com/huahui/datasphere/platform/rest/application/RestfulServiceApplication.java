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

package com.huahui.datasphere.platform.rest.application;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.*;

@SpringBootApplication
@ComponentScan({"com.huahui.datasphere.platform.rest"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class RestfulServiceApplication
{
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(new Class[] { RestfulServiceApplication.class });
    }
    
    public static void main(final String[] args) {
        SpringApplication.run(RestfulServiceApplication.class, args);
    }
}
