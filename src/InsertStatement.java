import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertStatement extends SQLStatement{


    public InsertStatement(String siebelString) {
        super(siebelString);
    }


    @Override
    public void replaceLog(){
        this.siebelReplaceLog = this.siebelLog;
        try {
            for(String i:keyList){
                System.out.println("INSERT: Заменяю это: " + i + " На это:" + this.keyValueTable.get(i));
                this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst("" + i + ",", "'" + this.keyValueTable.get(i) + "',");
            }

            System.out.println(keyList.get(keyList.size()-1)); //Заменем последний элемент
            this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst(", " + keyList.get(keyList.size()-1), ", '" + this.keyValueTable.get(keyList.get(keyList.size()-1)) + "'");
        }catch (NullPointerException e){
            System.out.println("Что-то пошло не так в методе replaceLog");
        }
    }

    public void addingValuesInComments(){

        String siebelReplaceLogWithoutValues = this.siebelReplaceLog.substring(0, this.siebelReplaceLog.indexOf("VALUES"));
        String siebelReplaceLogValues = this.siebelReplaceLog.substring(this.siebelReplaceLog.indexOf("VALUES"));

        Hashtable columnToValue = new Hashtable(); //Тут хранится <Название колонки>:<Значение колонки>

        Pattern p = Pattern.compile("(.*,)|(.*\\))"); //Вытаскивание названий колонок
        Matcher m = p.matcher(siebelReplaceLogWithoutValues);


        Pattern pvalues = Pattern.compile("(\\'.*?\\'(\\)){0,1})|(current_date)|(SIEBEL.*NEXTVAL)"); //Вытаскивание значений колонок
        Matcher mvalues = pvalues.matcher(siebelReplaceLogValues);


        while (m.find() && mvalues.find() ) {//Заполняем мапу COLUMN:/*VALUE*/
            System.out.println(m.group(0) + " /*" + mvalues.group(0) + "*/");
            columnToValue.put(m.group(0), mvalues.group(0));
        }

        System.out.println(columnToValue.toString());

        Set<String> keys = columnToValue.keySet();

        System.out.println(siebelReplaceLog);
        for(String key : keys) {
            System.out.println("Выбираем значение для ключа:" + key + " Со значением:" + columnToValue.get(key));

            if (key.substring(key.length() - 1).equals(")")){
                key = key.substring(0, key.indexOf(")"));
                this.siebelReplaceLog = this.siebelReplaceLog.replaceAll(key+"\\)",key + ") /*" + columnToValue.get(key+")") + "*/");
            }
            this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst(key,key + " /*" + columnToValue.get(key) + "*/");
        }
    }
}
