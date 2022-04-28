import java.io.*;

import java.time.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import Model.LogDetail;
import Model.Util;

public class App {
    public static List<String> MechineList = new ArrayList<>();
    public static List<String> ActionList = new ArrayList<>();
    public static String LogFolderPath = "E:\\Zoho\\Task-3\\LogFiles";

    public static void main(String[] args) throws Exception {
        // Add Mechine and Action Types
        AddMechineData();
        AddActions();

        // File Write Section

        WriteDataToFile("LogFile17.txt", 5000);
        System.out.println("\n\nData Added");

        // File Read Section

        // File logfiledir = new File(LogFolderPath);
        // File files[] = logfiledir.listFiles();
        // for (File file : files) {
        // List<LogDetail> LogData = GetDataFromFile(file);
        // List<LogDetail> lastActions = AddLastActions(LogData);
        // System.out.println("File Name : " + file.getName() + "\n");
        // if (lastActions.size() != 0) {
        // System.out.println("-------------------------------");
        // PrintData(lastActions);
        // System.out.println("-------------------------------");
        // } else {
        // System.out.println("No Updates in This File");
        // }

        // }

    }

    public static void PrintData(List<LogDetail> Logs) {
        for (LogDetail logDetail : Logs) {
            System.out.println(logDetail.toString());

        }
    }

    public static List<LogDetail> AddLastActions(List<LogDetail> LogsData) throws Exception {
        List<LogDetail> LastActionsData = new ArrayList<>();
        for (String mec : MechineList) {
            Boolean res = Util.IsAlreadyHaveData(mec);
            if (res) {
                LocalDateTime lasttime = Util.GetAbsoluteUpdateDatetime(mec);
                List<LogDetail> filterdata = GetSystemLog(LogsData, mec, lasttime);
                if (filterdata.size() != 0) {
                    LogDetail Lastaction = GetLastAction(filterdata);
                    Util.UpdateLogDetails(Lastaction);
                    LastActionsData.add(Lastaction);
                }
            } else {
                List<LogDetail> filterdata = GetSystemLog(LogsData, mec);
                LogDetail Lastaction = GetLastAction(filterdata);
                Util.AddLogDetails(Lastaction);
                LastActionsData.add(Lastaction);
            }

        }
        return LastActionsData;

    }

    public static void WriteDataToFile(String fileName, int DataCount) throws Exception {
        File logfile = new File(LogFolderPath + "\\" + fileName);
        FileWriter FWriter = new FileWriter(logfile.getAbsoluteFile(), true);
        BufferedWriter Bwriter = new BufferedWriter(FWriter);
        List<LogDetail> LoggerData = ForLogData(DataCount);
        for (LogDetail logDetail : LoggerData) {
            Bwriter.write(logDetail.toString() + "\n");

        }
        Bwriter.close();
    }

    public static List<LogDetail> GetDataFromFile(File logfile) throws Exception {
        List<LogDetail> logdata = new ArrayList<>();
        File file = logfile;
        FileReader Freader = new FileReader(file.getCanonicalFile());
        BufferedReader Breader = new BufferedReader(Freader);
        String value = "";

        while ((value = Breader.readLine()) != null) {
            String[] obj = value.split(Pattern.quote("|"));
            LogDetail ld = new LogDetail();
            ld.SystemName = obj[0];
            ld.ActionType = obj[1];
            ld.ActionDate = LocalDateTime.parse(obj[2]);
            logdata.add(ld);

        }
        Breader.close();

        return logdata;

    }

    public static List<LogDetail> GetSystemLog(List<LogDetail> logdata, String systemname) {
        List<LogDetail> logs = logdata.stream().filter(temp -> temp.SystemName.equals(systemname))
                .collect(Collectors.toList());
        return logs;
    }

    public static List<LogDetail> GetSystemLog(List<LogDetail> logdata, String systemname,
            LocalDateTime LastUpdateTime) {
        List<LogDetail> logs = logdata.stream()
                .filter(temp -> temp.SystemName.equals(systemname) && temp.ActionDate.compareTo(LastUpdateTime) > 0)
                .collect(Collectors.toList());
        return logs;
    }

    public static LogDetail GetLastAction(List<LogDetail> logdetails) {
        LogDetail CurrUpdate = new LogDetail();
        int i = 0;
        for (LogDetail logModule : logdetails) {
            if (i == 0) {
                CurrUpdate = logModule;
                i++;
            } else {
                if (CurrUpdate.ActionDate.compareTo(logModule.ActionDate) < 0
                        || CurrUpdate.ActionDate.compareTo(logModule.ActionDate) == 0) {
                    CurrUpdate = logModule;
                }

            }

        }
        return CurrUpdate;
    }

    public static String GetRandomMachile() {
        int size = MechineList.size();

        Random rd = new Random();
        int number = rd.nextInt(0, size);
        String mec = MechineList.get(number);
        return mec;

    }

    public static List<LogDetail> ForLogData(int Number) throws Exception {
        List<LogDetail> LogData = new ArrayList<>();
        for (int i = 0; i < Number; i++) {
            LogDetail LD = new LogDetail();
            LD.SystemName = GetRandomMachile();
            LD.ActionType = GetRandomAction();
            LD.ActionDate = LocalDateTime.now();
            LogData.add(LD);
            Thread.sleep(100);

            System.out.print("\t" + (i + 1));

        }
        return LogData;

    }

    public static String GetRandomAction() {
        int Size = ActionList.size();

        Random rd = new Random();
        int number = rd.nextInt(0, Size);
        String action = ActionList.get(number);

        return action;
    }

    public static void AddMechineData() {
        // Add 20 Mechines by default
        for (int i = 1; i <= 20; i++) {
            MechineList.add("SystemNo" + i);
        }
    }

    public static void AddActions() {
        ActionList.add("Insert");
        ActionList.add("Update");
        ActionList.add("Delete");
    }
}
