import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiebelLog {

    static ArrayList<String> keyList = new ArrayList<String>(); //Тут хранятся бинды, на место которых надо поставить значения
    static Hashtable tableKeyValue = new Hashtable();
    static String siebelStringTotal;

    public static void replaceLog(String siebelString){

        Pattern selectPattern = Pattern.compile("SELECT\\n");
        Pattern updatePattern = Pattern.compile("UPDATE\\n");

        boolean select = selectPattern.matcher(siebelString).find();
        boolean update = updatePattern.matcher(siebelString).find();

        if(select){
            System.out.println("Я НАШЕЛ СЕЛЕКТ");
        }

        getKeyList(siebelString); //Получаем лист с биндами, на место которых надо поставить значения

        getKeyValueMap(siebelString);//Получаем Table: ключ - бинд, значение - значение бинда


        if(select){
            //Удаляем первое значение из таблицы, так в первом бинде лога фигня
            tableKeyValue.remove(":1");
            // System.out.println(tableKeyValue);
            keyList.remove(0);
        }

        siebelString  = siebelString.substring(0, siebelString.indexOf("ObjMgrSqlLog"));

        //Заменяем в логе бинды на значения
        siebelStringTotal = replaceBinds(siebelString);

        //Убираем первый бинд для селекта
        if(select){
            siebelStringTotal = siebelStringTotal.replaceAll(",\n.*:1", "");
            System.out.println("ЭТО СЕЛЕКТ");
        }

    }



    public static String replaceBinds(String siebelString){
        try {
            for(String i:keyList){
                System.out.println("Заменяю это: " + i + " На это:" + tableKeyValue.get(i));
                siebelString = siebelString.replaceFirst("" + i, "'" + tableKeyValue.get(i) + "'");
            }
        }catch (NullPointerException e){
            System.out.println("Что-то пошло не так в методе replaceBinds");
        }
    return siebelString;
    }




    public static void getKeyValueMap(String siebelString){
        Pattern p = Pattern.compile("Bind variable \\d+:.*");
        Matcher m = p.matcher(siebelString);

        while (m.find()){
            String tempKey = m.group().substring(m.group().indexOf("ble")+4, m.group().indexOf(':')+1);
            String key = tempKey.replaceAll("\\d+:", ":" + tempKey.substring(0,tempKey.indexOf(':')));
            tableKeyValue.put(key, m.group().substring(m.group().indexOf(':')+2));
        }
    }


    public static void getKeyList(String siebelString){
        Pattern p = Pattern.compile("( :[0-9]+)|(:[0-9]+,)|(:[0-9]+\\))");
        Matcher m = p.matcher(siebelString);

        while (m.find()){
            String k = m.group();
            if(k.substring(k.length()-1).equals(",")){
                keyList.add(m.group().substring(0, m.group().indexOf(',')));
            }else if(k.charAt(0) == ' '){
                keyList.add(m.group().substring(1));
            }else if(k.substring(k.length()-1).equals(")")){
                keyList.add(m.group().substring(0, m.group().indexOf(')')));
            }else{
                keyList.add(m.group());
            }

        }
    }
}
