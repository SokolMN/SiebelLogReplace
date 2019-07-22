public class UpdateStatement extends SQLStatement{

    public UpdateStatement(String siebelString) {
        super(siebelString);
    }

    @Override
    public void replaceLog(){
        this.siebelReplaceLog = this.siebelLog;
        System.out.println("ЭТО UPDATE");
        try {
            for(String i:keyList){
                System.out.println("Заменяю это: " + i + " На это:" + this.keyValueTable.get(i));
                this.siebelReplaceLog = this.siebelReplaceLog.replaceFirst("= " + i, "= '" + this.keyValueTable.get(i) + "'");
            }
        }catch (NullPointerException e){
            System.out.println("Что-то пошло не так в методе replaceLog");
        }
    }
}
