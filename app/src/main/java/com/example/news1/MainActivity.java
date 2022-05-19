package com.example.news1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView news_RV;
    private ArrayList<News> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news_RV = findViewById(R.id.news_RV);

    }
    private class GetNews extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputstream();
            if(inputStream!= null){
                try {
                    initXmlPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private  InputStream getInputstream(){
            try {
                URL url =new URL("https://www.autosport.com/rss/all/news/");
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                return connection.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        private  void  initXmlPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            parser.next();

            parser.require(XmlPullParser.START_TAG,null,"rss");

            while (parser.next()!= XmlPullParser.END_TAG){
                if (parser.getEventType()!= XmlPullParser.START_TAG){
                    continue;
                }
                parser.require(XmlPullParser.START_TAG,null,"channel");
                while(parser.next()!= XmlPullParser.END_TAG){
                    if(parser.getEventType()!= XmlPullParser.START_TAG){
                        continue;
                    }
                    String tagName = parser.getName();
                    if(tagName.equals("item")){
                        parser.require(XmlPullParser.START_TAG,null,"item");
                        String title="";
                        String link = "";
                        String description ="";
                        String date = "";

                        while (parser.next()!=XmlPullParser.END_TAG){
                            if(parser.getEventType()!= XmlPullParser.START_TAG){
                                continue;
                            }

                            String tagName1= parser.getName();
                            if(tagName1.equals("title")){
                                title = getInfo(parser,"title");
                            }
                            else if (tagName1.equals("link")){
                                link = getInfo(parser,"link");
                            }
                            else if (tagName1.equals("description")){
                                description = getInfo(parser,"description");
                            }
                            else if (tagName1.equals("pubdate")){
                                date = getInfo(parser,"pubdate");
                            }
                            else {
                                skipData(parser);
                            }
                        }
                        News newsitem = new News(title,description,link,date);
                        news.add(newsitem);
                    }
                    else {
                        skipData(parser);
                    }
                }
            }

        }
        private  String getInfo(XmlPullParser parser , String tag) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG,null,tag);

            String content ="";

            if(parser.next()== XmlPullParser.TEXT){
                content=parser.getText();
                parser.next();
            }
            return content;
        }
        private void skipData(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType()!=XmlPullParser.START_TAG){
                throw new IllegalStateException();
            }

            int num = 1;

            while (num!=0){
                switch (parser.next()){
                    case XmlPullParser.START_TAG:
                        num++;
                        break;

                    case XmlPullParser.END_TAG:
                        num--;
                        break;

                    default:
                        break;
                }
            }
        }
    }
}