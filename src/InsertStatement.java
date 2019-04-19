public class InsertStatement extends SQLStatement{


    public InsertStatement(String siebelString) {
        super(siebelString);
    }


    @Override
    public void replaceLog(){
        this.siebelReplaceLog = this.siebelLog;
        try {
            for(String i:keyList){
                System.out.println("Заменяю это: " + i + " На это:" + this.keyValueTable.get(i));
                this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst("" + i + ",", "'" + this.keyValueTable.get(i) + "',");
            }

            System.out.println(keyList.get(keyList.size()-1)); //Заменем последний элемент
            this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst(", " + keyList.get(keyList.size()-1), ", '" + this.keyValueTable.get(keyList.get(keyList.size()-1)) + "'");
        }catch (NullPointerException e){
            System.out.println("Что-то пошло не так в методе replaceLog");
        }
    }
}
