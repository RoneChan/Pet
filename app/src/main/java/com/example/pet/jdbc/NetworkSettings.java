package com.example.pet.jdbc;

public class NetworkSettings {
    public static final String HOST = "192.168.42.1";
    private static final String PORT = "8080";
    public static final String GEI_BY_CITY = "http://"+ HOST +":"+PORT + "/city";
    public static final String UPLOAD_IMAGE = "http://"+ HOST +":"+PORT + "/uploadimg";
    public static final String GET_BY_CATALOG = "http://"+ HOST +":"+PORT + "/catalog";
    public static final String GET_BY_ID = "http://"+ HOST +":"+PORT + "/id";
    public static final String GET_VEDIO = "http://"+ HOST +":"+PORT + "/video";
    public static final String CHANGE_COLLECT = "http://"+ HOST +":"+PORT + "/changeCollect";;
    public static final String CHANGE_ISSUE = "http://"+ HOST +":"+PORT + "/changeIssue";
    public static final String QUERY_COLLECT = "http://"+ HOST +":"+PORT + "/QueryCollect";
    public static final String GET_ISSUE = "http://"+ HOST +":"+PORT + "/issue";
    public static final String POST_PET = "http://"+ HOST +":"+PORT + "/postPet";
    public static final String INSERT_ADD = "http://"+ HOST +":"+PORT + "/insertAdd";
    public static final String LOGIN = "http://"+ HOST +":"+PORT + "/login";
}
