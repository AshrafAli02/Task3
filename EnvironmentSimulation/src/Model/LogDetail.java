package Model;

import java.time.LocalDateTime;

public class LogDetail {
    public String SystemName;
    public String ActionType;
    public LocalDateTime ActionDate;

    public LogDetail() {

    }

    public LogDetail(String systemname, String actiontype, LocalDateTime actiontime) {
        this.SystemName = systemname;
        this.ActionType = actiontype;
        this.ActionDate = actiontime;
    }

    @Override
    public String toString() {
        String Value = this.SystemName + "|" + this.ActionType + "|" + this.ActionDate;
        return Value;
    }
}