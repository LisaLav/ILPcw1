package uk.ac.ed.inf;

public final class CentralArea {

    private static CentralArea centralArea;

    private String url;

    private CentralArea() {
        this.url = "https://ilp-rest.azurewebsites.net/";
    }

    private CentralArea(String url){
        this.url = url;
    }

    public static CentralArea getInstance(String url) {
        if (centralArea == null){
            if (url != null){
                centralArea = new CentralArea(url);
            } else{
                centralArea = new CentralArea();
            }
        }
        return centralArea;
    }

    public String getUrl(){
        return this.url;
    }

}
