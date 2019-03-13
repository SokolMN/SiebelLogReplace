public class SelectStatement extends SQLStatement{

    public SelectStatement(String siebelString) {
        super(siebelString);
    }

    public void removeFirstBind(){
        this.keyList.remove(0);
        this.keyValueTable.remove(":1");
        this.siebelLog = this.siebelLog.replaceAll(",\n.*:1", "");
    }


}
