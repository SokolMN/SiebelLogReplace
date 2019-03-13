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
        return this.siebelReplaceLog;
    }

    public void cutObjBinds(){
        this.siebelReplaceLog = this.siebelReplaceLog.substring(0, this.siebelReplaceLog.indexOf("ObjMgrSqlLog"));
    }

    public void removeFirstBind(){
        //Пустой метод для того, чтобы можно было использовать полиморфизм в классе Main
    }

    public SQLStatement(String siebelString){
        this.siebelLog = siebelString;
    }

    public void replaceLog(){
        this.siebelReplaceLog = this.siebelLog;
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
        Pattern p = Pattern.compile("Bind variable \\d+:.*");
        Matcher m = p.matcher(this.siebelLog);

        while (m.find()){
            String tempKey = m.group().substring(m.group().indexOf("ble")+4, m.group().indexOf(':')+1);
            String key = tempKey.replaceAll("\\d+:", ":" + tempKey.substring(0,tempKey.indexOf(':')));
            this.keyValueTable.put(key, m.group().substring(m.group().indexOf(':')+2));
        }
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
