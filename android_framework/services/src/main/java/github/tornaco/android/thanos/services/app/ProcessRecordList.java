package github.tornaco.android.thanos.services.app;

import java.util.HashSet;
import java.util.Set;

import github.tornaco.android.thanos.core.process.ProcessRecord;
import lombok.Getter;
import util.CollectionUtils;

@Getter
class ProcessRecordList implements Comparable<ProcessRecordList> {

    private long lastUpdateTime = System.currentTimeMillis();
    private Set<ProcessRecord> processRecords = new HashSet<>();
    private String packageName;

    ProcessRecordList(String packageName) {
        this.packageName = packageName;
    }

    void addProcessRecord(ProcessRecord record) {
        lastUpdateTime = System.currentTimeMillis();
        processRecords.add(record);
    }

    boolean removeProcessRecord(ProcessRecord record) {
        lastUpdateTime = System.currentTimeMillis();
        return processRecords.remove(record);
    }

    boolean isEmpty() {
        return CollectionUtils.isNullOrEmpty(processRecords);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(ProcessRecordList processRecordList) {
        return Long.compare(processRecordList.lastUpdateTime, lastUpdateTime);
    }
}
