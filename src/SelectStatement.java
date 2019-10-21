public class SelectStatement extends SQLStatement{

    public SelectStatement(String siebelString) {
        super(siebelString);
    }

   // @Override
    public void removeFirstBind(){
        this.keyList.remove(0);
        this.keyValueTable.remove(":1");
        this.keyValueTable.remove(":0");
        this.keyList.remove(":0");
        this.siebelLog = this.siebelLog.replaceAll(",\n.*:1", "");
    }

}
