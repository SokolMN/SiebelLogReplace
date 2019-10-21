import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLStatement {
    public String siebelLog; //Лог с биндами
    public String siebelReplaceLog; //Лог переведенный
    public ArrayList<String> keyList = new ArrayList<>();
    public Hashtable keyValueTable = new Hashtable();


    public String getSiebelReplaceLog(){
        this.siebelReplaceLog = this.siebelReplaceLog.replaceAll("\\*", "%");
        return this.siebelReplaceLog;
    }

    public void cutObjBinds(){
        try {
            this.siebelReplaceLog = this.siebelReplaceLog.substring(0, this.siebelReplaceLog.indexOf("ObjMgrSqlLog"));
        }catch(StringIndexOutOfBoundsException e) {
            //this.siebelReplaceLog = this.siebelReplaceLog.replaceAll("(ObjMgrSqlLog.*)|(SQLParseAndExecute.*)", "");
            this.siebelReplaceLog = this.siebelReplaceLog.substring(0, this.siebelReplaceLog.indexOf("SQLParseAndExecute"));
        }
    }



    private String removeFirstBind(String logForSelect){
        this.keyList.remove(0);
        this.keyValueTable.remove(":1");
        this.keyValueTable.remove(":0");
        this.keyList.remove(":0");
        logForSelect= logForSelect.replaceAll(",\n.*:1", "");

        return logForSelect;
    }


    public SQLStatement(String siebelString){
        this.siebelLog = siebelString;
    }



    public void replaceLog(){

        Pattern selectPattern = Pattern.compile("^.{0,2}SELECT.*\\n");
        boolean selectFlg = selectPattern.matcher(this.siebelLog).find();

        if(selectFlg){
            this.siebelReplaceLog = removeFirstBind(this.siebelLog);
        }else{
            this.siebelReplaceLog = this.siebelLog;
        }


        try {
            for(String i:keyList){
                System.out.println("Заменяю это: " + i + " На это:" + this.keyValueTable.get(i));
                this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst("" + i, "'" + this.keyValueTable.get(i) + "'");
            }
        }catch (NullPointerException e){
            System.out.println("Что-то пошло не так в методе replaceLog");
        }
    }


    public void setKeyValueTable(){
        //Pattern p = Pattern.compile("(Bind variable \\d+:.*)|(\\s\\d:)"); Старая версия
        Pattern p = Pattern.compile("\\s\\d{1,3}:\\s.*");
        Matcher m = p.matcher(this.siebelLog);

        /*while (m.find()){
            String tempKey = m.group().substring(m.group().indexOf("ble")+4, m.group().indexOf(':')+1);
            String key = tempKey.replaceAll("\\d+:", ":" + tempKey.substring(0,tempKey.indexOf(':')));
            this.keyValueTable.put(key, m.group().substring(m.group().indexOf(':')+2));
        }*///Старая версия
        while (m.find()){
            String tempKey = m.group();
            String key = ":" + tempKey.substring(1, tempKey.indexOf(":"));
            this.keyValueTable.put(key, m.group().substring(m.group().indexOf(':')+2));
        }

        System.out.println("ЭТО МЕТОД setKeyValueTable");
        System.out.println(keyValueTable.toString());
    }


    public Hashtable getKeyValueTable() {
        return this.keyValueTable;
    }

    public ArrayList<String> getKeyList(){
        return this.keyList;
    }



    public void setKeyList(){
        Pattern p = Pattern.compile("( :[0-9]+)|(:[0-9]+,)|(:[0-9]+\\))");
        Matcher m = p.matcher(this.siebelLog);

        while (m.find()){
            String k = m.group();
            if(k.substring(k.length()-1).equals(",")){
                this.keyList.add(m.group().substring(0, m.group().indexOf(',')));
            }else if(k.charAt(0) == ' '){
                this.keyList.add(m.group().substring(1));
            }else if(k.substring(k.length()-1).equals(")")){
                this.keyList.add(m.group().substring(0, m.group().indexOf(')')));
            }else{
                this.keyList.add(m.group());
            }
        }
    }
}
