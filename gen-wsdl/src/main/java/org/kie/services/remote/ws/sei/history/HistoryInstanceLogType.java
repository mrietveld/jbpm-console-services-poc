package org.kie.services.remote.ws.sei.history;

import javax.xml.bind.annotation.XmlType;

@XmlType
public enum HistoryInstanceLogType {

    PROCESS, NODE, VARIABLE, TASK;
}
