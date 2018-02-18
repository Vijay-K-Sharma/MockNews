package mock.mocknews.utils;

import mock.mocknews.MockNewsHolder;

public class NewsUtils {
    private static MockNewsHolder sMockNewsHolder;
    public static void setNewsFeedHolder(MockNewsHolder mockNewsHolder){
        sMockNewsHolder = mockNewsHolder;
    }
    public static MockNewsHolder getsMockNewsHolder(){
        return sMockNewsHolder;
    }
}
