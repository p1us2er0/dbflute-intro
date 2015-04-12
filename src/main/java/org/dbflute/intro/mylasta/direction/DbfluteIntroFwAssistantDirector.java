/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.mylasta.direction;

import java.util.List;


/**
 * @author jflute
 */
public class DbfluteIntroFwAssistantDirector extends DbfluteFwAssistantDirector {

    @Override
    protected String getDomainConfigFile() {
        return "dbfluteIntro_config.properties";
    }

    @Override
    protected void setupDomainMessage(List<String> nameList) {
        nameList.add("dbfluteIntro_message"); // concrete name
        nameList.add("dbfluteIntro_label");
    }
}
