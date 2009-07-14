/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.robot.rule.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.beans.util.Beans;
import org.seasar.robot.entity.ResponseData;

/**
 * @author shinsuke
 *
 */
public class RegexRule extends AbstractRule {

    private static final long serialVersionUID = 1L;

    protected boolean defaultRule = false;

    protected boolean allRequired = true;

    protected Map<String, Pattern> regexMap = new HashMap<String, Pattern>();

    /* (non-Javadoc)
     * @see org.seasar.robot.rule.impl.AbstractRule#match(org.seasar.robot.entity.ResponseData)
     */
    @Override
    public boolean match(ResponseData responseData) {
        if (defaultRule) {
            return true;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        Beans.copy(responseData, map).excludesWhitespace().execute();
        for (Map.Entry<String, Pattern> entry : regexMap.entrySet()) {
            String value = "";
            Object obj = map.get(entry.getKey());
            if (obj != null) {
                value = obj.toString();
            }
            Matcher matcher = entry.getValue().matcher(value);
            if (allRequired) {
                if (!matcher.matches()) {
                    return false;
                }
            } else {
                if (matcher.matches()) {
                    return true;
                }
            }
        }

        if (allRequired) {
            return true;
        } else {
            return false;
        }
    }

    public void addRule(String key, String regex) {
        regexMap.put(key, Pattern.compile(regex));
    }

    public void addRule(String key, Pattern pattern) {
        regexMap.put(key, pattern);
    }

    public boolean isDefaultRule() {
        return defaultRule;
    }

    public void setDefaultRule(boolean defaultRule) {
        this.defaultRule = defaultRule;
    }

    public boolean isAllRequired() {
        return allRequired;
    }

    public void setAllRequired(boolean allRequired) {
        this.allRequired = allRequired;
    }
}
