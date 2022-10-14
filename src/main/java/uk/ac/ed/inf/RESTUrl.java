package uk.ac.ed.inf;

/**
 * RESTUrl is a singleton class that ensures that only one url to the REST server is possible
 */
public final class RESTUrl {

    /**
     * To help ensure that only one restUrl ever exists
     */
    private static RESTUrl restUrl;

    /**
     * The base url to the REST server
     */
    private String url;

    /**
     * A method for if a url is not passed in when starting the program
     */
    private RESTUrl() {
        this.url = "https://ilp-rest.azurewebsites.net/";
    }

    /**
     * A method for when a url is passed in when starting the program
     * @param url the base url for the REST server
     */
    private RESTUrl(String url){
        this.url = url;
    }

    /**
     * getInstance makes sure to only ever have one instance of the RESTUrl class active, acting as a singleton
     * @param url the base url for the REST server
     * @return the RESTUrl instance
     */
    public static RESTUrl getInstance(String url) {

        //if the restUrl doesn't exist yet then no instance of RESTUrl exists
        if (restUrl == null){
            if (url != null){
                restUrl = new RESTUrl(url);
            } else{
                restUrl = new RESTUrl();
            }
        }

        return restUrl;

    }

    /**
     * Returns the url
     * @return the REST url
     */
    public String getUrl(){
        return this.url;
    }

}
